package by.ivanov.unit.repository;

import by.ivanov.unit.domain.PunchItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PunchItem entity.
 */
@Repository
public interface PunchItemRepository extends JpaRepository<PunchItem, Long>, JpaSpecificationExecutor<PunchItem> {
    @Query("select punchItem from PunchItem punchItem where punchItem.author.login = ?#{principal.username}")
    List<PunchItem> findByAuthorIsCurrentUser();

    default Optional<PunchItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PunchItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PunchItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct punchItem from PunchItem punchItem left join fetch punchItem.type left join fetch punchItem.line left join fetch punchItem.punchList left join fetch punchItem.priority left join fetch punchItem.executor left join fetch punchItem.author",
        countQuery = "select count(distinct punchItem) from PunchItem punchItem"
    )
    Page<PunchItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct punchItem from PunchItem punchItem left join fetch punchItem.type left join fetch punchItem.line left join fetch punchItem.punchList left join fetch punchItem.priority left join fetch punchItem.executor left join fetch punchItem.author"
    )
    List<PunchItem> findAllWithToOneRelationships();

    @Query(
        "select punchItem from PunchItem punchItem left join fetch punchItem.type left join fetch punchItem.line left join fetch punchItem.punchList left join fetch punchItem.priority left join fetch punchItem.executor left join fetch punchItem.author where punchItem.id =:id"
    )
    Optional<PunchItem> findOneWithToOneRelationships(@Param("id") Long id);
}
