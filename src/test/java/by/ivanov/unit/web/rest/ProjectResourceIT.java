package by.ivanov.unit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import by.ivanov.unit.IntegrationTest;
import by.ivanov.unit.domain.Project;
import by.ivanov.unit.repository.ProjectRepository;
import by.ivanov.unit.security.AuthoritiesConstants;
import by.ivanov.unit.service.ProjectService;
import by.ivanov.unit.service.dto.ProjectDTO;
import by.ivanov.unit.service.mapper.ProjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProjectResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProjectResourceIT {

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
	private static final String UPDATED_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
	private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

	private static final String ENTITY_API_URL = "/api/projects";
	private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

	private static final Random random = new Random();
	private static final AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

	@Autowired
	private ProjectRepository projectRepository;

	@Mock
	private ProjectRepository projectRepositoryMock;

	@Autowired
	private ProjectMapper projectMapper;

	@Mock
	private ProjectService projectServiceMock;

	@Autowired
	private EntityManager em;

	@Autowired
	private MockMvc restProjectMockMvc;

	private Project project;

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Project createEntity(EntityManager em) {
		return new Project().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
	}

	/**
	 * Create an updated entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Project createUpdatedEntity(EntityManager em) {
		return new Project().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
	}

	@BeforeEach
	public void initTest() {
		project = createEntity(em);
	}

	@Test
	@Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void createProject() throws Exception {
		int databaseSizeBeforeCreate = projectRepository.findAll().size();
		// Create the Project
		ProjectDTO projectDTO = projectMapper.toDto(project);
		restProjectMockMvc
			.perform(
				post(ENTITY_API_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(TestUtil.convertObjectToJsonBytes(projectDTO))
			)
			.andExpect(status().isCreated());

		// Validate the Project in the database
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
		Project testProject = projectList.get(projectList.size() - 1);
		assertThat(testProject.getName()).isEqualTo(DEFAULT_NAME);
		assertThat(testProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
	}

	@Test
	@Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void createProjectWithExistingId() throws Exception {
		// Create the Project with an existing ID
		project.setId(1L);
		ProjectDTO projectDTO = projectMapper.toDto(project);

		int databaseSizeBeforeCreate = projectRepository.findAll().size();

		// An entity with an existing ID cannot be created, so this API call must fail
		restProjectMockMvc
			.perform(
				post(ENTITY_API_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(TestUtil.convertObjectToJsonBytes(projectDTO))
			)
			.andExpect(status().isBadRequest());

		// Validate the Project in the database
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	void checkNameIsRequired() throws Exception {
		int databaseSizeBeforeTest = projectRepository.findAll().size();
		// set the field null
		project.setName(null);

		// Create the Project, which fails.
		ProjectDTO projectDTO = projectMapper.toDto(project);

		restProjectMockMvc
			.perform(
				post(ENTITY_API_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(TestUtil.convertObjectToJsonBytes(projectDTO))
			)
			.andExpect(status().isBadRequest());

		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	void getAllProjects() throws Exception {
		// Initialize the database
		projectRepository.saveAndFlush(project);

		// Get all the projectList
		restProjectMockMvc
			.perform(get(ENTITY_API_URL + "?sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
			.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
			.andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
	}

	@SuppressWarnings({ "unchecked" })
	void getAllProjectsWithEagerRelationshipsIsEnabled() throws Exception {
		when(projectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

		restProjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

		verify(projectServiceMock, times(1)).findAllWithEagerRelationships(any());
	}

	@SuppressWarnings({ "unchecked" })
	void getAllProjectsWithEagerRelationshipsIsNotEnabled() throws Exception {
		when(projectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

		restProjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

		verify(projectServiceMock, times(1)).findAllWithEagerRelationships(any());
	}

	@Test
	@Transactional
	void getProject() throws Exception {
		// Initialize the database
		projectRepository.saveAndFlush(project);

		// Get the project
		restProjectMockMvc
			.perform(get(ENTITY_API_URL_ID, project.getId()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.id").value(project.getId().intValue()))
			.andExpect(jsonPath("$.name").value(DEFAULT_NAME))
			.andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
	}

	@Test
	@Transactional
	void getNonExistingProject() throws Exception {
		// Get the project
		restProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void putNewProject() throws Exception {
		// Initialize the database
		projectRepository.saveAndFlush(project);

		int databaseSizeBeforeUpdate = projectRepository.findAll().size();

		// Update the project
		Project updatedProject = projectRepository.findById(project.getId()).get();
		// Disconnect from session so that the updates on updatedProject are not directly saved in db
		em.detach(updatedProject);
		updatedProject.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
		ProjectDTO projectDTO = projectMapper.toDto(updatedProject);

		restProjectMockMvc
			.perform(
				put(ENTITY_API_URL_ID, projectDTO.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(TestUtil.convertObjectToJsonBytes(projectDTO))
			)
			.andExpect(status().isOk());

		// Validate the Project in the database
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
		Project testProject = projectList.get(projectList.size() - 1);
		assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
		assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
	}

	@Test
	@Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void putNonExistingProject() throws Exception {
		int databaseSizeBeforeUpdate = projectRepository.findAll().size();
		project.setId(count.incrementAndGet());

		// Create the Project
		ProjectDTO projectDTO = projectMapper.toDto(project);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restProjectMockMvc
			.perform(
				put(ENTITY_API_URL_ID, projectDTO.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(TestUtil.convertObjectToJsonBytes(projectDTO))
			)
			.andExpect(status().isBadRequest());

		// Validate the Project in the database
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void putWithIdMismatchProject() throws Exception {
		int databaseSizeBeforeUpdate = projectRepository.findAll().size();
		project.setId(count.incrementAndGet());

		// Create the Project
		ProjectDTO projectDTO = projectMapper.toDto(project);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restProjectMockMvc
			.perform(
				put(ENTITY_API_URL_ID, count.incrementAndGet())
					.contentType(MediaType.APPLICATION_JSON)
					.content(TestUtil.convertObjectToJsonBytes(projectDTO))
			)
			.andExpect(status().isBadRequest());

		// Validate the Project in the database
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void putWithMissingIdPathParamProject() throws Exception {
		int databaseSizeBeforeUpdate = projectRepository.findAll().size();
		project.setId(count.incrementAndGet());

		// Create the Project
		ProjectDTO projectDTO = projectMapper.toDto(project);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restProjectMockMvc
			.perform(
				put(ENTITY_API_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(TestUtil.convertObjectToJsonBytes(projectDTO))
			)
			.andExpect(status().isMethodNotAllowed());

		// Validate the Project in the database
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void partialUpdateProjectWithPatch() throws Exception {
		// Initialize the database
		projectRepository.saveAndFlush(project);

		int databaseSizeBeforeUpdate = projectRepository.findAll().size();

		// Update the project using partial update
		Project partialUpdatedProject = new Project();
		partialUpdatedProject.setId(project.getId());

		partialUpdatedProject.name(UPDATED_NAME);

		restProjectMockMvc
			.perform(
				patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
					.contentType("application/merge-patch+json")
					.content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
			)
			.andExpect(status().isOk());

		// Validate the Project in the database
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
		Project testProject = projectList.get(projectList.size() - 1);
		assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
		assertThat(testProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
	}

	@Test
	@Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void fullUpdateProjectWithPatch() throws Exception {
		// Initialize the database
		projectRepository.saveAndFlush(project);

		int databaseSizeBeforeUpdate = projectRepository.findAll().size();

		// Update the project using partial update
		Project partialUpdatedProject = new Project();
		partialUpdatedProject.setId(project.getId());

		partialUpdatedProject.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

		restProjectMockMvc
			.perform(
				patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
					.contentType("application/merge-patch+json")
					.content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
			)
			.andExpect(status().isOk());

		// Validate the Project in the database
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
		Project testProject = projectList.get(projectList.size() - 1);
		assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
		assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
	}

	@Test
	@Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void patchNonExistingProject() throws Exception {
		int databaseSizeBeforeUpdate = projectRepository.findAll().size();
		project.setId(count.incrementAndGet());

		// Create the Project
		ProjectDTO projectDTO = projectMapper.toDto(project);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restProjectMockMvc
			.perform(
				patch(ENTITY_API_URL_ID, projectDTO.getId())
					.contentType("application/merge-patch+json")
					.content(TestUtil.convertObjectToJsonBytes(projectDTO))
			)
			.andExpect(status().isBadRequest());

		// Validate the Project in the database
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void patchWithIdMismatchProject() throws Exception {
		int databaseSizeBeforeUpdate = projectRepository.findAll().size();
		project.setId(count.incrementAndGet());

		// Create the Project
		ProjectDTO projectDTO = projectMapper.toDto(project);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restProjectMockMvc
			.perform(
				patch(ENTITY_API_URL_ID, count.incrementAndGet())
					.contentType("application/merge-patch+json")
					.content(TestUtil.convertObjectToJsonBytes(projectDTO))
			)
			.andExpect(status().isBadRequest());

		// Validate the Project in the database
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void patchWithMissingIdPathParamProject() throws Exception {
		int databaseSizeBeforeUpdate = projectRepository.findAll().size();
		project.setId(count.incrementAndGet());

		// Create the Project
		ProjectDTO projectDTO = projectMapper.toDto(project);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restProjectMockMvc
			.perform(
				patch(ENTITY_API_URL)
					.contentType("application/merge-patch+json")
					.content(TestUtil.convertObjectToJsonBytes(projectDTO))
			)
			.andExpect(status().isMethodNotAllowed());

		// Validate the Project in the database
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
	void deleteProject() throws Exception {
		// Initialize the database
		projectRepository.saveAndFlush(project);

		int databaseSizeBeforeDelete = projectRepository.findAll().size();

		// Delete the project
		restProjectMockMvc
			.perform(delete(ENTITY_API_URL_ID, project.getId()).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());

		// Validate the database contains one less item
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
	}
}
