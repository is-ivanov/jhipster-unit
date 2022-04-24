package by.ivanov.unit.repository;

import by.ivanov.unit.domain.PunchList;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the PunchList entity.
 */
@Repository
@JaversSpringDataAuditable
public interface PunchListRepository extends JpaRepository<PunchList, Long>, JpaSpecificationExecutor<PunchList> {
	default Optional<PunchList> findOneWithEagerRelationships(Long id) {
		return this.findOneWithToOneRelationships(id);
	}

	default List<PunchList> findAllWithEagerRelationships() {
		return this.findAllWithToOneRelationships();
	}

	@Override
	@EntityGraph(attributePaths = {"author.company", "author.user", "project"})
	@NotNull
	Page<PunchList> findAll(Specification<PunchList> spec, @NotNull Pageable pageable);

	default Page<PunchList> findAllWithEagerRelationships(Pageable pageable) {
		return this.findAllWithToOneRelationships(pageable);
	}

	@Query(
		value = "select distinct punchList from PunchList punchList left join fetch punchList.project",
		countQuery = "select count(distinct punchList) from PunchList punchList"
	)
	Page<PunchList> findAllWithToOneRelationships(Pageable pageable);

	@Query("select distinct punchList from PunchList punchList left join fetch punchList.project")
	List<PunchList> findAllWithToOneRelationships();

	@Query("select punchList from PunchList punchList left join fetch punchList.project where punchList.id =:id")
	Optional<PunchList> findOneWithToOneRelationships(@Param("id") Long id);
}
