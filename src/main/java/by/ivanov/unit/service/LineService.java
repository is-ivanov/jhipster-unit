package by.ivanov.unit.service;

import by.ivanov.unit.service.dto.LineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
