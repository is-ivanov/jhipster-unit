package by.ivanov.unit.web.rest;

import by.ivanov.unit.repository.PunchListRepository;
import by.ivanov.unit.service.PunchListQueryService;
import by.ivanov.unit.service.PunchListService;
import by.ivanov.unit.service.criteria.PunchListCriteria;
import by.ivanov.unit.service.dto.PunchListDTO;
import by.ivanov.unit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link by.ivanov.unit.domain.PunchList}.
 */
@RestController
@RequestMapping("/api")
public class PunchListResource {

    private final Logger log = LoggerFactory.getLogger(PunchListResource.class);

    private static final String ENTITY_NAME = "punchList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PunchListService punchListService;

    private final PunchListRepository punchListRepository;

    private final PunchListQueryService punchListQueryService;

    public PunchListResource(
        PunchListService punchListService,
        PunchListRepository punchListRepository,
        PunchListQueryService punchListQueryService
    ) {
        this.punchListService = punchListService;
        this.punchListRepository = punchListRepository;
        this.punchListQueryService = punchListQueryService;
    }

    /**
     * {@code POST  /punch-lists} : Create a new punchList.
     *
     * @param punchListDTO the punchListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new punchListDTO, or with status {@code 400 (Bad Request)} if the punchList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/punch-lists")
    public ResponseEntity<PunchListDTO> createPunchList(@Valid @RequestBody PunchListDTO punchListDTO) throws URISyntaxException {
        log.debug("REST request to save PunchList : {}", punchListDTO);
        if (punchListDTO.getId() != null) {
            throw new BadRequestAlertException("A new punchList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PunchListDTO result = punchListService.save(punchListDTO);
        return ResponseEntity
            .created(new URI("/api/punch-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /punch-lists/:id} : Updates an existing punchList.
     *
     * @param id the id of the punchListDTO to save.
     * @param punchListDTO the punchListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated punchListDTO,
     * or with status {@code 400 (Bad Request)} if the punchListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the punchListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/punch-lists/{id}")
    public ResponseEntity<PunchListDTO> updatePunchList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PunchListDTO punchListDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PunchList : {}, {}", id, punchListDTO);
        if (punchListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, punchListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!punchListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PunchListDTO result = punchListService.save(punchListDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, punchListDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /punch-lists/:id} : Partial updates given fields of an existing punchList, field will ignore if it is null
     *
     * @param id the id of the punchListDTO to save.
     * @param punchListDTO the punchListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated punchListDTO,
     * or with status {@code 400 (Bad Request)} if the punchListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the punchListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the punchListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/punch-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PunchListDTO> partialUpdatePunchList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PunchListDTO punchListDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PunchList partially : {}, {}", id, punchListDTO);
        if (punchListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, punchListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!punchListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PunchListDTO> result = punchListService.partialUpdate(punchListDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, punchListDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /punch-lists} : get all the punchLists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of punchLists in body.
     */
    @GetMapping("/punch-lists")
    public ResponseEntity<List<PunchListDTO>> getAllPunchLists(
        PunchListCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PunchLists by criteria: {}", criteria);
        Page<PunchListDTO> page = punchListQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /punch-lists/count} : count all the punchLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/punch-lists/count")
    public ResponseEntity<Long> countPunchLists(PunchListCriteria criteria) {
        log.debug("REST request to count PunchLists by criteria: {}", criteria);
        return ResponseEntity.ok().body(punchListQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /punch-lists/:id} : get the "id" punchList.
     *
     * @param id the id of the punchListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the punchListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/punch-lists/{id}")
    public ResponseEntity<PunchListDTO> getPunchList(@PathVariable Long id) {
        log.debug("REST request to get PunchList : {}", id);
        Optional<PunchListDTO> punchListDTO = punchListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(punchListDTO);
    }

    /**
     * {@code DELETE  /punch-lists/:id} : delete the "id" punchList.
     *
     * @param id the id of the punchListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/punch-lists/{id}")
    public ResponseEntity<Void> deletePunchList(@PathVariable Long id) {
        log.debug("REST request to delete PunchList : {}", id);
        punchListService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
