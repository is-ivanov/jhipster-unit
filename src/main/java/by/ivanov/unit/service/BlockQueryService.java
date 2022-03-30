package by.ivanov.unit.service;

import by.ivanov.unit.domain.*; // for static metamodels
import by.ivanov.unit.domain.Block;
import by.ivanov.unit.repository.BlockRepository;
import by.ivanov.unit.service.criteria.BlockCriteria;
import by.ivanov.unit.service.dto.BlockDTO;
import by.ivanov.unit.service.mapper.BlockMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Block} entities in the database.
 * The main input is a {@link BlockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BlockDTO} or a {@link Page} of {@link BlockDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BlockQueryService extends QueryService<Block> {

    private final Logger log = LoggerFactory.getLogger(BlockQueryService.class);

    private final BlockRepository blockRepository;

    private final BlockMapper blockMapper;

    public BlockQueryService(BlockRepository blockRepository, BlockMapper blockMapper) {
        this.blockRepository = blockRepository;
        this.blockMapper = blockMapper;
    }

    /**
     * Return a {@link List} of {@link BlockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BlockDTO> findByCriteria(BlockCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Block> specification = createSpecification(criteria);
        return blockMapper.toDto(blockRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BlockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BlockDTO> findByCriteria(BlockCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Block> specification = createSpecification(criteria);
        return blockRepository.findAll(specification, page).map(blockMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BlockCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Block> specification = createSpecification(criteria);
        return blockRepository.count(specification);
    }

    /**
     * Function to convert {@link BlockCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Block> createSpecification(BlockCriteria criteria) {
        Specification<Block> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Block_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), Block_.number));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Block_.description));
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjectId(), root -> root.join(Block_.project, JoinType.LEFT).get(Project_.id))
                    );
            }
        }
        return specification;
    }
}
