package by.ivanov.unit.web.rest;

import by.ivanov.unit.repository.PriorityPunchRepository;
import by.ivanov.unit.security.AuthoritiesConstants;
import by.ivanov.unit.service.PriorityPunchService;
import by.ivanov.unit.service.dto.PriorityPunchDTO;
import by.ivanov.unit.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link by.ivanov.unit.domain.PriorityPunch}.
 */
@RestController
@RequestMapping("/api")
public class PriorityPunchResource {

    private final Logger log = LoggerFactory.getLogger(PriorityPunchResource.class);

    private static final String ENTITY_NAME = "priorityPunch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PriorityPunchService priorityPunchService;

    private final PriorityPunchRepository priorityPunchRepository;

    public PriorityPunchResource(PriorityPunchService priorityPunchService, PriorityPunchRepository priorityPunchRepository) {
        this.priorityPunchService = priorityPunchService;
        this.priorityPunchRepository = priorityPunchRepository;
    }

    /**
     * {@code POST  /priority-punches} : Create a new priorityPunch.
     *
     * @param priorityPunchDTO the priorityPunchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new priorityPunchDTO, or with status {@code 400 (Bad Request)} if the priorityPunch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/priority-punches")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<PriorityPunchDTO> createPriorityPunch(@Valid @RequestBody PriorityPunchDTO priorityPunchDTO)
        throws URISyntaxException {
        log.debug("REST request to save PriorityPunch : {}", priorityPunchDTO);
        if (priorityPunchDTO.getId() != null) {
            throw new BadRequestAlertException("A new priorityPunch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PriorityPunchDTO result = priorityPunchService.save(priorityPunchDTO);
        return ResponseEntity
            .created(new URI("/api/priority-punches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /priority-punches/:id} : Updates an existing priorityPunch.
     *
     * @param id the id of the priorityPunchDTO to save.
     * @param priorityPunchDTO the priorityPunchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priorityPunchDTO,
     * or with status {@code 400 (Bad Request)} if the priorityPunchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the priorityPunchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/priority-punches/{id}")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<PriorityPunchDTO> updatePriorityPunch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PriorityPunchDTO priorityPunchDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PriorityPunch : {}, {}", id, priorityPunchDTO);
        if (priorityPunchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priorityPunchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priorityPunchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PriorityPunchDTO result = priorityPunchService.update(priorityPunchDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, priorityPunchDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /priority-punches/:id} : Partial updates given fields of an existing priorityPunch, field will ignore if it is null
     *
     * @param id the id of the priorityPunchDTO to save.
     * @param priorityPunchDTO the priorityPunchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priorityPunchDTO,
     * or with status {@code 400 (Bad Request)} if the priorityPunchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the priorityPunchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the priorityPunchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/priority-punches/{id}", consumes = { "application/json", "application/merge-patch+json" })
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<PriorityPunchDTO> partialUpdatePriorityPunch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PriorityPunchDTO priorityPunchDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PriorityPunch partially : {}, {}", id, priorityPunchDTO);
        if (priorityPunchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priorityPunchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priorityPunchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PriorityPunchDTO> result = priorityPunchService.partialUpdate(priorityPunchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, priorityPunchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /priority-punches} : get all the priorityPunches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of priorityPunches in body.
     */
    @GetMapping("/priority-punches")
    public List<PriorityPunchDTO> getAllPriorityPunches() {
        log.debug("REST request to get all PriorityPunches");
        return priorityPunchService.findAll();
    }

    /**
     * {@code GET  /priority-punches/:id} : get the "id" priorityPunch.
     *
     * @param id the id of the priorityPunchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the priorityPunchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/priority-punches/{id}")
    public ResponseEntity<PriorityPunchDTO> getPriorityPunch(@PathVariable Long id) {
        log.debug("REST request to get PriorityPunch : {}", id);
        Optional<PriorityPunchDTO> priorityPunchDTO = priorityPunchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(priorityPunchDTO);
    }

    /**
     * {@code DELETE  /priority-punches/:id} : delete the "id" priorityPunch.
     *
     * @param id the id of the priorityPunchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/priority-punches/{id}")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<Void> deletePriorityPunch(@PathVariable Long id) {
        log.debug("REST request to delete PriorityPunch : {}", id);
        priorityPunchService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
