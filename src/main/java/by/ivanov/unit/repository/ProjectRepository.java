package by.ivanov.unit.repository;

import by.ivanov.unit.domain.Project;
import java.util.List;
import java.util.Optional;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Project entity.
 */
@Repository
@JaversSpringDataAuditable
public interface ProjectRepository extends ProjectRepositoryWithBagRelationships, JpaRepository<Project, Long> {
	default Optional<Project> findOneWithEagerRelationships(Long id) {
		return this.fetchBagRelationships(this.findById(id));
	}

	default List<Project> findAllWithEagerRelationships() {
		return this.fetchBagRelationships(this.findAll());
	}

	default Page<Project> findAllWithEagerRelationships(Pageable pageable) {
		return this.fetchBagRelationships(this.findAll(pageable));
	}
}
