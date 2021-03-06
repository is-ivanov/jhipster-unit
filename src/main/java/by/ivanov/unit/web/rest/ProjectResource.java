package by.ivanov.unit.web.rest;

import by.ivanov.unit.repository.ProjectRepository;
import by.ivanov.unit.security.AuthoritiesConstants;
import by.ivanov.unit.service.ProjectService;
import by.ivanov.unit.service.dto.ProjectDTO;
import by.ivanov.unit.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link by.ivanov.unit.domain.Project}.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

	private static final String ENTITY_NAME = "project";
	private final Logger log = LoggerFactory.getLogger(ProjectResource.class);
	private final ProjectService projectService;
	private final ProjectRepository projectRepository;

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	public ProjectResource(ProjectService projectService, ProjectRepository projectRepository) {
		this.projectService = projectService;
		this.projectRepository = projectRepository;
	}

	/**
	 * {@code POST  /projects} : Create a new project.
	 *
	 * @param projectDTO the projectDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projectDTO, or with status {@code 400 (Bad Request)} if the project has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/projects")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO)
		throws URISyntaxException {
		log.debug("REST request to save Project : {}", projectDTO);
		if (projectDTO.getId() != null) {
			throw new BadRequestAlertException("A new project cannot already have an ID", ENTITY_NAME, "idexists");
		}
		ProjectDTO result = projectService.save(projectDTO);
		return ResponseEntity
			.created(new URI("/api/projects/" + result.getId()))
			.headers(
				HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString())
			)
			.body(result);
	}

	/**
	 * {@code PUT  /projects/:id} : Updates an existing project.
	 *
	 * @param id         the id of the projectDTO to save.
	 * @param projectDTO the projectDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectDTO,
	 * or with status {@code 400 (Bad Request)} if the projectDTO is not valid,
	 * or with status {@code 500 (Internal Server Error)} if the projectDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/projects/{id}")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<ProjectDTO> updateProject(
		@PathVariable(value = "id", required = false) final Long id,
		@Valid @RequestBody ProjectDTO projectDTO
	) throws URISyntaxException {
		log.debug("REST request to update Project : {}, {}", id, projectDTO);
		if (projectDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, projectDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!projectRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

        ProjectDTO result = projectService.update(projectDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectDTO.getId().toString()))
            .body(result);
    }

	/**
	 * {@code PATCH  /projects/:id} : Partial updates given fields of an existing project, field will ignore if it is null
	 *
	 * @param id         the id of the projectDTO to save.
	 * @param projectDTO the projectDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectDTO,
	 * or with status {@code 400 (Bad Request)} if the projectDTO is not valid,
	 * or with status {@code 404 (Not Found)} if the projectDTO is not found,
	 * or with status {@code 500 (Internal Server Error)} if the projectDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/projects/{id}", consumes = { "application/json", "application/merge-patch+json" })
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<ProjectDTO> partialUpdateProject(
		@PathVariable(value = "id", required = false) final Long id,
		@NotNull @RequestBody ProjectDTO projectDTO
	) throws URISyntaxException {
		log.debug("REST request to partial update Project partially : {}, {}", id, projectDTO);
		if (projectDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, projectDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!projectRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<ProjectDTO> result = projectService.partialUpdate(projectDTO);

		return ResponseUtil.wrapOrNotFound(
			result,
			HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectDTO.getId().toString())
		);
	}

	/**
	 * {@code GET  /projects} : get all the projects.
	 *
	 * @param pageable  the pagination information.
	 * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projects in body.
	 */
	@GetMapping("/projects")
	public ResponseEntity<List<ProjectDTO>> getAllProjects(
		@ParameterObject Pageable pageable,
		@RequestParam(required = false, defaultValue = "true") boolean eagerload
	) {
		log.debug("REST request to get a page of Projects");
		Page<ProjectDTO> page;
		if (eagerload) {
			page = projectService.findAllWithEagerRelationships(pageable);
		} else {
			page = projectService.findAll(pageable);
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /projects/:id} : get the "id" project.
	 *
	 * @param id the id of the projectDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projectDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/projects/{id}")
	public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
		log.debug("REST request to get Project : {}", id);
		Optional<ProjectDTO> projectDTO = projectService.findOne(id);
		return ResponseUtil.wrapOrNotFound(projectDTO);
	}

	/**
	 * {@code DELETE  /projects/:id} : delete the "id" project.
	 *
	 * @param id the id of the projectDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/projects/{id}")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
		log.debug("REST request to delete Project : {}", id);
		projectService.delete(id);
		return ResponseEntity
			.noContent()
			.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
			.build();
	}
}
