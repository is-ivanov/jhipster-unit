package by.ivanov.unit.service;

import by.ivanov.unit.service.dto.BlockDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link by.ivanov.unit.domain.Block}.
 */
public interface BlockService {
    /**
     * Save a block.
     *
     * @param blockDTO the entity to save.
     * @return the persisted entity.
     */
    BlockDTO save(BlockDTO blockDTO);

    /**
     * Partially updates a block.
     *
     * @param blockDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BlockDTO> partialUpdate(BlockDTO blockDTO);

    /**
     * Get all the blocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BlockDTO> findAll(Pageable pageable);

    /**
     * Get all the blocks with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BlockDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" block.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BlockDTO> findOne(Long id);

    /**
     * Delete the "id" block.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
