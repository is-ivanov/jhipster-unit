package by.ivanov.unit.repository;

import by.ivanov.unit.domain.Project;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProjectRepositoryWithBagRelationshipsImpl implements ProjectRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Project> fetchBagRelationships(Optional<Project> project) {
        return project.map(this::fetchSubContractors);
    }

    @Override
    public Page<Project> fetchBagRelationships(Page<Project> projects) {
        return new PageImpl<>(fetchBagRelationships(projects.getContent()), projects.getPageable(), projects.getTotalElements());
    }

    @Override
    public List<Project> fetchBagRelationships(List<Project> projects) {
        return Optional.of(projects).map(this::fetchSubContractors).get();
    }

    Project fetchSubContractors(Project result) {
        return entityManager
            .createQuery(
                "select project from Project project left join fetch project.subContractors where project is :project",
                Project.class
            )
            .setParameter("project", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Project> fetchSubContractors(List<Project> projects) {
        return entityManager
            .createQuery(
                "select distinct project from Project project left join fetch project.subContractors where project in :projects",
                Project.class
            )
            .setParameter("projects", projects)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
