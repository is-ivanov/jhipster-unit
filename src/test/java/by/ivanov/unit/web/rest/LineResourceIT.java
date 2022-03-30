package by.ivanov.unit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import by.ivanov.unit.IntegrationTest;
import by.ivanov.unit.domain.Block;
import by.ivanov.unit.domain.Line;
import by.ivanov.unit.domain.enumeration.StatusLine;
import by.ivanov.unit.repository.LineRepository;
import by.ivanov.unit.service.criteria.LineCriteria;
import by.ivanov.unit.service.dto.LineDTO;
import by.ivanov.unit.service.mapper.LineMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LineResourceIT {

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_REVISION = "AAAAAAAAAA";
    private static final String UPDATED_REVISION = "BBBBBBBBBB";

    private static final StatusLine DEFAULT_STATUS = StatusLine.NEW;
    private static final StatusLine UPDATED_STATUS = StatusLine.IN_PROGRESS;

    private static final String ENTITY_API_URL = "/api/lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineMapper lineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLineMockMvc;

    private Line line;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Line createEntity(EntityManager em) {
        Line line = new Line().tag(DEFAULT_TAG).revision(DEFAULT_REVISION).status(DEFAULT_STATUS);
        // Add required entity
        Block block;
        if (TestUtil.findAll(em, Block.class).isEmpty()) {
            block = BlockResourceIT.createEntity(em);
            em.persist(block);
            em.flush();
        } else {
            block = TestUtil.findAll(em, Block.class).get(0);
        }
        line.setBlock(block);
        return line;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Line createUpdatedEntity(EntityManager em) {
        Line line = new Line().tag(UPDATED_TAG).revision(UPDATED_REVISION).status(UPDATED_STATUS);
        // Add required entity
        Block block;
        if (TestUtil.findAll(em, Block.class).isEmpty()) {
            block = BlockResourceIT.createUpdatedEntity(em);
            em.persist(block);
            em.flush();
        } else {
            block = TestUtil.findAll(em, Block.class).get(0);
        }
        line.setBlock(block);
        return line;
    }

    @BeforeEach
    public void initTest() {
        line = createEntity(em);
    }

    @Test
    @Transactional
    void createLine() throws Exception {
        int databaseSizeBeforeCreate = lineRepository.findAll().size();
        // Create the Line
        LineDTO lineDTO = lineMapper.toDto(line);
        restLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isCreated());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeCreate + 1);
        Line testLine = lineList.get(lineList.size() - 1);
        assertThat(testLine.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testLine.getRevision()).isEqualTo(DEFAULT_REVISION);
        assertThat(testLine.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createLineWithExistingId() throws Exception {
        // Create the Line with an existing ID
        line.setId(1L);
        LineDTO lineDTO = lineMapper.toDto(line);

        int databaseSizeBeforeCreate = lineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineRepository.findAll().size();
        // set the field null
        line.setTag(null);

        // Create the Line, which fails.
        LineDTO lineDTO = lineMapper.toDto(line);

        restLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isBadRequest());

        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRevisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineRepository.findAll().size();
        // set the field null
        line.setRevision(null);

        // Create the Line, which fails.
        LineDTO lineDTO = lineMapper.toDto(line);

        restLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isBadRequest());

        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineRepository.findAll().size();
        // set the field null
        line.setStatus(null);

        // Create the Line, which fails.
        LineDTO lineDTO = lineMapper.toDto(line);

        restLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isBadRequest());

        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLines() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList
        restLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(line.getId().intValue())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
            .andExpect(jsonPath("$.[*].revision").value(hasItem(DEFAULT_REVISION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get the line
        restLineMockMvc
            .perform(get(ENTITY_API_URL_ID, line.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(line.getId().intValue()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG))
            .andExpect(jsonPath("$.revision").value(DEFAULT_REVISION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getLinesByIdFiltering() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        Long id = line.getId();

        defaultLineShouldBeFound("id.equals=" + id);
        defaultLineShouldNotBeFound("id.notEquals=" + id);

        defaultLineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLineShouldNotBeFound("id.greaterThan=" + id);

        defaultLineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLinesByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where tag equals to DEFAULT_TAG
        defaultLineShouldBeFound("tag.equals=" + DEFAULT_TAG);

        // Get all the lineList where tag equals to UPDATED_TAG
        defaultLineShouldNotBeFound("tag.equals=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    void getAllLinesByTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where tag not equals to DEFAULT_TAG
        defaultLineShouldNotBeFound("tag.notEquals=" + DEFAULT_TAG);

        // Get all the lineList where tag not equals to UPDATED_TAG
        defaultLineShouldBeFound("tag.notEquals=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    void getAllLinesByTagIsInShouldWork() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where tag in DEFAULT_TAG or UPDATED_TAG
        defaultLineShouldBeFound("tag.in=" + DEFAULT_TAG + "," + UPDATED_TAG);

        // Get all the lineList where tag equals to UPDATED_TAG
        defaultLineShouldNotBeFound("tag.in=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    void getAllLinesByTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where tag is not null
        defaultLineShouldBeFound("tag.specified=true");

        // Get all the lineList where tag is null
        defaultLineShouldNotBeFound("tag.specified=false");
    }

    @Test
    @Transactional
    void getAllLinesByTagContainsSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where tag contains DEFAULT_TAG
        defaultLineShouldBeFound("tag.contains=" + DEFAULT_TAG);

        // Get all the lineList where tag contains UPDATED_TAG
        defaultLineShouldNotBeFound("tag.contains=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    void getAllLinesByTagNotContainsSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where tag does not contain DEFAULT_TAG
        defaultLineShouldNotBeFound("tag.doesNotContain=" + DEFAULT_TAG);

        // Get all the lineList where tag does not contain UPDATED_TAG
        defaultLineShouldBeFound("tag.doesNotContain=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    void getAllLinesByRevisionIsEqualToSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where revision equals to DEFAULT_REVISION
        defaultLineShouldBeFound("revision.equals=" + DEFAULT_REVISION);

        // Get all the lineList where revision equals to UPDATED_REVISION
        defaultLineShouldNotBeFound("revision.equals=" + UPDATED_REVISION);
    }

    @Test
    @Transactional
    void getAllLinesByRevisionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where revision not equals to DEFAULT_REVISION
        defaultLineShouldNotBeFound("revision.notEquals=" + DEFAULT_REVISION);

        // Get all the lineList where revision not equals to UPDATED_REVISION
        defaultLineShouldBeFound("revision.notEquals=" + UPDATED_REVISION);
    }

    @Test
    @Transactional
    void getAllLinesByRevisionIsInShouldWork() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where revision in DEFAULT_REVISION or UPDATED_REVISION
        defaultLineShouldBeFound("revision.in=" + DEFAULT_REVISION + "," + UPDATED_REVISION);

        // Get all the lineList where revision equals to UPDATED_REVISION
        defaultLineShouldNotBeFound("revision.in=" + UPDATED_REVISION);
    }

    @Test
    @Transactional
    void getAllLinesByRevisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where revision is not null
        defaultLineShouldBeFound("revision.specified=true");

        // Get all the lineList where revision is null
        defaultLineShouldNotBeFound("revision.specified=false");
    }

    @Test
    @Transactional
    void getAllLinesByRevisionContainsSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where revision contains DEFAULT_REVISION
        defaultLineShouldBeFound("revision.contains=" + DEFAULT_REVISION);

        // Get all the lineList where revision contains UPDATED_REVISION
        defaultLineShouldNotBeFound("revision.contains=" + UPDATED_REVISION);
    }

    @Test
    @Transactional
    void getAllLinesByRevisionNotContainsSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where revision does not contain DEFAULT_REVISION
        defaultLineShouldNotBeFound("revision.doesNotContain=" + DEFAULT_REVISION);

        // Get all the lineList where revision does not contain UPDATED_REVISION
        defaultLineShouldBeFound("revision.doesNotContain=" + UPDATED_REVISION);
    }

    @Test
    @Transactional
    void getAllLinesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where status equals to DEFAULT_STATUS
        defaultLineShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the lineList where status equals to UPDATED_STATUS
        defaultLineShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLinesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where status not equals to DEFAULT_STATUS
        defaultLineShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the lineList where status not equals to UPDATED_STATUS
        defaultLineShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLinesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultLineShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the lineList where status equals to UPDATED_STATUS
        defaultLineShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLinesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where status is not null
        defaultLineShouldBeFound("status.specified=true");

        // Get all the lineList where status is null
        defaultLineShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllLinesByBlockIsEqualToSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);
        Block block;
        if (TestUtil.findAll(em, Block.class).isEmpty()) {
            block = BlockResourceIT.createEntity(em);
            em.persist(block);
            em.flush();
        } else {
            block = TestUtil.findAll(em, Block.class).get(0);
        }
        em.persist(block);
        em.flush();
        line.setBlock(block);
        lineRepository.saveAndFlush(line);
        Long blockId = block.getId();

        // Get all the lineList where block equals to blockId
        defaultLineShouldBeFound("blockId.equals=" + blockId);

        // Get all the lineList where block equals to (blockId + 1)
        defaultLineShouldNotBeFound("blockId.equals=" + (blockId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLineShouldBeFound(String filter) throws Exception {
        restLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(line.getId().intValue())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
            .andExpect(jsonPath("$.[*].revision").value(hasItem(DEFAULT_REVISION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restLineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLineShouldNotBeFound(String filter) throws Exception {
        restLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLine() throws Exception {
        // Get the line
        restLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        int databaseSizeBeforeUpdate = lineRepository.findAll().size();

        // Update the line
        Line updatedLine = lineRepository.findById(line.getId()).get();
        // Disconnect from session so that the updates on updatedLine are not directly saved in db
        em.detach(updatedLine);
        updatedLine.tag(UPDATED_TAG).revision(UPDATED_REVISION).status(UPDATED_STATUS);
        LineDTO lineDTO = lineMapper.toDto(updatedLine);

        restLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
        Line testLine = lineList.get(lineList.size() - 1);
        assertThat(testLine.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testLine.getRevision()).isEqualTo(UPDATED_REVISION);
        assertThat(testLine.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingLine() throws Exception {
        int databaseSizeBeforeUpdate = lineRepository.findAll().size();
        line.setId(count.incrementAndGet());

        // Create the Line
        LineDTO lineDTO = lineMapper.toDto(line);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLine() throws Exception {
        int databaseSizeBeforeUpdate = lineRepository.findAll().size();
        line.setId(count.incrementAndGet());

        // Create the Line
        LineDTO lineDTO = lineMapper.toDto(line);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLine() throws Exception {
        int databaseSizeBeforeUpdate = lineRepository.findAll().size();
        line.setId(count.incrementAndGet());

        // Create the Line
        LineDTO lineDTO = lineMapper.toDto(line);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLineWithPatch() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        int databaseSizeBeforeUpdate = lineRepository.findAll().size();

        // Update the line using partial update
        Line partialUpdatedLine = new Line();
        partialUpdatedLine.setId(line.getId());

        partialUpdatedLine.status(UPDATED_STATUS);

        restLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLine))
            )
            .andExpect(status().isOk());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
        Line testLine = lineList.get(lineList.size() - 1);
        assertThat(testLine.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testLine.getRevision()).isEqualTo(DEFAULT_REVISION);
        assertThat(testLine.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateLineWithPatch() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        int databaseSizeBeforeUpdate = lineRepository.findAll().size();

        // Update the line using partial update
        Line partialUpdatedLine = new Line();
        partialUpdatedLine.setId(line.getId());

        partialUpdatedLine.tag(UPDATED_TAG).revision(UPDATED_REVISION).status(UPDATED_STATUS);

        restLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLine))
            )
            .andExpect(status().isOk());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
        Line testLine = lineList.get(lineList.size() - 1);
        assertThat(testLine.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testLine.getRevision()).isEqualTo(UPDATED_REVISION);
        assertThat(testLine.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingLine() throws Exception {
        int databaseSizeBeforeUpdate = lineRepository.findAll().size();
        line.setId(count.incrementAndGet());

        // Create the Line
        LineDTO lineDTO = lineMapper.toDto(line);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLine() throws Exception {
        int databaseSizeBeforeUpdate = lineRepository.findAll().size();
        line.setId(count.incrementAndGet());

        // Create the Line
        LineDTO lineDTO = lineMapper.toDto(line);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLine() throws Exception {
        int databaseSizeBeforeUpdate = lineRepository.findAll().size();
        line.setId(count.incrementAndGet());

        // Create the Line
        LineDTO lineDTO = lineMapper.toDto(line);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        int databaseSizeBeforeDelete = lineRepository.findAll().size();

        // Delete the line
        restLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, line.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
