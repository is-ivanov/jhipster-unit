package by.ivanov.unit.service;

import by.ivanov.unit.service.dto.LineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link by.ivanov.unit.domain.Line}.
 */
public interface LineService {
    /**
     * Save a line.
     *
     * @param lineDTO the entity to save.
     * @return the persisted entity.
     */
    LineDTO save(LineDTO lineDTO);

    /**
     * Updates a line.
     *
     * @param lineDTO the entity to update.
     * @return the persisted entity.
     */
    LineDTO update(LineDTO lineDTO);

    /**
     * Partially updates a line.
     *
     * @param lineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LineDTO> partialUpdate(LineDTO lineDTO);

    /**
     * Get all the lines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LineDTO> findAll(Pageable pageable);

    /**
     * Get all the lines with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LineDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" line.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LineDTO> findOne(Long id);

    /**
     * Delete the "id" line.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

	/**
	 * Get revisions for all Lines.
	 *
	 * @return the list of revisions.
	 */
	List<String> findAllRevisions();

	/**
	 * Get revisions for all Lines from project with ID
	 *
	 * @param projectId the ID of project.
	 * @return the list of revisions.
	 */
	List<String> findAllRevisionsWithProject(Long projectId);

	/**
	 * Get revisions for all Lines from block with ID
	 *
	 * @param blockId the ID of block.
	 * @return the list of revisions.
	 */
	List<String> findAllRevisionsWithBlock(Long blockId);
}
