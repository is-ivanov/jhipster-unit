package by.ivanov.unit.repository;

import by.ivanov.unit.domain.Line;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Line entity.
 */
@Repository
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
}
