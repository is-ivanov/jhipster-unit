package by.ivanov.unit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import by.ivanov.unit.IntegrationTest;
import by.ivanov.unit.domain.Project;
import by.ivanov.unit.domain.PunchList;
import by.ivanov.unit.repository.PunchListRepository;
import by.ivanov.unit.service.PunchListService;
import by.ivanov.unit.service.criteria.PunchListCriteria;
import by.ivanov.unit.service.dto.PunchListDTO;
import by.ivanov.unit.service.mapper.PunchListMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PunchListResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PunchListResourceIT {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;
    private static final Integer SMALLER_NUMBER = 1 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/punch-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PunchListRepository punchListRepository;

    @Mock
    private PunchListRepository punchListRepositoryMock;

    @Autowired
    private PunchListMapper punchListMapper;

    @Mock
    private PunchListService punchListServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPunchListMockMvc;

    private PunchList punchList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PunchList createEntity(EntityManager em) {
        PunchList punchList = new PunchList().number(DEFAULT_NUMBER).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        punchList.setProject(project);
        return punchList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PunchList createUpdatedEntity(EntityManager em) {
        PunchList punchList = new PunchList().number(UPDATED_NUMBER).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createUpdatedEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        punchList.setProject(project);
        return punchList;
    }

    @BeforeEach
    public void initTest() {
        punchList = createEntity(em);
    }

    @Test
    @Transactional
    void createPunchList() throws Exception {
        int databaseSizeBeforeCreate = punchListRepository.findAll().size();
        // Create the PunchList
        PunchListDTO punchListDTO = punchListMapper.toDto(punchList);
        restPunchListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(punchListDTO)))
            .andExpect(status().isCreated());

        // Validate the PunchList in the database
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeCreate + 1);
        PunchList testPunchList = punchListList.get(punchListList.size() - 1);
        assertThat(testPunchList.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPunchList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPunchList.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPunchListWithExistingId() throws Exception {
        // Create the PunchList with an existing ID
        punchList.setId(1L);
        PunchListDTO punchListDTO = punchListMapper.toDto(punchList);

        int databaseSizeBeforeCreate = punchListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPunchListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(punchListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PunchList in the database
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = punchListRepository.findAll().size();
        // set the field null
        punchList.setNumber(null);

        // Create the PunchList, which fails.
        PunchListDTO punchListDTO = punchListMapper.toDto(punchList);

        restPunchListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(punchListDTO)))
            .andExpect(status().isBadRequest());

        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPunchLists() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList
        restPunchListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(punchList.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPunchListsWithEagerRelationshipsIsEnabled() throws Exception {
        when(punchListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPunchListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(punchListServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPunchListsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(punchListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPunchListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(punchListServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPunchList() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get the punchList
        restPunchListMockMvc
            .perform(get(ENTITY_API_URL_ID, punchList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(punchList.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getPunchListsByIdFiltering() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        Long id = punchList.getId();

        defaultPunchListShouldBeFound("id.equals=" + id);
        defaultPunchListShouldNotBeFound("id.notEquals=" + id);

        defaultPunchListShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPunchListShouldNotBeFound("id.greaterThan=" + id);

        defaultPunchListShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPunchListShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPunchListsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where number equals to DEFAULT_NUMBER
        defaultPunchListShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the punchListList where number equals to UPDATED_NUMBER
        defaultPunchListShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchListsByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where number not equals to DEFAULT_NUMBER
        defaultPunchListShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the punchListList where number not equals to UPDATED_NUMBER
        defaultPunchListShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchListsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultPunchListShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the punchListList where number equals to UPDATED_NUMBER
        defaultPunchListShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchListsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where number is not null
        defaultPunchListShouldBeFound("number.specified=true");

        // Get all the punchListList where number is null
        defaultPunchListShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllPunchListsByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where number is greater than or equal to DEFAULT_NUMBER
        defaultPunchListShouldBeFound("number.greaterThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the punchListList where number is greater than or equal to UPDATED_NUMBER
        defaultPunchListShouldNotBeFound("number.greaterThanOrEqual=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchListsByNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where number is less than or equal to DEFAULT_NUMBER
        defaultPunchListShouldBeFound("number.lessThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the punchListList where number is less than or equal to SMALLER_NUMBER
        defaultPunchListShouldNotBeFound("number.lessThanOrEqual=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchListsByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where number is less than DEFAULT_NUMBER
        defaultPunchListShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the punchListList where number is less than UPDATED_NUMBER
        defaultPunchListShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchListsByNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where number is greater than DEFAULT_NUMBER
        defaultPunchListShouldNotBeFound("number.greaterThan=" + DEFAULT_NUMBER);

        // Get all the punchListList where number is greater than SMALLER_NUMBER
        defaultPunchListShouldBeFound("number.greaterThan=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchListsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where name equals to DEFAULT_NAME
        defaultPunchListShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the punchListList where name equals to UPDATED_NAME
        defaultPunchListShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPunchListsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where name not equals to DEFAULT_NAME
        defaultPunchListShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the punchListList where name not equals to UPDATED_NAME
        defaultPunchListShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPunchListsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPunchListShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the punchListList where name equals to UPDATED_NAME
        defaultPunchListShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPunchListsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where name is not null
        defaultPunchListShouldBeFound("name.specified=true");

        // Get all the punchListList where name is null
        defaultPunchListShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPunchListsByNameContainsSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where name contains DEFAULT_NAME
        defaultPunchListShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the punchListList where name contains UPDATED_NAME
        defaultPunchListShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPunchListsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where name does not contain DEFAULT_NAME
        defaultPunchListShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the punchListList where name does not contain UPDATED_NAME
        defaultPunchListShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPunchListsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where description equals to DEFAULT_DESCRIPTION
        defaultPunchListShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the punchListList where description equals to UPDATED_DESCRIPTION
        defaultPunchListShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPunchListsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where description not equals to DEFAULT_DESCRIPTION
        defaultPunchListShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the punchListList where description not equals to UPDATED_DESCRIPTION
        defaultPunchListShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPunchListsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPunchListShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the punchListList where description equals to UPDATED_DESCRIPTION
        defaultPunchListShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPunchListsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where description is not null
        defaultPunchListShouldBeFound("description.specified=true");

        // Get all the punchListList where description is null
        defaultPunchListShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPunchListsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where description contains DEFAULT_DESCRIPTION
        defaultPunchListShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the punchListList where description contains UPDATED_DESCRIPTION
        defaultPunchListShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPunchListsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        // Get all the punchListList where description does not contain DEFAULT_DESCRIPTION
        defaultPunchListShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the punchListList where description does not contain UPDATED_DESCRIPTION
        defaultPunchListShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPunchListsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);
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
        punchList.setProject(project);
        punchListRepository.saveAndFlush(punchList);
        Long projectId = project.getId();

        // Get all the punchListList where project equals to projectId
        defaultPunchListShouldBeFound("projectId.equals=" + projectId);

        // Get all the punchListList where project equals to (projectId + 1)
        defaultPunchListShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPunchListShouldBeFound(String filter) throws Exception {
        restPunchListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(punchList.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPunchListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPunchListShouldNotBeFound(String filter) throws Exception {
        restPunchListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPunchListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPunchList() throws Exception {
        // Get the punchList
        restPunchListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPunchList() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        int databaseSizeBeforeUpdate = punchListRepository.findAll().size();

        // Update the punchList
        PunchList updatedPunchList = punchListRepository.findById(punchList.getId()).get();
        // Disconnect from session so that the updates on updatedPunchList are not directly saved in db
        em.detach(updatedPunchList);
        updatedPunchList.number(UPDATED_NUMBER).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        PunchListDTO punchListDTO = punchListMapper.toDto(updatedPunchList);

        restPunchListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, punchListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(punchListDTO))
            )
            .andExpect(status().isOk());

        // Validate the PunchList in the database
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeUpdate);
        PunchList testPunchList = punchListList.get(punchListList.size() - 1);
        assertThat(testPunchList.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPunchList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPunchList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPunchList() throws Exception {
        int databaseSizeBeforeUpdate = punchListRepository.findAll().size();
        punchList.setId(count.incrementAndGet());

        // Create the PunchList
        PunchListDTO punchListDTO = punchListMapper.toDto(punchList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPunchListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, punchListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(punchListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PunchList in the database
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPunchList() throws Exception {
        int databaseSizeBeforeUpdate = punchListRepository.findAll().size();
        punchList.setId(count.incrementAndGet());

        // Create the PunchList
        PunchListDTO punchListDTO = punchListMapper.toDto(punchList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPunchListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(punchListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PunchList in the database
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPunchList() throws Exception {
        int databaseSizeBeforeUpdate = punchListRepository.findAll().size();
        punchList.setId(count.incrementAndGet());

        // Create the PunchList
        PunchListDTO punchListDTO = punchListMapper.toDto(punchList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPunchListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(punchListDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PunchList in the database
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePunchListWithPatch() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        int databaseSizeBeforeUpdate = punchListRepository.findAll().size();

        // Update the punchList using partial update
        PunchList partialUpdatedPunchList = new PunchList();
        partialUpdatedPunchList.setId(punchList.getId());

        partialUpdatedPunchList.number(UPDATED_NUMBER);

        restPunchListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPunchList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPunchList))
            )
            .andExpect(status().isOk());

        // Validate the PunchList in the database
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeUpdate);
        PunchList testPunchList = punchListList.get(punchListList.size() - 1);
        assertThat(testPunchList.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPunchList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPunchList.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePunchListWithPatch() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        int databaseSizeBeforeUpdate = punchListRepository.findAll().size();

        // Update the punchList using partial update
        PunchList partialUpdatedPunchList = new PunchList();
        partialUpdatedPunchList.setId(punchList.getId());

        partialUpdatedPunchList.number(UPDATED_NUMBER).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPunchListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPunchList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPunchList))
            )
            .andExpect(status().isOk());

        // Validate the PunchList in the database
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeUpdate);
        PunchList testPunchList = punchListList.get(punchListList.size() - 1);
        assertThat(testPunchList.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPunchList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPunchList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPunchList() throws Exception {
        int databaseSizeBeforeUpdate = punchListRepository.findAll().size();
        punchList.setId(count.incrementAndGet());

        // Create the PunchList
        PunchListDTO punchListDTO = punchListMapper.toDto(punchList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPunchListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, punchListDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(punchListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PunchList in the database
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPunchList() throws Exception {
        int databaseSizeBeforeUpdate = punchListRepository.findAll().size();
        punchList.setId(count.incrementAndGet());

        // Create the PunchList
        PunchListDTO punchListDTO = punchListMapper.toDto(punchList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPunchListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(punchListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PunchList in the database
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPunchList() throws Exception {
        int databaseSizeBeforeUpdate = punchListRepository.findAll().size();
        punchList.setId(count.incrementAndGet());

        // Create the PunchList
        PunchListDTO punchListDTO = punchListMapper.toDto(punchList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPunchListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(punchListDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PunchList in the database
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePunchList() throws Exception {
        // Initialize the database
        punchListRepository.saveAndFlush(punchList);

        int databaseSizeBeforeDelete = punchListRepository.findAll().size();

        // Delete the punchList
        restPunchListMockMvc
            .perform(delete(ENTITY_API_URL_ID, punchList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PunchList> punchListList = punchListRepository.findAll();
        assertThat(punchListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
