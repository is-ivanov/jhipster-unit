package by.ivanov.unit.service;

import by.ivanov.unit.service.dto.PriorityPunchDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link by.ivanov.unit.domain.PriorityPunch}.
 */
public interface PriorityPunchService {
    /**
     * Save a priorityPunch.
     *
     * @param priorityPunchDTO the entity to save.
     * @return the persisted entity.
     */
    PriorityPunchDTO save(PriorityPunchDTO priorityPunchDTO);

    /**
     * Updates a priorityPunch.
     *
     * @param priorityPunchDTO the entity to update.
     * @return the persisted entity.
     */
    PriorityPunchDTO update(PriorityPunchDTO priorityPunchDTO);

    /**
     * Partially updates a priorityPunch.
     *
     * @param priorityPunchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PriorityPunchDTO> partialUpdate(PriorityPunchDTO priorityPunchDTO);

    /**
     * Get all the priorityPunches.
     *
     * @return the list of entities.
     */
    List<PriorityPunchDTO> findAll();

    /**
     * Get the "id" priorityPunch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PriorityPunchDTO> findOne(Long id);

    /**
     * Delete the "id" priorityPunch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
