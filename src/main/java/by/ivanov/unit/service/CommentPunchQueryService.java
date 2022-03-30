package by.ivanov.unit.service;

import by.ivanov.unit.domain.*; // for static metamodels
import by.ivanov.unit.domain.CommentPunch;
import by.ivanov.unit.repository.CommentPunchRepository;
import by.ivanov.unit.service.criteria.CommentPunchCriteria;
import by.ivanov.unit.service.dto.CommentPunchDTO;
import by.ivanov.unit.service.mapper.CommentPunchMapper;
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
 * Service for executing complex queries for {@link CommentPunch} entities in the database.
 * The main input is a {@link CommentPunchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommentPunchDTO} or a {@link Page} of {@link CommentPunchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommentPunchQueryService extends QueryService<CommentPunch> {

    private final Logger log = LoggerFactory.getLogger(CommentPunchQueryService.class);

    private final CommentPunchRepository commentPunchRepository;

    private final CommentPunchMapper commentPunchMapper;

    public CommentPunchQueryService(CommentPunchRepository commentPunchRepository, CommentPunchMapper commentPunchMapper) {
        this.commentPunchRepository = commentPunchRepository;
        this.commentPunchMapper = commentPunchMapper;
    }

    /**
     * Return a {@link List} of {@link CommentPunchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommentPunchDTO> findByCriteria(CommentPunchCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CommentPunch> specification = createSpecification(criteria);
        return commentPunchMapper.toDto(commentPunchRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CommentPunchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommentPunchDTO> findByCriteria(CommentPunchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CommentPunch> specification = createSpecification(criteria);
        return commentPunchRepository.findAll(specification, page).map(commentPunchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommentPunchCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CommentPunch> specification = createSpecification(criteria);
        return commentPunchRepository.count(specification);
    }

    /**
     * Function to convert {@link CommentPunchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CommentPunch> createSpecification(CommentPunchCriteria criteria) {
        Specification<CommentPunch> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CommentPunch_.id));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), CommentPunch_.comment));
            }
            if (criteria.getPunchItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPunchItemId(),
                            root -> root.join(CommentPunch_.punchItem, JoinType.LEFT).get(PunchItem_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
