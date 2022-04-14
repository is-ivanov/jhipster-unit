package by.ivanov.unit.web.rest;

import by.ivanov.unit.IntegrationTest;
import by.ivanov.unit.domain.PriorityPunch;
import by.ivanov.unit.repository.PriorityPunchRepository;
import by.ivanov.unit.security.AuthoritiesConstants;
import by.ivanov.unit.service.dto.PriorityPunchDTO;
import by.ivanov.unit.service.mapper.PriorityPunchMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PriorityPunchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PriorityPunchResourceIT {

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/priority-punches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PriorityPunchRepository priorityPunchRepository;

    @Autowired
    private PriorityPunchMapper priorityPunchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriorityPunchMockMvc;

    private PriorityPunch priorityPunch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriorityPunch createEntity(EntityManager em) {
        PriorityPunch priorityPunch = new PriorityPunch().priority(DEFAULT_PRIORITY).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return priorityPunch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriorityPunch createUpdatedEntity(EntityManager em) {
        PriorityPunch priorityPunch = new PriorityPunch().priority(UPDATED_PRIORITY).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return priorityPunch;
    }

    @BeforeEach
    public void initTest() {
        priorityPunch = createEntity(em);
    }

    @Test
    @Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void createPriorityPunch() throws Exception {
        int databaseSizeBeforeCreate = priorityPunchRepository.findAll().size();
        // Create the PriorityPunch
        PriorityPunchDTO priorityPunchDTO = priorityPunchMapper.toDto(priorityPunch);
        restPriorityPunchMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priorityPunchDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PriorityPunch in the database
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeCreate + 1);
        PriorityPunch testPriorityPunch = priorityPunchList.get(priorityPunchList.size() - 1);
        assertThat(testPriorityPunch.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testPriorityPunch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPriorityPunch.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void createPriorityPunchWithExistingId() throws Exception {
        // Create the PriorityPunch with an existing ID
        priorityPunch.setId(1L);
        PriorityPunchDTO priorityPunchDTO = priorityPunchMapper.toDto(priorityPunch);

        int databaseSizeBeforeCreate = priorityPunchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriorityPunchMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priorityPunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriorityPunch in the database
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = priorityPunchRepository.findAll().size();
        // set the field null
        priorityPunch.setPriority(null);

        // Create the PriorityPunch, which fails.
        PriorityPunchDTO priorityPunchDTO = priorityPunchMapper.toDto(priorityPunch);

        restPriorityPunchMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priorityPunchDTO))
            )
            .andExpect(status().isBadRequest());

        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = priorityPunchRepository.findAll().size();
        // set the field null
        priorityPunch.setName(null);

        // Create the PriorityPunch, which fails.
        PriorityPunchDTO priorityPunchDTO = priorityPunchMapper.toDto(priorityPunch);

        restPriorityPunchMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priorityPunchDTO))
            )
            .andExpect(status().isBadRequest());

        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPriorityPunches() throws Exception {
        // Initialize the database
        priorityPunchRepository.saveAndFlush(priorityPunch);

        // Get all the priorityPunchList
        restPriorityPunchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priorityPunch.getId().intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPriorityPunch() throws Exception {
        // Initialize the database
        priorityPunchRepository.saveAndFlush(priorityPunch);

        // Get the priorityPunch
        restPriorityPunchMockMvc
            .perform(get(ENTITY_API_URL_ID, priorityPunch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(priorityPunch.getId().intValue()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPriorityPunch() throws Exception {
        // Get the priorityPunch
        restPriorityPunchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void putNewPriorityPunch() throws Exception {
        // Initialize the database
        priorityPunchRepository.saveAndFlush(priorityPunch);

        int databaseSizeBeforeUpdate = priorityPunchRepository.findAll().size();

        // Update the priorityPunch
        PriorityPunch updatedPriorityPunch = priorityPunchRepository.findById(priorityPunch.getId()).get();
        // Disconnect from session so that the updates on updatedPriorityPunch are not directly saved in db
        em.detach(updatedPriorityPunch);
        updatedPriorityPunch.priority(UPDATED_PRIORITY).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        PriorityPunchDTO priorityPunchDTO = priorityPunchMapper.toDto(updatedPriorityPunch);

        restPriorityPunchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priorityPunchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priorityPunchDTO))
            )
            .andExpect(status().isOk());

        // Validate the PriorityPunch in the database
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeUpdate);
        PriorityPunch testPriorityPunch = priorityPunchList.get(priorityPunchList.size() - 1);
        assertThat(testPriorityPunch.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testPriorityPunch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPriorityPunch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void putNonExistingPriorityPunch() throws Exception {
        int databaseSizeBeforeUpdate = priorityPunchRepository.findAll().size();
        priorityPunch.setId(count.incrementAndGet());

        // Create the PriorityPunch
        PriorityPunchDTO priorityPunchDTO = priorityPunchMapper.toDto(priorityPunch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriorityPunchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priorityPunchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priorityPunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriorityPunch in the database
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void putWithIdMismatchPriorityPunch() throws Exception {
        int databaseSizeBeforeUpdate = priorityPunchRepository.findAll().size();
        priorityPunch.setId(count.incrementAndGet());

        // Create the PriorityPunch
        PriorityPunchDTO priorityPunchDTO = priorityPunchMapper.toDto(priorityPunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriorityPunchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priorityPunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriorityPunch in the database
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPriorityPunch() throws Exception {
        int databaseSizeBeforeUpdate = priorityPunchRepository.findAll().size();
        priorityPunch.setId(count.incrementAndGet());

        // Create the PriorityPunch
        PriorityPunchDTO priorityPunchDTO = priorityPunchMapper.toDto(priorityPunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriorityPunchMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priorityPunchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriorityPunch in the database
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void partialUpdatePriorityPunchWithPatch() throws Exception {
        // Initialize the database
        priorityPunchRepository.saveAndFlush(priorityPunch);

        int databaseSizeBeforeUpdate = priorityPunchRepository.findAll().size();

        // Update the priorityPunch using partial update
        PriorityPunch partialUpdatedPriorityPunch = new PriorityPunch();
        partialUpdatedPriorityPunch.setId(priorityPunch.getId());

        partialUpdatedPriorityPunch.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPriorityPunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriorityPunch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriorityPunch))
            )
            .andExpect(status().isOk());

        // Validate the PriorityPunch in the database
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeUpdate);
        PriorityPunch testPriorityPunch = priorityPunchList.get(priorityPunchList.size() - 1);
        assertThat(testPriorityPunch.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testPriorityPunch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPriorityPunch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void fullUpdatePriorityPunchWithPatch() throws Exception {
        // Initialize the database
        priorityPunchRepository.saveAndFlush(priorityPunch);

        int databaseSizeBeforeUpdate = priorityPunchRepository.findAll().size();

        // Update the priorityPunch using partial update
        PriorityPunch partialUpdatedPriorityPunch = new PriorityPunch();
        partialUpdatedPriorityPunch.setId(priorityPunch.getId());

        partialUpdatedPriorityPunch.priority(UPDATED_PRIORITY).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPriorityPunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriorityPunch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriorityPunch))
            )
            .andExpect(status().isOk());

        // Validate the PriorityPunch in the database
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeUpdate);
        PriorityPunch testPriorityPunch = priorityPunchList.get(priorityPunchList.size() - 1);
        assertThat(testPriorityPunch.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testPriorityPunch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPriorityPunch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void patchNonExistingPriorityPunch() throws Exception {
        int databaseSizeBeforeUpdate = priorityPunchRepository.findAll().size();
        priorityPunch.setId(count.incrementAndGet());

        // Create the PriorityPunch
        PriorityPunchDTO priorityPunchDTO = priorityPunchMapper.toDto(priorityPunch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriorityPunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, priorityPunchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priorityPunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriorityPunch in the database
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void patchWithIdMismatchPriorityPunch() throws Exception {
        int databaseSizeBeforeUpdate = priorityPunchRepository.findAll().size();
        priorityPunch.setId(count.incrementAndGet());

        // Create the PriorityPunch
        PriorityPunchDTO priorityPunchDTO = priorityPunchMapper.toDto(priorityPunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriorityPunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priorityPunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriorityPunch in the database
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPriorityPunch() throws Exception {
        int databaseSizeBeforeUpdate = priorityPunchRepository.findAll().size();
        priorityPunch.setId(count.incrementAndGet());

        // Create the PriorityPunch
        PriorityPunchDTO priorityPunchDTO = priorityPunchMapper.toDto(priorityPunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriorityPunchMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priorityPunchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriorityPunch in the database
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void deletePriorityPunch() throws Exception {
        // Initialize the database
        priorityPunchRepository.saveAndFlush(priorityPunch);

        int databaseSizeBeforeDelete = priorityPunchRepository.findAll().size();

        // Delete the priorityPunch
        restPriorityPunchMockMvc
            .perform(delete(ENTITY_API_URL_ID, priorityPunch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PriorityPunch> priorityPunchList = priorityPunchRepository.findAll();
        assertThat(priorityPunchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
