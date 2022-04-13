package by.ivanov.unit.web.rest;

import by.ivanov.unit.repository.CommentPunchRepository;
import by.ivanov.unit.service.CommentPunchQueryService;
import by.ivanov.unit.service.CommentPunchService;
import by.ivanov.unit.service.criteria.CommentPunchCriteria;
import by.ivanov.unit.service.dto.CommentPunchDTO;
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
 * REST controller for managing {@link by.ivanov.unit.domain.CommentPunch}.
 */
@RestController
@RequestMapping("/api")
public class CommentPunchResource {

    private final Logger log = LoggerFactory.getLogger(CommentPunchResource.class);

    private static final String ENTITY_NAME = "commentPunch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentPunchService commentPunchService;

    private final CommentPunchRepository commentPunchRepository;

    private final CommentPunchQueryService commentPunchQueryService;

    public CommentPunchResource(
        CommentPunchService commentPunchService,
        CommentPunchRepository commentPunchRepository,
        CommentPunchQueryService commentPunchQueryService
    ) {
        this.commentPunchService = commentPunchService;
        this.commentPunchRepository = commentPunchRepository;
        this.commentPunchQueryService = commentPunchQueryService;
    }

    /**
     * {@code POST  /comment-punches} : Create a new commentPunch.
     *
     * @param commentPunchDTO the commentPunchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentPunchDTO, or with status {@code 400 (Bad Request)} if the commentPunch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comment-punches")
    public ResponseEntity<CommentPunchDTO> createCommentPunch(@Valid @RequestBody CommentPunchDTO commentPunchDTO)
        throws URISyntaxException {
        log.debug("REST request to save CommentPunch : {}", commentPunchDTO);
        if (commentPunchDTO.getId() != null) {
            throw new BadRequestAlertException("A new commentPunch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentPunchDTO result = commentPunchService.save(commentPunchDTO);
        return ResponseEntity
            .created(new URI("/api/comment-punches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comment-punches/:id} : Updates an existing commentPunch.
     *
     * @param id the id of the commentPunchDTO to save.
     * @param commentPunchDTO the commentPunchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentPunchDTO,
     * or with status {@code 400 (Bad Request)} if the commentPunchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentPunchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comment-punches/{id}")
    public ResponseEntity<CommentPunchDTO> updateCommentPunch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommentPunchDTO commentPunchDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommentPunch : {}, {}", id, commentPunchDTO);
        if (commentPunchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentPunchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentPunchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommentPunchDTO result = commentPunchService.update(commentPunchDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentPunchDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comment-punches/:id} : Partial updates given fields of an existing commentPunch, field will ignore if it is null
     *
     * @param id the id of the commentPunchDTO to save.
     * @param commentPunchDTO the commentPunchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentPunchDTO,
     * or with status {@code 400 (Bad Request)} if the commentPunchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commentPunchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commentPunchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comment-punches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommentPunchDTO> partialUpdateCommentPunch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommentPunchDTO commentPunchDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommentPunch partially : {}, {}", id, commentPunchDTO);
        if (commentPunchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentPunchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentPunchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommentPunchDTO> result = commentPunchService.partialUpdate(commentPunchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentPunchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /comment-punches} : get all the commentPunches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commentPunches in body.
     */
    @GetMapping("/comment-punches")
    public ResponseEntity<List<CommentPunchDTO>> getAllCommentPunches(
        CommentPunchCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CommentPunches by criteria: {}", criteria);
        Page<CommentPunchDTO> page = commentPunchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comment-punches/count} : count all the commentPunches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/comment-punches/count")
    public ResponseEntity<Long> countCommentPunches(CommentPunchCriteria criteria) {
        log.debug("REST request to count CommentPunches by criteria: {}", criteria);
        return ResponseEntity.ok().body(commentPunchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /comment-punches/:id} : get the "id" commentPunch.
     *
     * @param id the id of the commentPunchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentPunchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comment-punches/{id}")
    public ResponseEntity<CommentPunchDTO> getCommentPunch(@PathVariable Long id) {
        log.debug("REST request to get CommentPunch : {}", id);
        Optional<CommentPunchDTO> commentPunchDTO = commentPunchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentPunchDTO);
    }

    /**
     * {@code DELETE  /comment-punches/:id} : delete the "id" commentPunch.
     *
     * @param id the id of the commentPunchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comment-punches/{id}")
    public ResponseEntity<Void> deleteCommentPunch(@PathVariable Long id) {
        log.debug("REST request to delete CommentPunch : {}", id);
        commentPunchService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
