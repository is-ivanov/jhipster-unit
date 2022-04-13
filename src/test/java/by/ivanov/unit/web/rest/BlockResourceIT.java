package by.ivanov.unit.web.rest;

import by.ivanov.unit.IntegrationTest;
import by.ivanov.unit.domain.Block;
import by.ivanov.unit.domain.Project;
import by.ivanov.unit.repository.BlockRepository;
import by.ivanov.unit.security.AuthoritiesConstants;
import by.ivanov.unit.service.BlockService;
import by.ivanov.unit.service.dto.BlockDTO;
import by.ivanov.unit.service.mapper.BlockMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BlockResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BlockResourceIT {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;
    private static final Integer SMALLER_NUMBER = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/blocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BlockRepository blockRepository;

    @Mock
    private BlockRepository blockRepositoryMock;

    @Autowired
    private BlockMapper blockMapper;

    @Mock
    private BlockService blockServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlockMockMvc;

    private Block block;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Block createEntity(EntityManager em) {
        Block block = new Block().number(DEFAULT_NUMBER).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        block.setProject(project);
        return block;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Block createUpdatedEntity(EntityManager em) {
        Block block = new Block().number(UPDATED_NUMBER).description(UPDATED_DESCRIPTION);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createUpdatedEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        block.setProject(project);
        return block;
    }

    @BeforeEach
    public void initTest() {
        block = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void createBlock() throws Exception {
        int databaseSizeBeforeCreate = blockRepository.findAll().size();
        // Create the Block
        BlockDTO blockDTO = blockMapper.toDto(block);
        restBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isCreated());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate + 1);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testBlock.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void createBlockWithExistingId() throws Exception {
        // Create the Block with an existing ID
        block.setId(1L);
        BlockDTO blockDTO = blockMapper.toDto(block);

        int databaseSizeBeforeCreate = blockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = blockRepository.findAll().size();
        // set the field null
        block.setNumber(null);

        // Create the Block, which fails.
        BlockDTO blockDTO = blockMapper.toDto(block);

        restBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isBadRequest());

        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = blockRepository.findAll().size();
        // set the field null
        block.setDescription(null);

        // Create the Block, which fails.
        BlockDTO blockDTO = blockMapper.toDto(block);

        restBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isBadRequest());

        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBlocks() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList
        restBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(block.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBlocksWithEagerRelationshipsIsEnabled() throws Exception {
        when(blockServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBlockMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(blockServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBlocksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(blockServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBlockMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(blockServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get the block
        restBlockMockMvc
            .perform(get(ENTITY_API_URL_ID, block.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(block.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getBlocksByIdFiltering() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        Long id = block.getId();

        defaultBlockShouldBeFound("id.equals=" + id);
        defaultBlockShouldNotBeFound("id.notEquals=" + id);

        defaultBlockShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBlockShouldNotBeFound("id.greaterThan=" + id);

        defaultBlockShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBlockShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBlocksByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where number equals to DEFAULT_NUMBER
        defaultBlockShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the blockList where number equals to UPDATED_NUMBER
        defaultBlockShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllBlocksByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where number not equals to DEFAULT_NUMBER
        defaultBlockShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the blockList where number not equals to UPDATED_NUMBER
        defaultBlockShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllBlocksByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultBlockShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the blockList where number equals to UPDATED_NUMBER
        defaultBlockShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllBlocksByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where number is not null
        defaultBlockShouldBeFound("number.specified=true");

        // Get all the blockList where number is null
        defaultBlockShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllBlocksByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where number is greater than or equal to DEFAULT_NUMBER
        defaultBlockShouldBeFound("number.greaterThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the blockList where number is greater than or equal to UPDATED_NUMBER
        defaultBlockShouldNotBeFound("number.greaterThanOrEqual=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllBlocksByNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where number is less than or equal to DEFAULT_NUMBER
        defaultBlockShouldBeFound("number.lessThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the blockList where number is less than or equal to SMALLER_NUMBER
        defaultBlockShouldNotBeFound("number.lessThanOrEqual=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllBlocksByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where number is less than DEFAULT_NUMBER
        defaultBlockShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the blockList where number is less than UPDATED_NUMBER
        defaultBlockShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllBlocksByNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where number is greater than DEFAULT_NUMBER
        defaultBlockShouldNotBeFound("number.greaterThan=" + DEFAULT_NUMBER);

        // Get all the blockList where number is greater than SMALLER_NUMBER
        defaultBlockShouldBeFound("number.greaterThan=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllBlocksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where description equals to DEFAULT_DESCRIPTION
        defaultBlockShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the blockList where description equals to UPDATED_DESCRIPTION
        defaultBlockShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBlocksByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where description not equals to DEFAULT_DESCRIPTION
        defaultBlockShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the blockList where description not equals to UPDATED_DESCRIPTION
        defaultBlockShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBlocksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBlockShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the blockList where description equals to UPDATED_DESCRIPTION
        defaultBlockShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBlocksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where description is not null
        defaultBlockShouldBeFound("description.specified=true");

        // Get all the blockList where description is null
        defaultBlockShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllBlocksByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where description contains DEFAULT_DESCRIPTION
        defaultBlockShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the blockList where description contains UPDATED_DESCRIPTION
        defaultBlockShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBlocksByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where description does not contain DEFAULT_DESCRIPTION
        defaultBlockShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the blockList where description does not contain UPDATED_DESCRIPTION
        defaultBlockShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBlocksByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(project);
        em.flush();
        block.setProject(project);
        blockRepository.saveAndFlush(block);
        Long projectId = project.getId();

        // Get all the blockList where project equals to projectId
        defaultBlockShouldBeFound("projectId.equals=" + projectId);

        // Get all the blockList where project equals to (projectId + 1)
        defaultBlockShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBlockShouldBeFound(String filter) throws Exception {
        restBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(block.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restBlockMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBlockShouldNotBeFound(String filter) throws Exception {
        restBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBlockMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBlock() throws Exception {
        // Get the block
        restBlockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void putNewBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block
        Block updatedBlock = blockRepository.findById(block.getId()).get();
        // Disconnect from session so that the updates on updatedBlock are not directly saved in db
        em.detach(updatedBlock);
        updatedBlock.number(UPDATED_NUMBER).description(UPDATED_DESCRIPTION);
        BlockDTO blockDTO = blockMapper.toDto(updatedBlock);

        restBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blockDTO))
            )
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testBlock.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void putNonExistingBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // Create the Block
        BlockDTO blockDTO = blockMapper.toDto(block);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void putWithIdMismatchBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // Create the Block
        BlockDTO blockDTO = blockMapper.toDto(block);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // Create the Block
        BlockDTO blockDTO = blockMapper.toDto(block);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void partialUpdateBlockWithPatch() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block using partial update
        Block partialUpdatedBlock = new Block();
        partialUpdatedBlock.setId(block.getId());

        partialUpdatedBlock.number(UPDATED_NUMBER);

        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlock))
            )
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testBlock.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void fullUpdateBlockWithPatch() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block using partial update
        Block partialUpdatedBlock = new Block();
        partialUpdatedBlock.setId(block.getId());

        partialUpdatedBlock.number(UPDATED_NUMBER).description(UPDATED_DESCRIPTION);

        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlock))
            )
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testBlock.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void patchNonExistingBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // Create the Block
        BlockDTO blockDTO = blockMapper.toDto(block);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, blockDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void patchWithIdMismatchBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // Create the Block
        BlockDTO blockDTO = blockMapper.toDto(block);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // Create the Block
        BlockDTO blockDTO = blockMapper.toDto(block);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void deleteBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeDelete = blockRepository.findAll().size();

        // Delete the block
        restBlockMockMvc
            .perform(delete(ENTITY_API_URL_ID, block.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
