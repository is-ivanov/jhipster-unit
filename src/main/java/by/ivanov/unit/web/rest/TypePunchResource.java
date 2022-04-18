package by.ivanov.unit.web.rest;

import by.ivanov.unit.repository.TypePunchRepository;
import by.ivanov.unit.security.AuthoritiesConstants;
import by.ivanov.unit.service.TypePunchService;
import by.ivanov.unit.service.dto.TypePunchDTO;
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
 * REST controller for managing {@link by.ivanov.unit.domain.TypePunch}.
 */
@RestController
@RequestMapping("/api")
public class TypePunchResource {

    private final Logger log = LoggerFactory.getLogger(TypePunchResource.class);

    private static final String ENTITY_NAME = "typePunch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypePunchService typePunchService;

    private final TypePunchRepository typePunchRepository;

    public TypePunchResource(TypePunchService typePunchService, TypePunchRepository typePunchRepository) {
        this.typePunchService = typePunchService;
        this.typePunchRepository = typePunchRepository;
    }

    /**
     * {@code POST  /type-punches} : Create a new typePunch.
     *
     * @param typePunchDTO the typePunchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typePunchDTO, or with status {@code 400 (Bad Request)} if the typePunch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-punches")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<TypePunchDTO> createTypePunch(@Valid @RequestBody TypePunchDTO typePunchDTO) throws URISyntaxException {
        log.debug("REST request to save TypePunch : {}", typePunchDTO);
        if (typePunchDTO.getId() != null) {
            throw new BadRequestAlertException("A new typePunch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypePunchDTO result = typePunchService.save(typePunchDTO);
        return ResponseEntity
            .created(new URI("/api/type-punches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-punches/:id} : Updates an existing typePunch.
     *
     * @param id the id of the typePunchDTO to save.
     * @param typePunchDTO the typePunchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePunchDTO,
     * or with status {@code 400 (Bad Request)} if the typePunchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typePunchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-punches/{id}")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<TypePunchDTO> updateTypePunch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypePunchDTO typePunchDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypePunch : {}, {}", id, typePunchDTO);
        if (typePunchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePunchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typePunchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypePunchDTO result = typePunchService.update(typePunchDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typePunchDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-punches/:id} : Partial updates given fields of an existing typePunch, field will ignore if it is null
     *
     * @param id the id of the typePunchDTO to save.
     * @param typePunchDTO the typePunchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePunchDTO,
     * or with status {@code 400 (Bad Request)} if the typePunchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typePunchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typePunchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-punches/{id}", consumes = { "application/json", "application/merge-patch+json" })
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<TypePunchDTO> partialUpdateTypePunch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypePunchDTO typePunchDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypePunch partially : {}, {}", id, typePunchDTO);
        if (typePunchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePunchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typePunchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypePunchDTO> result = typePunchService.partialUpdate(typePunchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typePunchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-punches} : get all the typePunches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typePunches in body.
     */
    @GetMapping("/type-punches")
    public List<TypePunchDTO> getAllTypePunches() {
        log.debug("REST request to get all TypePunches");
        return typePunchService.findAll();
    }

    /**
     * {@code GET  /type-punches/:id} : get the "id" typePunch.
     *
     * @param id the id of the typePunchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typePunchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-punches/{id}")
    public ResponseEntity<TypePunchDTO> getTypePunch(@PathVariable Long id) {
        log.debug("REST request to get TypePunch : {}", id);
        Optional<TypePunchDTO> typePunchDTO = typePunchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typePunchDTO);
    }

    /**
     * {@code DELETE  /type-punches/:id} : delete the "id" typePunch.
     *
     * @param id the id of the typePunchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-punches/{id}")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
	public ResponseEntity<Void> deleteTypePunch(@PathVariable Long id) {
        log.debug("REST request to delete TypePunch : {}", id);
        typePunchService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
