package by.ivanov.unit.service;

import by.ivanov.unit.service.dto.PunchListDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link by.ivanov.unit.domain.PunchList}.
 */
public interface PunchListService {
    /**
     * Save a punchList.
     *
     * @param punchListDTO the entity to save.
     * @return the persisted entity.
     */
    PunchListDTO save(PunchListDTO punchListDTO);

    /**
     * Updates a punchList.
     *
     * @param punchListDTO the entity to update.
     * @return the persisted entity.
     */
    PunchListDTO update(PunchListDTO punchListDTO);

    /**
     * Partially updates a punchList.
     *
     * @param punchListDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PunchListDTO> partialUpdate(PunchListDTO punchListDTO);

    /**
     * Get all the punchLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PunchListDTO> findAll(Pageable pageable);

    /**
     * Get all the punchLists with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PunchListDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" punchList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PunchListDTO> findOne(Long id);

    /**
     * Delete the "id" punchList.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
