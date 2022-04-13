package by.ivanov.unit.service;

import by.ivanov.unit.service.dto.CommentPunchDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link by.ivanov.unit.domain.CommentPunch}.
 */
public interface CommentPunchService {
    /**
     * Save a commentPunch.
     *
     * @param commentPunchDTO the entity to save.
     * @return the persisted entity.
     */
    CommentPunchDTO save(CommentPunchDTO commentPunchDTO);

    /**
     * Updates a commentPunch.
     *
     * @param commentPunchDTO the entity to update.
     * @return the persisted entity.
     */
    CommentPunchDTO update(CommentPunchDTO commentPunchDTO);

    /**
     * Partially updates a commentPunch.
     *
     * @param commentPunchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommentPunchDTO> partialUpdate(CommentPunchDTO commentPunchDTO);

    /**
     * Get all the commentPunches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentPunchDTO> findAll(Pageable pageable);

    /**
     * Get the "id" commentPunch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentPunchDTO> findOne(Long id);

    /**
     * Delete the "id" commentPunch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
