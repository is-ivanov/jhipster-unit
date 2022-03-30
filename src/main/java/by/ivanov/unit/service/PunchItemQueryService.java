package by.ivanov.unit.service;

import by.ivanov.unit.domain.*; // for static metamodels
import by.ivanov.unit.domain.PunchItem;
import by.ivanov.unit.repository.PunchItemRepository;
import by.ivanov.unit.service.criteria.PunchItemCriteria;
import by.ivanov.unit.service.dto.PunchItemDTO;
import by.ivanov.unit.service.mapper.PunchItemMapper;
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
 * Service for executing complex queries for {@link PunchItem} entities in the database.
 * The main input is a {@link PunchItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PunchItemDTO} or a {@link Page} of {@link PunchItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PunchItemQueryService extends QueryService<PunchItem> {

    private final Logger log = LoggerFactory.getLogger(PunchItemQueryService.class);

    private final PunchItemRepository punchItemRepository;

    private final PunchItemMapper punchItemMapper;

    public PunchItemQueryService(PunchItemRepository punchItemRepository, PunchItemMapper punchItemMapper) {
        this.punchItemRepository = punchItemRepository;
        this.punchItemMapper = punchItemMapper;
    }

    /**
     * Return a {@link List} of {@link PunchItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PunchItemDTO> findByCriteria(PunchItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PunchItem> specification = createSpecification(criteria);
        return punchItemMapper.toDto(punchItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PunchItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PunchItemDTO> findByCriteria(PunchItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PunchItem> specification = createSpecification(criteria);
        return punchItemRepository.findAll(specification, page).map(punchItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PunchItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PunchItem> specification = createSpecification(criteria);
        return punchItemRepository.count(specification);
    }

    /**
     * Function to convert {@link PunchItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PunchItem> createSpecification(PunchItemCriteria criteria) {
        Specification<PunchItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PunchItem_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), PunchItem_.number));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), PunchItem_.location));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PunchItem_.description));
            }
            if (criteria.getRevisionDrawing() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRevisionDrawing(), PunchItem_.revisionDrawing));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), PunchItem_.status));
            }
            if (criteria.getClosedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClosedDate(), PunchItem_.closedDate));
            }
            if (criteria.getTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTypeId(), root -> root.join(PunchItem_.type, JoinType.LEFT).get(TypePunch_.id))
                    );
            }
            if (criteria.getLineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLineId(), root -> root.join(PunchItem_.line, JoinType.LEFT).get(Line_.id))
                    );
            }
            if (criteria.getPunchListId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPunchListId(),
                            root -> root.join(PunchItem_.punchList, JoinType.LEFT).get(PunchList_.id)
                        )
                    );
            }
            if (criteria.getPriorityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPriorityId(),
                            root -> root.join(PunchItem_.priority, JoinType.LEFT).get(PriorityPunch_.id)
                        )
                    );
            }
            if (criteria.getExecutorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getExecutorId(), root -> root.join(PunchItem_.executor, JoinType.LEFT).get(Company_.id))
                    );
            }
            if (criteria.getAuthorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAuthorId(), root -> root.join(PunchItem_.author, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getCommentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommentsId(),
                            root -> root.join(PunchItem_.comments, JoinType.LEFT).get(CommentPunch_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
