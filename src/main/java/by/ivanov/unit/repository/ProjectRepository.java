package by.ivanov.unit.repository;

import by.ivanov.unit.domain.Project;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
