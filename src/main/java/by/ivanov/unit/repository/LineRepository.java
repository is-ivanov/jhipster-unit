package by.ivanov.unit.repository;

import by.ivanov.unit.domain.Line;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Line entity.
 */
@Repository
@JaversSpringDataAuditable
public interface LineRepository extends JpaRepository<Line, Long>, JpaSpecificationExecutor<Line> {
	default Optional<Line> findOneWithEagerRelationships(Long id) {
		return this.findOneWithToOneRelationships(id);
	}

	default List<Line> findAllWithEagerRelationships() {
		return this.findAllWithToOneRelationships();
	}

	default Page<Line> findAllWithEagerRelationships(Pageable pageable) {
		return this.findAllWithToOneRelationships(pageable);
	}

	@Query(
		value = "select distinct line from Line line left join fetch line.block",
		countQuery = "select count(distinct line) from Line line"
	)
	Page<Line> findAllWithToOneRelationships(Pageable pageable);

	@Query("select distinct line from Line line left join fetch line.block")
	List<Line> findAllWithToOneRelationships();

	@Query("select line from Line line left join fetch line.block where line.id =:id")
	Optional<Line> findOneWithToOneRelationships(@Param("id") Long id);

	@Query("SELECT l FROM Line l WHERE l.status <> 'DELETED'")
	List<Line> findAllNonDeleted();

	@Query("SELECT DISTINCT l.revision FROM Line l ORDER BY l.revision")
	List<String> findAllRevisions();

	@Query("""
		SELECT DISTINCT l.revision
		 FROM Line l
		WHERE l.block.project.id = :projectId
		ORDER BY l.revision
		""")
	List<String> findAllRevisionsWithProject(@Param("projectId") Long projectId);

	@Query("""
		SELECT DISTINCT l.revision
		 FROM Line l
		WHERE l.block.id = :blockId
		ORDER BY l.revision
		""")
	List<String> findAllRevisionsWithBlock(@Param("blockId") Long blockId);
}
