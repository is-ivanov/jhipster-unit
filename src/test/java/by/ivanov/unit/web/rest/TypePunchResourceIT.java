package by.ivanov.unit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import by.ivanov.unit.IntegrationTest;
import by.ivanov.unit.domain.TypePunch;
import by.ivanov.unit.repository.TypePunchRepository;
import by.ivanov.unit.service.dto.TypePunchDTO;
import by.ivanov.unit.service.mapper.TypePunchMapper;
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
 * Integration tests for the {@link TypePunchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypePunchResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-punches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypePunchRepository typePunchRepository;

    @Autowired
    private TypePunchMapper typePunchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypePunchMockMvc;

    private TypePunch typePunch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePunch createEntity(EntityManager em) {
        TypePunch typePunch = new TypePunch().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return typePunch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePunch createUpdatedEntity(EntityManager em) {
        TypePunch typePunch = new TypePunch().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return typePunch;
    }

    @BeforeEach
    public void initTest() {
        typePunch = createEntity(em);
    }

    @Test
    @Transactional
    void createTypePunch() throws Exception {
        int databaseSizeBeforeCreate = typePunchRepository.findAll().size();
        // Create the TypePunch
        TypePunchDTO typePunchDTO = typePunchMapper.toDto(typePunch);
        restTypePunchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePunchDTO)))
            .andExpect(status().isCreated());

        // Validate the TypePunch in the database
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeCreate + 1);
        TypePunch testTypePunch = typePunchList.get(typePunchList.size() - 1);
        assertThat(testTypePunch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTypePunch.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createTypePunchWithExistingId() throws Exception {
        // Create the TypePunch with an existing ID
        typePunch.setId(1L);
        TypePunchDTO typePunchDTO = typePunchMapper.toDto(typePunch);

        int databaseSizeBeforeCreate = typePunchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypePunchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePunchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypePunch in the database
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = typePunchRepository.findAll().size();
        // set the field null
        typePunch.setName(null);

        // Create the TypePunch, which fails.
        TypePunchDTO typePunchDTO = typePunchMapper.toDto(typePunch);

        restTypePunchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePunchDTO)))
            .andExpect(status().isBadRequest());

        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypePunches() throws Exception {
        // Initialize the database
        typePunchRepository.saveAndFlush(typePunch);

        // Get all the typePunchList
        restTypePunchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typePunch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTypePunch() throws Exception {
        // Initialize the database
        typePunchRepository.saveAndFlush(typePunch);

        // Get the typePunch
        restTypePunchMockMvc
            .perform(get(ENTITY_API_URL_ID, typePunch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typePunch.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingTypePunch() throws Exception {
        // Get the typePunch
        restTypePunchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypePunch() throws Exception {
        // Initialize the database
        typePunchRepository.saveAndFlush(typePunch);

        int databaseSizeBeforeUpdate = typePunchRepository.findAll().size();

        // Update the typePunch
        TypePunch updatedTypePunch = typePunchRepository.findById(typePunch.getId()).get();
        // Disconnect from session so that the updates on updatedTypePunch are not directly saved in db
        em.detach(updatedTypePunch);
        updatedTypePunch.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        TypePunchDTO typePunchDTO = typePunchMapper.toDto(updatedTypePunch);

        restTypePunchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typePunchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePunchDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypePunch in the database
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeUpdate);
        TypePunch testTypePunch = typePunchList.get(typePunchList.size() - 1);
        assertThat(testTypePunch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTypePunch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingTypePunch() throws Exception {
        int databaseSizeBeforeUpdate = typePunchRepository.findAll().size();
        typePunch.setId(count.incrementAndGet());

        // Create the TypePunch
        TypePunchDTO typePunchDTO = typePunchMapper.toDto(typePunch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePunchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typePunchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePunch in the database
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypePunch() throws Exception {
        int databaseSizeBeforeUpdate = typePunchRepository.findAll().size();
        typePunch.setId(count.incrementAndGet());

        // Create the TypePunch
        TypePunchDTO typePunchDTO = typePunchMapper.toDto(typePunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePunchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePunch in the database
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypePunch() throws Exception {
        int databaseSizeBeforeUpdate = typePunchRepository.findAll().size();
        typePunch.setId(count.incrementAndGet());

        // Create the TypePunch
        TypePunchDTO typePunchDTO = typePunchMapper.toDto(typePunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePunchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePunchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypePunch in the database
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypePunchWithPatch() throws Exception {
        // Initialize the database
        typePunchRepository.saveAndFlush(typePunch);

        int databaseSizeBeforeUpdate = typePunchRepository.findAll().size();

        // Update the typePunch using partial update
        TypePunch partialUpdatedTypePunch = new TypePunch();
        partialUpdatedTypePunch.setId(typePunch.getId());

        partialUpdatedTypePunch.name(UPDATED_NAME);

        restTypePunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypePunch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePunch))
            )
            .andExpect(status().isOk());

        // Validate the TypePunch in the database
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeUpdate);
        TypePunch testTypePunch = typePunchList.get(typePunchList.size() - 1);
        assertThat(testTypePunch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTypePunch.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateTypePunchWithPatch() throws Exception {
        // Initialize the database
        typePunchRepository.saveAndFlush(typePunch);

        int databaseSizeBeforeUpdate = typePunchRepository.findAll().size();

        // Update the typePunch using partial update
        TypePunch partialUpdatedTypePunch = new TypePunch();
        partialUpdatedTypePunch.setId(typePunch.getId());

        partialUpdatedTypePunch.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTypePunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypePunch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePunch))
            )
            .andExpect(status().isOk());

        // Validate the TypePunch in the database
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeUpdate);
        TypePunch testTypePunch = typePunchList.get(typePunchList.size() - 1);
        assertThat(testTypePunch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTypePunch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTypePunch() throws Exception {
        int databaseSizeBeforeUpdate = typePunchRepository.findAll().size();
        typePunch.setId(count.incrementAndGet());

        // Create the TypePunch
        TypePunchDTO typePunchDTO = typePunchMapper.toDto(typePunch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typePunchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typePunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePunch in the database
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypePunch() throws Exception {
        int databaseSizeBeforeUpdate = typePunchRepository.findAll().size();
        typePunch.setId(count.incrementAndGet());

        // Create the TypePunch
        TypePunchDTO typePunchDTO = typePunchMapper.toDto(typePunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePunchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typePunchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePunch in the database
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypePunch() throws Exception {
        int databaseSizeBeforeUpdate = typePunchRepository.findAll().size();
        typePunch.setId(count.incrementAndGet());

        // Create the TypePunch
        TypePunchDTO typePunchDTO = typePunchMapper.toDto(typePunch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePunchMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typePunchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypePunch in the database
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypePunch() throws Exception {
        // Initialize the database
        typePunchRepository.saveAndFlush(typePunch);

        int databaseSizeBeforeDelete = typePunchRepository.findAll().size();

        // Delete the typePunch
        restTypePunchMockMvc
            .perform(delete(ENTITY_API_URL_ID, typePunch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypePunch> typePunchList = typePunchRepository.findAll();
        assertThat(typePunchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
