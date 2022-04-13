package by.ivanov.unit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import by.ivanov.unit.IntegrationTest;
import by.ivanov.unit.domain.CommentPunch;
import by.ivanov.unit.domain.PunchItem;
import by.ivanov.unit.repository.CommentPunchRepository;
import by.ivanov.unit.service.criteria.CommentPunchCriteria;
import by.ivanov.unit.service.dto.CommentPunchDTO;
import by.ivanov.unit.service.mapper.CommentPunchMapper;
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
 * Integration tests for the {@link CommentPunchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommentPunchResourceIT {

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/comment-punches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommentPunchRepository commentPunchRepository;

    @Autowired
    private CommentPunchMapper commentPunchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentPunchMockMvc;

    private CommentPunch commentPunch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentPunch createEntity(EntityManager em) {
        CommentPunch commentPunch = new CommentPunch().comment(DEFAULT_COMMENT);
        // Add required entity
        PunchItem punchItem;
        if (TestUtil.findAll(em, PunchItem.class).isEmpty()) {
            punchItem = PunchItemResourceIT.createEntity(em);
            em.persist(punchItem);
            em.flush();
        } else {
            punchItem = TestUtil.findAll(em, PunchItem.class).get(0);
        }
        commentPunch.setPunchItem(punchItem);
        return commentPunch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentPunch createUpdatedEntity(EntityManager em) {
        CommentPunch commentPunch = new CommentPunch().comment(UPDATED_COMMENT);
        // Add required entity
        PunchItem punchItem;
        if (TestUtil.findAll(em, PunchItem.class).isEmpty()) {
            punchItem = PunchItemResourceIT.createUpdatedEntity(em);
            em.persist(punchItem);
            em.flush();
        } else {
            punchItem = TestUtil.findAll(em, PunchItem.class).get(0);
        }
        commentPunch.setPunchItem(punchItem);
        return commentPunch;
    }

    @BeforeEach
    public void initTest() {
        commentPunch = createEntity(em);
    }

    @Test
    @Transactional
    void createCommentPunch() throws Exception {
        int databaseSizeBeforeCreate = commentPunchRepository.findAll().size();
        // Create the CommentPunch
        CommentPunchDTO commentPunchDTO = commentPunchMapper.toDto(commentPunch);
        restCommentPunchMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentPunchDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommentPunch in the database
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeCreate + 1);
        CommentPunch testCommentPunch = commentPunchList.get(commentPunchList.size() - 1);
        assertThat(testCommentPunch.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createCommentPunchWithExistingId() throws Exception {
        // Create the CommentPunch with an existing ID
        commentPunch.setId(1L);
        CommentPunchDTO commentPunchDTO = commentPunchMapper.toDto(commentPunch);

        int databaseSizeBeforeCreate = commentPunchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentPunchMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentPunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentPunch in the database
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentPunchRepository.findAll().size();
        // set the field null
        commentPunch.setComment(null);

        // Create the CommentPunch, which fails.
        CommentPunchDTO commentPunchDTO = commentPunchMapper.toDto(commentPunch);

        restCommentPunchMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentPunchDTO))
            )
            .andExpect(status().isBadRequest());

        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommentPunches() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        // Get all the commentPunchList
        restCommentPunchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentPunch.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getCommentPunch() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        // Get the commentPunch
        restCommentPunchMockMvc
            .perform(get(ENTITY_API_URL_ID, commentPunch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commentPunch.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getCommentPunchesByIdFiltering() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        Long id = commentPunch.getId();

        defaultCommentPunchShouldBeFound("id.equals=" + id);
        defaultCommentPunchShouldNotBeFound("id.notEquals=" + id);

        defaultCommentPunchShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommentPunchShouldNotBeFound("id.greaterThan=" + id);

        defaultCommentPunchShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommentPunchShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommentPunchesByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        // Get all the commentPunchList where comment equals to DEFAULT_COMMENT
        defaultCommentPunchShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the commentPunchList where comment equals to UPDATED_COMMENT
        defaultCommentPunchShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllCommentPunchesByCommentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        // Get all the commentPunchList where comment not equals to DEFAULT_COMMENT
        defaultCommentPunchShouldNotBeFound("comment.notEquals=" + DEFAULT_COMMENT);

        // Get all the commentPunchList where comment not equals to UPDATED_COMMENT
        defaultCommentPunchShouldBeFound("comment.notEquals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllCommentPunchesByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        // Get all the commentPunchList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultCommentPunchShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the commentPunchList where comment equals to UPDATED_COMMENT
        defaultCommentPunchShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllCommentPunchesByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        // Get all the commentPunchList where comment is not null
        defaultCommentPunchShouldBeFound("comment.specified=true");

        // Get all the commentPunchList where comment is null
        defaultCommentPunchShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentPunchesByCommentContainsSomething() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        // Get all the commentPunchList where comment contains DEFAULT_COMMENT
        defaultCommentPunchShouldBeFound("comment.contains=" + DEFAULT_COMMENT);

        // Get all the commentPunchList where comment contains UPDATED_COMMENT
        defaultCommentPunchShouldNotBeFound("comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllCommentPunchesByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        // Get all the commentPunchList where comment does not contain DEFAULT_COMMENT
        defaultCommentPunchShouldNotBeFound("comment.doesNotContain=" + DEFAULT_COMMENT);

        // Get all the commentPunchList where comment does not contain UPDATED_COMMENT
        defaultCommentPunchShouldBeFound("comment.doesNotContain=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllCommentPunchesByPunchItemIsEqualToSomething() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);
        PunchItem punchItem;
        if (TestUtil.findAll(em, PunchItem.class).isEmpty()) {
            punchItem = PunchItemResourceIT.createEntity(em);
            em.persist(punchItem);
            em.flush();
        } else {
            punchItem = TestUtil.findAll(em, PunchItem.class).get(0);
        }
        em.persist(punchItem);
        em.flush();
        commentPunch.setPunchItem(punchItem);
        commentPunchRepository.saveAndFlush(commentPunch);
        Long punchItemId = punchItem.getId();

        // Get all the commentPunchList where punchItem equals to punchItemId
        defaultCommentPunchShouldBeFound("punchItemId.equals=" + punchItemId);

        // Get all the commentPunchList where punchItem equals to (punchItemId + 1)
        defaultCommentPunchShouldNotBeFound("punchItemId.equals=" + (punchItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommentPunchShouldBeFound(String filter) throws Exception {
        restCommentPunchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentPunch.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));

        // Check, that the count call also returns 1
        restCommentPunchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommentPunchShouldNotBeFound(String filter) throws Exception {
        restCommentPunchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommentPunchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommentPunch() throws Exception {
        // Get the commentPunch
        restCommentPunchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommentPunch() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        int databaseSizeBeforeUpdate = commentPunchRepository.findAll().size();

        // Update the commentPunch
        CommentPunch updatedCommentPunch = commentPunchRepository.findById(commentPunch.getId()).get();
        // Disconnect from session so that the updates on updatedCommentPunch are not directly saved in db
        em.detach(updatedCommentPunch);
        updatedCommentPunch.comment(UPDATED_COMMENT);
        CommentPunchDTO commentPunchDTO = commentPunchMapper.toDto(updatedCommentPunch);

        restCommentPunchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentPunchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentPunchDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommentPunch in the database
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeUpdate);
        CommentPunch testCommentPunch = commentPunchList.get(commentPunchList.size() - 1);
        assertThat(testCommentPunch.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingCommentPunch() throws Exception {
        int databaseSizeBeforeUpdate = commentPunchRepository.findAll().size();
        commentPunch.setId(count.incrementAndGet());

        // Create the CommentPunch
        CommentPunchDTO commentPunchDTO = commentPunchMapper.toDto(commentPunch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentPunchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentPunchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentPunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentPunch in the database
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommentPunch() throws Exception {
        int databaseSizeBeforeUpdate = commentPunchRepository.findAll().size();
        commentPunch.setId(count.incrementAndGet());

        // Create the CommentPunch
        CommentPunchDTO commentPunchDTO = commentPunchMapper.toDto(commentPunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentPunchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentPunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentPunch in the database
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommentPunch() throws Exception {
        int databaseSizeBeforeUpdate = commentPunchRepository.findAll().size();
        commentPunch.setId(count.incrementAndGet());

        // Create the CommentPunch
        CommentPunchDTO commentPunchDTO = commentPunchMapper.toDto(commentPunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentPunchMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentPunchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentPunch in the database
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommentPunchWithPatch() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        int databaseSizeBeforeUpdate = commentPunchRepository.findAll().size();

        // Update the commentPunch using partial update
        CommentPunch partialUpdatedCommentPunch = new CommentPunch();
        partialUpdatedCommentPunch.setId(commentPunch.getId());

        partialUpdatedCommentPunch.comment(UPDATED_COMMENT);

        restCommentPunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentPunch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentPunch))
            )
            .andExpect(status().isOk());

        // Validate the CommentPunch in the database
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeUpdate);
        CommentPunch testCommentPunch = commentPunchList.get(commentPunchList.size() - 1);
        assertThat(testCommentPunch.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateCommentPunchWithPatch() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        int databaseSizeBeforeUpdate = commentPunchRepository.findAll().size();

        // Update the commentPunch using partial update
        CommentPunch partialUpdatedCommentPunch = new CommentPunch();
        partialUpdatedCommentPunch.setId(commentPunch.getId());

        partialUpdatedCommentPunch.comment(UPDATED_COMMENT);

        restCommentPunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentPunch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentPunch))
            )
            .andExpect(status().isOk());

        // Validate the CommentPunch in the database
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeUpdate);
        CommentPunch testCommentPunch = commentPunchList.get(commentPunchList.size() - 1);
        assertThat(testCommentPunch.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingCommentPunch() throws Exception {
        int databaseSizeBeforeUpdate = commentPunchRepository.findAll().size();
        commentPunch.setId(count.incrementAndGet());

        // Create the CommentPunch
        CommentPunchDTO commentPunchDTO = commentPunchMapper.toDto(commentPunch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentPunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentPunchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentPunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentPunch in the database
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommentPunch() throws Exception {
        int databaseSizeBeforeUpdate = commentPunchRepository.findAll().size();
        commentPunch.setId(count.incrementAndGet());

        // Create the CommentPunch
        CommentPunchDTO commentPunchDTO = commentPunchMapper.toDto(commentPunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentPunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentPunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentPunch in the database
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommentPunch() throws Exception {
        int databaseSizeBeforeUpdate = commentPunchRepository.findAll().size();
        commentPunch.setId(count.incrementAndGet());

        // Create the CommentPunch
        CommentPunchDTO commentPunchDTO = commentPunchMapper.toDto(commentPunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentPunchMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentPunchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentPunch in the database
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommentPunch() throws Exception {
        // Initialize the database
        commentPunchRepository.saveAndFlush(commentPunch);

        int databaseSizeBeforeDelete = commentPunchRepository.findAll().size();

        // Delete the commentPunch
        restCommentPunchMockMvc
            .perform(delete(ENTITY_API_URL_ID, commentPunch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommentPunch> commentPunchList = commentPunchRepository.findAll();
        assertThat(commentPunchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
