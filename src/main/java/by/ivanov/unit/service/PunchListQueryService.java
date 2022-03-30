package by.ivanov.unit.service;

import by.ivanov.unit.domain.*; // for static metamodels
import by.ivanov.unit.domain.PunchList;
import by.ivanov.unit.repository.PunchListRepository;
import by.ivanov.unit.service.criteria.PunchListCriteria;
import by.ivanov.unit.service.dto.PunchListDTO;
import by.ivanov.unit.service.mapper.PunchListMapper;
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
 * Service for executing complex queries for {@link PunchList} entities in the database.
 * The main input is a {@link PunchListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PunchListDTO} or a {@link Page} of {@link PunchListDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PunchListQueryService extends QueryService<PunchList> {

    private final Logger log = LoggerFactory.getLogger(PunchListQueryService.class);

    private final PunchListRepository punchListRepository;

    private final PunchListMapper punchListMapper;

    public PunchListQueryService(PunchListRepository punchListRepository, PunchListMapper punchListMapper) {
        this.punchListRepository = punchListRepository;
        this.punchListMapper = punchListMapper;
    }

    /**
     * Return a {@link List} of {@link PunchListDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PunchListDTO> findByCriteria(PunchListCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PunchList> specification = createSpecification(criteria);
        return punchListMapper.toDto(punchListRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PunchListDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PunchListDTO> findByCriteria(PunchListCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PunchList> specification = createSpecification(criteria);
        return punchListRepository.findAll(specification, page).map(punchListMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PunchListCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PunchList> specification = createSpecification(criteria);
        return punchListRepository.count(specification);
    }

    /**
     * Function to convert {@link PunchListCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PunchList> createSpecification(PunchListCriteria criteria) {
        Specification<PunchList> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PunchList_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), PunchList_.number));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PunchList_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PunchList_.description));
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjectId(), root -> root.join(PunchList_.project, JoinType.LEFT).get(Project_.id))
                    );
            }
        }
        return specification;
    }
}
