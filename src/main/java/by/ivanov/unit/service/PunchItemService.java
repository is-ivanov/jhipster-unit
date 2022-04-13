package by.ivanov.unit.service;

import by.ivanov.unit.service.dto.PunchItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link by.ivanov.unit.domain.PunchItem}.
 */
public interface PunchItemService {
    /**
     * Save a punchItem.
     *
     * @param punchItemDTO the entity to save.
     * @return the persisted entity.
     */
    PunchItemDTO save(PunchItemDTO punchItemDTO);

    /**
     * Updates a punchItem.
     *
     * @param punchItemDTO the entity to update.
     * @return the persisted entity.
     */
    PunchItemDTO update(PunchItemDTO punchItemDTO);

    /**
     * Partially updates a punchItem.
     *
     * @param punchItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PunchItemDTO> partialUpdate(PunchItemDTO punchItemDTO);

    /**
     * Get all the punchItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PunchItemDTO> findAll(Pageable pageable);

    /**
     * Get all the punchItems with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PunchItemDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" punchItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PunchItemDTO> findOne(Long id);

    /**
     * Delete the "id" punchItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
