package by.ivanov.unit.web.rest;

import by.ivanov.unit.repository.LineRepository;
import by.ivanov.unit.security.AuthoritiesConstants;
import by.ivanov.unit.service.LineQueryService;
import by.ivanov.unit.service.LineService;
import by.ivanov.unit.service.criteria.LineCriteria;
import by.ivanov.unit.service.dto.LineDTO;
import by.ivanov.unit.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link by.ivanov.unit.domain.Line} and their revisions.
 */
@RestController
@RequestMapping("/api")
public class LineResource {

	private final Logger log = LoggerFactory.getLogger(LineResource.class);

	private static final String ENTITY_NAME = "line";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final LineService lineService;

	private final LineRepository lineRepository;

	private final LineQueryService lineQueryService;

	public LineResource(LineService lineService, LineRepository lineRepository, LineQueryService lineQueryService) {
		this.lineService = lineService;
		this.lineRepository = lineRepository;
		this.lineQueryService = lineQueryService;
	}

	/**
	 * {@code POST  /lines} : Create a new line.
	 *
	 * @param lineDTO the lineDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lineDTO, or with status {@code 400 (Bad Request)} if the line has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/lines")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
	public ResponseEntity<LineDTO> createLine(@Valid @RequestBody LineDTO lineDTO) throws URISyntaxException {
		log.debug("REST request to create Line : {}", lineDTO);
		if (lineDTO.getId() != null) {
			throw new BadRequestAlertException("A new line cannot already have an ID", ENTITY_NAME, "idexists");
		}
		LineDTO result = lineService.save(lineDTO);
		return ResponseEntity
			.created(new URI("/api/lines/" + result.getId()))
			.headers(
				HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString())
			)
			.body(result);
	}

	/**
	 * {@code PUT  /lines/:id} : Updates an existing line.
	 *
	 * @param id the id of the lineDTO to save.
	 * @param lineDTO the lineDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineDTO,
	 * or with status {@code 400 (Bad Request)} if the lineDTO is not valid,
	 * or with status {@code 500 (Internal Server Error)} if the lineDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/lines/{id}")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
	public ResponseEntity<LineDTO> updateLine(
		@PathVariable(value = "id", required = false) final Long id,
		@Valid @RequestBody LineDTO lineDTO
	) throws URISyntaxException {
		log.debug("REST request to update Line : {}, {}", id, lineDTO);
		if (lineDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, lineDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!lineRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

        LineDTO result = lineService.update(lineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lineDTO.getId().toString()))
            .body(result);
    }

	/**
	 * {@code PATCH  /lines/:id} : Partial updates given fields of an existing line, field will ignore if it is null
	 *
	 * @param id the id of the lineDTO to save.
	 * @param lineDTO the lineDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineDTO,
	 * or with status {@code 400 (Bad Request)} if the lineDTO is not valid,
	 * or with status {@code 404 (Not Found)} if the lineDTO is not found,
	 * or with status {@code 500 (Internal Server Error)} if the lineDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/lines/{id}", consumes = { "application/json", "application/merge-patch+json" })
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
	public ResponseEntity<LineDTO> partialUpdateLine(
		@PathVariable(value = "id", required = false) final Long id,
		@NotNull @RequestBody LineDTO lineDTO
	) throws URISyntaxException {
		log.debug("REST request to partial update Line partially : {}, {}", id, lineDTO);
		if (lineDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, lineDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!lineRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<LineDTO> result = lineService.partialUpdate(lineDTO);

		return ResponseUtil.wrapOrNotFound(
			result,
			HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				lineDTO.getId().toString())
		);
	}

	/**
	 * {@code GET  /lines} : get all the lines.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lines in body.
	 */
	@GetMapping("/lines")
	public ResponseEntity<List<LineDTO>> getAllLines(
		LineCriteria criteria,
		@org.springdoc.api.annotations.ParameterObject Pageable pageable
	) {
		log.debug("REST request to get Lines by criteria: {}", criteria);
		Page<LineDTO> page = lineQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(),
			page
		);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /lines/count} : count all the lines.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
	 */
	@GetMapping("/lines/count")
	public ResponseEntity<Long> countLines(LineCriteria criteria) {
		log.debug("REST request to count Lines by criteria: {}", criteria);
		return ResponseEntity.ok().body(lineQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /lines/:id} : get the "id" line.
	 *
	 * @param id the id of the lineDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lineDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/lines/{id}")
	public ResponseEntity<LineDTO> getLine(@PathVariable Long id) {
		log.debug("REST request to get Line : {}", id);
		Optional<LineDTO> lineDTO = lineService.findOne(id);
		return ResponseUtil.wrapOrNotFound(lineDTO);
	}

	/**
	 * {@code DELETE  /lines/:id} : delete the "id" line.
	 *
	 * @param id the id of the lineDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/lines/{id}")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		log.debug("REST request to delete Line : {}", id);
		lineService.delete(id);
		return ResponseEntity
			.noContent()
			.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
			.build();
	}

	/**
	 * {@code GET  /lines/revisions} : get all Revisions for the lines.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of revisions in body.
	 */
	@GetMapping("/lines/revisions")
	public List<String> getAllRevisions() {
		log.debug("REST request to get revisions of all lines");
		return lineService.findAllRevisions();
	}

	/**
	 * {@code GET  /projects/:projectId/revisions} : get all Revisions for the project with "id".
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of revisions in body.
	 */
	@GetMapping("/projects/{projectId}/revisions")
	public List<String> getRevisionsByProject(@PathVariable Long projectId) {
		log.debug("REST request to get revisions of lines from project ID: {}", projectId);
		return lineService.findAllRevisionsWithProject(projectId);
	}

	/**
	 * {@code GET  /blocks/:blockId/revisions} : get all Revisions for the block with "id".
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of revisions in body.
	 */
	@GetMapping("/blocks/{blockId}/revisions")
	public List<String> getRevisionsByBlock(@PathVariable Long blockId) {
		log.debug("REST request to get revisions of lines from block ID: {}", blockId);
		return lineService.findAllRevisionsWithBlock(blockId);
	}
}
