package by.ivanov.unit.web.rest;

import by.ivanov.unit.repository.PunchItemRepository;
import by.ivanov.unit.service.PunchItemQueryService;
import by.ivanov.unit.service.PunchItemService;
import by.ivanov.unit.service.criteria.PunchItemCriteria;
import by.ivanov.unit.service.dto.PunchItemDTO;
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
 * REST controller for managing {@link by.ivanov.unit.domain.PunchItem}.
 */
@RestController
@RequestMapping("/api")
public class PunchItemResource {

    private final Logger log = LoggerFactory.getLogger(PunchItemResource.class);

    private static final String ENTITY_NAME = "punchItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PunchItemService punchItemService;

    private final PunchItemRepository punchItemRepository;

    private final PunchItemQueryService punchItemQueryService;

    public PunchItemResource(
        PunchItemService punchItemService,
        PunchItemRepository punchItemRepository,
        PunchItemQueryService punchItemQueryService
    ) {
        this.punchItemService = punchItemService;
        this.punchItemRepository = punchItemRepository;
        this.punchItemQueryService = punchItemQueryService;
    }

    /**
     * {@code POST  /punch-items} : Create a new punchItem.
     *
     * @param punchItemDTO the punchItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new punchItemDTO, or with status {@code 400 (Bad Request)} if the punchItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/punch-items")
    public ResponseEntity<PunchItemDTO> createPunchItem(@Valid @RequestBody PunchItemDTO punchItemDTO) throws URISyntaxException {
        log.debug("REST request to save PunchItem : {}", punchItemDTO);
        if (punchItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new punchItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PunchItemDTO result = punchItemService.save(punchItemDTO);
        return ResponseEntity
            .created(new URI("/api/punch-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /punch-items/:id} : Updates an existing punchItem.
     *
     * @param id the id of the punchItemDTO to save.
     * @param punchItemDTO the punchItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated punchItemDTO,
     * or with status {@code 400 (Bad Request)} if the punchItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the punchItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/punch-items/{id}")
    public ResponseEntity<PunchItemDTO> updatePunchItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PunchItemDTO punchItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PunchItem : {}, {}", id, punchItemDTO);
        if (punchItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, punchItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!punchItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PunchItemDTO result = punchItemService.save(punchItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, punchItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /punch-items/:id} : Partial updates given fields of an existing punchItem, field will ignore if it is null
     *
     * @param id the id of the punchItemDTO to save.
     * @param punchItemDTO the punchItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated punchItemDTO,
     * or with status {@code 400 (Bad Request)} if the punchItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the punchItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the punchItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/punch-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PunchItemDTO> partialUpdatePunchItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PunchItemDTO punchItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PunchItem partially : {}, {}", id, punchItemDTO);
        if (punchItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, punchItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!punchItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PunchItemDTO> result = punchItemService.partialUpdate(punchItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, punchItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /punch-items} : get all the punchItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of punchItems in body.
     */
    @GetMapping("/punch-items")
    public ResponseEntity<List<PunchItemDTO>> getAllPunchItems(
        PunchItemCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PunchItems by criteria: {}", criteria);
        Page<PunchItemDTO> page = punchItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /punch-items/count} : count all the punchItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/punch-items/count")
    public ResponseEntity<Long> countPunchItems(PunchItemCriteria criteria) {
        log.debug("REST request to count PunchItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(punchItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /punch-items/:id} : get the "id" punchItem.
     *
     * @param id the id of the punchItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the punchItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/punch-items/{id}")
    public ResponseEntity<PunchItemDTO> getPunchItem(@PathVariable Long id) {
        log.debug("REST request to get PunchItem : {}", id);
        Optional<PunchItemDTO> punchItemDTO = punchItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(punchItemDTO);
    }

    /**
     * {@code DELETE  /punch-items/:id} : delete the "id" punchItem.
     *
     * @param id the id of the punchItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/punch-items/{id}")
    public ResponseEntity<Void> deletePunchItem(@PathVariable Long id) {
        log.debug("REST request to delete PunchItem : {}", id);
        punchItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
