package by.ivanov.unit.service;

import by.ivanov.unit.service.dto.TypePunchDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link by.ivanov.unit.domain.TypePunch}.
 */
public interface TypePunchService {
    /**
     * Save a typePunch.
     *
     * @param typePunchDTO the entity to save.
     * @return the persisted entity.
     */
    TypePunchDTO save(TypePunchDTO typePunchDTO);

    /**
     * Partially updates a typePunch.
     *
     * @param typePunchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypePunchDTO> partialUpdate(TypePunchDTO typePunchDTO);

    /**
     * Get all the typePunches.
     *
     * @return the list of entities.
     */
    List<TypePunchDTO> findAll();

    /**
     * Get the "id" typePunch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypePunchDTO> findOne(Long id);

    /**
     * Delete the "id" typePunch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
