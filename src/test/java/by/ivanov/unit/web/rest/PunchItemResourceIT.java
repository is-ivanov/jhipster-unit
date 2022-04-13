package by.ivanov.unit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import by.ivanov.unit.IntegrationTest;
import by.ivanov.unit.domain.CommentPunch;
import by.ivanov.unit.domain.Company;
import by.ivanov.unit.domain.Line;
import by.ivanov.unit.domain.PriorityPunch;
import by.ivanov.unit.domain.PunchItem;
import by.ivanov.unit.domain.PunchList;
import by.ivanov.unit.domain.TypePunch;
import by.ivanov.unit.domain.User;
import by.ivanov.unit.domain.enumeration.StatusPunch;
import by.ivanov.unit.repository.PunchItemRepository;
import by.ivanov.unit.service.criteria.PunchItemCriteria;
import by.ivanov.unit.service.dto.PunchItemDTO;
import by.ivanov.unit.service.mapper.PunchItemMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PunchItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PunchItemResourceIT {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;
    private static final Integer SMALLER_NUMBER = 1 - 1;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REVISION_DRAWING = "AAAAAAAAAA";
    private static final String UPDATED_REVISION_DRAWING = "BBBBBBBBBB";

    private static final StatusPunch DEFAULT_STATUS = StatusPunch.INITIATED;
    private static final StatusPunch UPDATED_STATUS = StatusPunch.READY_FOR_REVIEW;

    private static final Instant DEFAULT_CLOSED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/punch-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PunchItemRepository punchItemRepository;

    @Autowired
    private PunchItemMapper punchItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPunchItemMockMvc;

    private PunchItem punchItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PunchItem createEntity(EntityManager em) {
        PunchItem punchItem = new PunchItem()
            .number(DEFAULT_NUMBER)
            .location(DEFAULT_LOCATION)
            .description(DEFAULT_DESCRIPTION)
            .revisionDrawing(DEFAULT_REVISION_DRAWING)
            .status(DEFAULT_STATUS)
            .closedDate(DEFAULT_CLOSED_DATE);
        // Add required entity
        TypePunch typePunch;
        if (TestUtil.findAll(em, TypePunch.class).isEmpty()) {
            typePunch = TypePunchResourceIT.createEntity(em);
            em.persist(typePunch);
            em.flush();
        } else {
            typePunch = TestUtil.findAll(em, TypePunch.class).get(0);
        }
        punchItem.setType(typePunch);
        // Add required entity
        PunchList punchList;
        if (TestUtil.findAll(em, PunchList.class).isEmpty()) {
            punchList = PunchListResourceIT.createEntity(em);
            em.persist(punchList);
            em.flush();
        } else {
            punchList = TestUtil.findAll(em, PunchList.class).get(0);
        }
        punchItem.setPunchList(punchList);
        // Add required entity
        PriorityPunch priorityPunch;
        if (TestUtil.findAll(em, PriorityPunch.class).isEmpty()) {
            priorityPunch = PriorityPunchResourceIT.createEntity(em);
            em.persist(priorityPunch);
            em.flush();
        } else {
            priorityPunch = TestUtil.findAll(em, PriorityPunch.class).get(0);
        }
        punchItem.setPriority(priorityPunch);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        punchItem.setAuthor(user);
        return punchItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PunchItem createUpdatedEntity(EntityManager em) {
        PunchItem punchItem = new PunchItem()
            .number(UPDATED_NUMBER)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .revisionDrawing(UPDATED_REVISION_DRAWING)
            .status(UPDATED_STATUS)
            .closedDate(UPDATED_CLOSED_DATE);
        // Add required entity
        TypePunch typePunch;
        if (TestUtil.findAll(em, TypePunch.class).isEmpty()) {
            typePunch = TypePunchResourceIT.createUpdatedEntity(em);
            em.persist(typePunch);
            em.flush();
        } else {
            typePunch = TestUtil.findAll(em, TypePunch.class).get(0);
        }
        punchItem.setType(typePunch);
        // Add required entity
        PunchList punchList;
        if (TestUtil.findAll(em, PunchList.class).isEmpty()) {
            punchList = PunchListResourceIT.createUpdatedEntity(em);
            em.persist(punchList);
            em.flush();
        } else {
            punchList = TestUtil.findAll(em, PunchList.class).get(0);
        }
        punchItem.setPunchList(punchList);
        // Add required entity
        PriorityPunch priorityPunch;
        if (TestUtil.findAll(em, PriorityPunch.class).isEmpty()) {
            priorityPunch = PriorityPunchResourceIT.createUpdatedEntity(em);
            em.persist(priorityPunch);
            em.flush();
        } else {
            priorityPunch = TestUtil.findAll(em, PriorityPunch.class).get(0);
        }
        punchItem.setPriority(priorityPunch);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        punchItem.setAuthor(user);
        return punchItem;
    }

    @BeforeEach
    public void initTest() {
        punchItem = createEntity(em);
    }

    @Test
    @Transactional
    void createPunchItem() throws Exception {
        int databaseSizeBeforeCreate = punchItemRepository.findAll().size();
        // Create the PunchItem
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(punchItem);
        restPunchItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(punchItemDTO)))
            .andExpect(status().isCreated());

        // Validate the PunchItem in the database
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeCreate + 1);
        PunchItem testPunchItem = punchItemList.get(punchItemList.size() - 1);
        assertThat(testPunchItem.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPunchItem.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testPunchItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPunchItem.getRevisionDrawing()).isEqualTo(DEFAULT_REVISION_DRAWING);
        assertThat(testPunchItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPunchItem.getClosedDate()).isEqualTo(DEFAULT_CLOSED_DATE);
    }

    @Test
    @Transactional
    void createPunchItemWithExistingId() throws Exception {
        // Create the PunchItem with an existing ID
        punchItem.setId(1L);
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(punchItem);

        int databaseSizeBeforeCreate = punchItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPunchItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(punchItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PunchItem in the database
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = punchItemRepository.findAll().size();
        // set the field null
        punchItem.setNumber(null);

        // Create the PunchItem, which fails.
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(punchItem);

        restPunchItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(punchItemDTO)))
            .andExpect(status().isBadRequest());

        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = punchItemRepository.findAll().size();
        // set the field null
        punchItem.setDescription(null);

        // Create the PunchItem, which fails.
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(punchItem);

        restPunchItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(punchItemDTO)))
            .andExpect(status().isBadRequest());

        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = punchItemRepository.findAll().size();
        // set the field null
        punchItem.setStatus(null);

        // Create the PunchItem, which fails.
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(punchItem);

        restPunchItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(punchItemDTO)))
            .andExpect(status().isBadRequest());

        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPunchItems() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList
        restPunchItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(punchItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].revisionDrawing").value(hasItem(DEFAULT_REVISION_DRAWING)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPunchItem() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get the punchItem
        restPunchItemMockMvc
            .perform(get(ENTITY_API_URL_ID, punchItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(punchItem.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.revisionDrawing").value(DEFAULT_REVISION_DRAWING))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.closedDate").value(DEFAULT_CLOSED_DATE.toString()));
    }

    @Test
    @Transactional
    void getPunchItemsByIdFiltering() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        Long id = punchItem.getId();

        defaultPunchItemShouldBeFound("id.equals=" + id);
        defaultPunchItemShouldNotBeFound("id.notEquals=" + id);

        defaultPunchItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPunchItemShouldNotBeFound("id.greaterThan=" + id);

        defaultPunchItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPunchItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPunchItemsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where number equals to DEFAULT_NUMBER
        defaultPunchItemShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the punchItemList where number equals to UPDATED_NUMBER
        defaultPunchItemShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchItemsByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where number not equals to DEFAULT_NUMBER
        defaultPunchItemShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the punchItemList where number not equals to UPDATED_NUMBER
        defaultPunchItemShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchItemsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultPunchItemShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the punchItemList where number equals to UPDATED_NUMBER
        defaultPunchItemShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchItemsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where number is not null
        defaultPunchItemShouldBeFound("number.specified=true");

        // Get all the punchItemList where number is null
        defaultPunchItemShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllPunchItemsByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where number is greater than or equal to DEFAULT_NUMBER
        defaultPunchItemShouldBeFound("number.greaterThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the punchItemList where number is greater than or equal to UPDATED_NUMBER
        defaultPunchItemShouldNotBeFound("number.greaterThanOrEqual=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchItemsByNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where number is less than or equal to DEFAULT_NUMBER
        defaultPunchItemShouldBeFound("number.lessThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the punchItemList where number is less than or equal to SMALLER_NUMBER
        defaultPunchItemShouldNotBeFound("number.lessThanOrEqual=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchItemsByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where number is less than DEFAULT_NUMBER
        defaultPunchItemShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the punchItemList where number is less than UPDATED_NUMBER
        defaultPunchItemShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchItemsByNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where number is greater than DEFAULT_NUMBER
        defaultPunchItemShouldNotBeFound("number.greaterThan=" + DEFAULT_NUMBER);

        // Get all the punchItemList where number is greater than SMALLER_NUMBER
        defaultPunchItemShouldBeFound("number.greaterThan=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPunchItemsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where location equals to DEFAULT_LOCATION
        defaultPunchItemShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the punchItemList where location equals to UPDATED_LOCATION
        defaultPunchItemShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllPunchItemsByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where location not equals to DEFAULT_LOCATION
        defaultPunchItemShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the punchItemList where location not equals to UPDATED_LOCATION
        defaultPunchItemShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllPunchItemsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultPunchItemShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the punchItemList where location equals to UPDATED_LOCATION
        defaultPunchItemShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllPunchItemsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where location is not null
        defaultPunchItemShouldBeFound("location.specified=true");

        // Get all the punchItemList where location is null
        defaultPunchItemShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllPunchItemsByLocationContainsSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where location contains DEFAULT_LOCATION
        defaultPunchItemShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the punchItemList where location contains UPDATED_LOCATION
        defaultPunchItemShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllPunchItemsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where location does not contain DEFAULT_LOCATION
        defaultPunchItemShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the punchItemList where location does not contain UPDATED_LOCATION
        defaultPunchItemShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllPunchItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where description equals to DEFAULT_DESCRIPTION
        defaultPunchItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the punchItemList where description equals to UPDATED_DESCRIPTION
        defaultPunchItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPunchItemsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where description not equals to DEFAULT_DESCRIPTION
        defaultPunchItemShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the punchItemList where description not equals to UPDATED_DESCRIPTION
        defaultPunchItemShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPunchItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPunchItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the punchItemList where description equals to UPDATED_DESCRIPTION
        defaultPunchItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPunchItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where description is not null
        defaultPunchItemShouldBeFound("description.specified=true");

        // Get all the punchItemList where description is null
        defaultPunchItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPunchItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where description contains DEFAULT_DESCRIPTION
        defaultPunchItemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the punchItemList where description contains UPDATED_DESCRIPTION
        defaultPunchItemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPunchItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where description does not contain DEFAULT_DESCRIPTION
        defaultPunchItemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the punchItemList where description does not contain UPDATED_DESCRIPTION
        defaultPunchItemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPunchItemsByRevisionDrawingIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where revisionDrawing equals to DEFAULT_REVISION_DRAWING
        defaultPunchItemShouldBeFound("revisionDrawing.equals=" + DEFAULT_REVISION_DRAWING);

        // Get all the punchItemList where revisionDrawing equals to UPDATED_REVISION_DRAWING
        defaultPunchItemShouldNotBeFound("revisionDrawing.equals=" + UPDATED_REVISION_DRAWING);
    }

    @Test
    @Transactional
    void getAllPunchItemsByRevisionDrawingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where revisionDrawing not equals to DEFAULT_REVISION_DRAWING
        defaultPunchItemShouldNotBeFound("revisionDrawing.notEquals=" + DEFAULT_REVISION_DRAWING);

        // Get all the punchItemList where revisionDrawing not equals to UPDATED_REVISION_DRAWING
        defaultPunchItemShouldBeFound("revisionDrawing.notEquals=" + UPDATED_REVISION_DRAWING);
    }

    @Test
    @Transactional
    void getAllPunchItemsByRevisionDrawingIsInShouldWork() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where revisionDrawing in DEFAULT_REVISION_DRAWING or UPDATED_REVISION_DRAWING
        defaultPunchItemShouldBeFound("revisionDrawing.in=" + DEFAULT_REVISION_DRAWING + "," + UPDATED_REVISION_DRAWING);

        // Get all the punchItemList where revisionDrawing equals to UPDATED_REVISION_DRAWING
        defaultPunchItemShouldNotBeFound("revisionDrawing.in=" + UPDATED_REVISION_DRAWING);
    }

    @Test
    @Transactional
    void getAllPunchItemsByRevisionDrawingIsNullOrNotNull() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where revisionDrawing is not null
        defaultPunchItemShouldBeFound("revisionDrawing.specified=true");

        // Get all the punchItemList where revisionDrawing is null
        defaultPunchItemShouldNotBeFound("revisionDrawing.specified=false");
    }

    @Test
    @Transactional
    void getAllPunchItemsByRevisionDrawingContainsSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where revisionDrawing contains DEFAULT_REVISION_DRAWING
        defaultPunchItemShouldBeFound("revisionDrawing.contains=" + DEFAULT_REVISION_DRAWING);

        // Get all the punchItemList where revisionDrawing contains UPDATED_REVISION_DRAWING
        defaultPunchItemShouldNotBeFound("revisionDrawing.contains=" + UPDATED_REVISION_DRAWING);
    }

    @Test
    @Transactional
    void getAllPunchItemsByRevisionDrawingNotContainsSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where revisionDrawing does not contain DEFAULT_REVISION_DRAWING
        defaultPunchItemShouldNotBeFound("revisionDrawing.doesNotContain=" + DEFAULT_REVISION_DRAWING);

        // Get all the punchItemList where revisionDrawing does not contain UPDATED_REVISION_DRAWING
        defaultPunchItemShouldBeFound("revisionDrawing.doesNotContain=" + UPDATED_REVISION_DRAWING);
    }

    @Test
    @Transactional
    void getAllPunchItemsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where status equals to DEFAULT_STATUS
        defaultPunchItemShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the punchItemList where status equals to UPDATED_STATUS
        defaultPunchItemShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPunchItemsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where status not equals to DEFAULT_STATUS
        defaultPunchItemShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the punchItemList where status not equals to UPDATED_STATUS
        defaultPunchItemShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPunchItemsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultPunchItemShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the punchItemList where status equals to UPDATED_STATUS
        defaultPunchItemShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPunchItemsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where status is not null
        defaultPunchItemShouldBeFound("status.specified=true");

        // Get all the punchItemList where status is null
        defaultPunchItemShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllPunchItemsByClosedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where closedDate equals to DEFAULT_CLOSED_DATE
        defaultPunchItemShouldBeFound("closedDate.equals=" + DEFAULT_CLOSED_DATE);

        // Get all the punchItemList where closedDate equals to UPDATED_CLOSED_DATE
        defaultPunchItemShouldNotBeFound("closedDate.equals=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    void getAllPunchItemsByClosedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where closedDate not equals to DEFAULT_CLOSED_DATE
        defaultPunchItemShouldNotBeFound("closedDate.notEquals=" + DEFAULT_CLOSED_DATE);

        // Get all the punchItemList where closedDate not equals to UPDATED_CLOSED_DATE
        defaultPunchItemShouldBeFound("closedDate.notEquals=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    void getAllPunchItemsByClosedDateIsInShouldWork() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where closedDate in DEFAULT_CLOSED_DATE or UPDATED_CLOSED_DATE
        defaultPunchItemShouldBeFound("closedDate.in=" + DEFAULT_CLOSED_DATE + "," + UPDATED_CLOSED_DATE);

        // Get all the punchItemList where closedDate equals to UPDATED_CLOSED_DATE
        defaultPunchItemShouldNotBeFound("closedDate.in=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    void getAllPunchItemsByClosedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        // Get all the punchItemList where closedDate is not null
        defaultPunchItemShouldBeFound("closedDate.specified=true");

        // Get all the punchItemList where closedDate is null
        defaultPunchItemShouldNotBeFound("closedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPunchItemsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);
        TypePunch type;
        if (TestUtil.findAll(em, TypePunch.class).isEmpty()) {
            type = TypePunchResourceIT.createEntity(em);
            em.persist(type);
            em.flush();
        } else {
            type = TestUtil.findAll(em, TypePunch.class).get(0);
        }
        em.persist(type);
        em.flush();
        punchItem.setType(type);
        punchItemRepository.saveAndFlush(punchItem);
        Long typeId = type.getId();

        // Get all the punchItemList where type equals to typeId
        defaultPunchItemShouldBeFound("typeId.equals=" + typeId);

        // Get all the punchItemList where type equals to (typeId + 1)
        defaultPunchItemShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    @Test
    @Transactional
    void getAllPunchItemsByLineIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);
        Line line;
        if (TestUtil.findAll(em, Line.class).isEmpty()) {
            line = LineResourceIT.createEntity(em);
            em.persist(line);
            em.flush();
        } else {
            line = TestUtil.findAll(em, Line.class).get(0);
        }
        em.persist(line);
        em.flush();
        punchItem.setLine(line);
        punchItemRepository.saveAndFlush(punchItem);
        Long lineId = line.getId();

        // Get all the punchItemList where line equals to lineId
        defaultPunchItemShouldBeFound("lineId.equals=" + lineId);

        // Get all the punchItemList where line equals to (lineId + 1)
        defaultPunchItemShouldNotBeFound("lineId.equals=" + (lineId + 1));
    }

    @Test
    @Transactional
    void getAllPunchItemsByPunchListIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);
        PunchList punchList;
        if (TestUtil.findAll(em, PunchList.class).isEmpty()) {
            punchList = PunchListResourceIT.createEntity(em);
            em.persist(punchList);
            em.flush();
        } else {
            punchList = TestUtil.findAll(em, PunchList.class).get(0);
        }
        em.persist(punchList);
        em.flush();
        punchItem.setPunchList(punchList);
        punchItemRepository.saveAndFlush(punchItem);
        Long punchListId = punchList.getId();

        // Get all the punchItemList where punchList equals to punchListId
        defaultPunchItemShouldBeFound("punchListId.equals=" + punchListId);

        // Get all the punchItemList where punchList equals to (punchListId + 1)
        defaultPunchItemShouldNotBeFound("punchListId.equals=" + (punchListId + 1));
    }

    @Test
    @Transactional
    void getAllPunchItemsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);
        PriorityPunch priority;
        if (TestUtil.findAll(em, PriorityPunch.class).isEmpty()) {
            priority = PriorityPunchResourceIT.createEntity(em);
            em.persist(priority);
            em.flush();
        } else {
            priority = TestUtil.findAll(em, PriorityPunch.class).get(0);
        }
        em.persist(priority);
        em.flush();
        punchItem.setPriority(priority);
        punchItemRepository.saveAndFlush(punchItem);
        Long priorityId = priority.getId();

        // Get all the punchItemList where priority equals to priorityId
        defaultPunchItemShouldBeFound("priorityId.equals=" + priorityId);

        // Get all the punchItemList where priority equals to (priorityId + 1)
        defaultPunchItemShouldNotBeFound("priorityId.equals=" + (priorityId + 1));
    }

    @Test
    @Transactional
    void getAllPunchItemsByExecutorIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);
        Company executor;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            executor = CompanyResourceIT.createEntity(em);
            em.persist(executor);
            em.flush();
        } else {
            executor = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(executor);
        em.flush();
        punchItem.setExecutor(executor);
        punchItemRepository.saveAndFlush(punchItem);
        Long executorId = executor.getId();

        // Get all the punchItemList where executor equals to executorId
        defaultPunchItemShouldBeFound("executorId.equals=" + executorId);

        // Get all the punchItemList where executor equals to (executorId + 1)
        defaultPunchItemShouldNotBeFound("executorId.equals=" + (executorId + 1));
    }

    @Test
    @Transactional
    void getAllPunchItemsByAuthorIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);
        User author;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            author = UserResourceIT.createEntity(em);
            em.persist(author);
            em.flush();
        } else {
            author = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(author);
        em.flush();
        punchItem.setAuthor(author);
        punchItemRepository.saveAndFlush(punchItem);
        Long authorId = author.getId();

        // Get all the punchItemList where author equals to authorId
        defaultPunchItemShouldBeFound("authorId.equals=" + authorId);

        // Get all the punchItemList where author equals to (authorId + 1)
        defaultPunchItemShouldNotBeFound("authorId.equals=" + (authorId + 1));
    }

    @Test
    @Transactional
    void getAllPunchItemsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);
        CommentPunch comments;
        if (TestUtil.findAll(em, CommentPunch.class).isEmpty()) {
            comments = CommentPunchResourceIT.createEntity(em);
            em.persist(comments);
            em.flush();
        } else {
            comments = TestUtil.findAll(em, CommentPunch.class).get(0);
        }
        em.persist(comments);
        em.flush();
        punchItem.addComments(comments);
        punchItemRepository.saveAndFlush(punchItem);
        Long commentsId = comments.getId();

        // Get all the punchItemList where comments equals to commentsId
        defaultPunchItemShouldBeFound("commentsId.equals=" + commentsId);

        // Get all the punchItemList where comments equals to (commentsId + 1)
        defaultPunchItemShouldNotBeFound("commentsId.equals=" + (commentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPunchItemShouldBeFound(String filter) throws Exception {
        restPunchItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(punchItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].revisionDrawing").value(hasItem(DEFAULT_REVISION_DRAWING)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())));

        // Check, that the count call also returns 1
        restPunchItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPunchItemShouldNotBeFound(String filter) throws Exception {
        restPunchItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPunchItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPunchItem() throws Exception {
        // Get the punchItem
        restPunchItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPunchItem() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        int databaseSizeBeforeUpdate = punchItemRepository.findAll().size();

        // Update the punchItem
        PunchItem updatedPunchItem = punchItemRepository.findById(punchItem.getId()).get();
        // Disconnect from session so that the updates on updatedPunchItem are not directly saved in db
        em.detach(updatedPunchItem);
        updatedPunchItem
            .number(UPDATED_NUMBER)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .revisionDrawing(UPDATED_REVISION_DRAWING)
            .status(UPDATED_STATUS)
            .closedDate(UPDATED_CLOSED_DATE);
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(updatedPunchItem);

        restPunchItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, punchItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(punchItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the PunchItem in the database
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeUpdate);
        PunchItem testPunchItem = punchItemList.get(punchItemList.size() - 1);
        assertThat(testPunchItem.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPunchItem.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testPunchItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPunchItem.getRevisionDrawing()).isEqualTo(UPDATED_REVISION_DRAWING);
        assertThat(testPunchItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPunchItem.getClosedDate()).isEqualTo(UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPunchItem() throws Exception {
        int databaseSizeBeforeUpdate = punchItemRepository.findAll().size();
        punchItem.setId(count.incrementAndGet());

        // Create the PunchItem
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(punchItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPunchItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, punchItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(punchItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PunchItem in the database
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPunchItem() throws Exception {
        int databaseSizeBeforeUpdate = punchItemRepository.findAll().size();
        punchItem.setId(count.incrementAndGet());

        // Create the PunchItem
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(punchItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPunchItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(punchItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PunchItem in the database
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPunchItem() throws Exception {
        int databaseSizeBeforeUpdate = punchItemRepository.findAll().size();
        punchItem.setId(count.incrementAndGet());

        // Create the PunchItem
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(punchItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPunchItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(punchItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PunchItem in the database
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePunchItemWithPatch() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        int databaseSizeBeforeUpdate = punchItemRepository.findAll().size();

        // Update the punchItem using partial update
        PunchItem partialUpdatedPunchItem = new PunchItem();
        partialUpdatedPunchItem.setId(punchItem.getId());

        partialUpdatedPunchItem
            .number(UPDATED_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .revisionDrawing(UPDATED_REVISION_DRAWING)
            .status(UPDATED_STATUS);

        restPunchItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPunchItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPunchItem))
            )
            .andExpect(status().isOk());

        // Validate the PunchItem in the database
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeUpdate);
        PunchItem testPunchItem = punchItemList.get(punchItemList.size() - 1);
        assertThat(testPunchItem.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPunchItem.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testPunchItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPunchItem.getRevisionDrawing()).isEqualTo(UPDATED_REVISION_DRAWING);
        assertThat(testPunchItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPunchItem.getClosedDate()).isEqualTo(DEFAULT_CLOSED_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePunchItemWithPatch() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        int databaseSizeBeforeUpdate = punchItemRepository.findAll().size();

        // Update the punchItem using partial update
        PunchItem partialUpdatedPunchItem = new PunchItem();
        partialUpdatedPunchItem.setId(punchItem.getId());

        partialUpdatedPunchItem
            .number(UPDATED_NUMBER)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .revisionDrawing(UPDATED_REVISION_DRAWING)
            .status(UPDATED_STATUS)
            .closedDate(UPDATED_CLOSED_DATE);

        restPunchItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPunchItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPunchItem))
            )
            .andExpect(status().isOk());

        // Validate the PunchItem in the database
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeUpdate);
        PunchItem testPunchItem = punchItemList.get(punchItemList.size() - 1);
        assertThat(testPunchItem.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPunchItem.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testPunchItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPunchItem.getRevisionDrawing()).isEqualTo(UPDATED_REVISION_DRAWING);
        assertThat(testPunchItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPunchItem.getClosedDate()).isEqualTo(UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPunchItem() throws Exception {
        int databaseSizeBeforeUpdate = punchItemRepository.findAll().size();
        punchItem.setId(count.incrementAndGet());

        // Create the PunchItem
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(punchItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPunchItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, punchItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(punchItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PunchItem in the database
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPunchItem() throws Exception {
        int databaseSizeBeforeUpdate = punchItemRepository.findAll().size();
        punchItem.setId(count.incrementAndGet());

        // Create the PunchItem
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(punchItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPunchItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(punchItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PunchItem in the database
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPunchItem() throws Exception {
        int databaseSizeBeforeUpdate = punchItemRepository.findAll().size();
        punchItem.setId(count.incrementAndGet());

        // Create the PunchItem
        PunchItemDTO punchItemDTO = punchItemMapper.toDto(punchItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPunchItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(punchItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PunchItem in the database
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePunchItem() throws Exception {
        // Initialize the database
        punchItemRepository.saveAndFlush(punchItem);

        int databaseSizeBeforeDelete = punchItemRepository.findAll().size();

        // Delete the punchItem
        restPunchItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, punchItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PunchItem> punchItemList = punchItemRepository.findAll();
        assertThat(punchItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
