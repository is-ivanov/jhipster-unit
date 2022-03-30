package by.ivanov.unit.repository;

import by.ivanov.unit.domain.Block;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Block entity.
 */
@Repository
public interface BlockRepository extends JpaRepository<Block, Long>, JpaSpecificationExecutor<Block> {
    default Optional<Block> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Block> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Block> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct block from Block block left join fetch block.project",
        countQuery = "select count(distinct block) from Block block"
    )
    Page<Block> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct block from Block block left join fetch block.project")
    List<Block> findAllWithToOneRelationships();

    @Query("select block from Block block left join fetch block.project where block.id =:id")
    Optional<Block> findOneWithToOneRelationships(@Param("id") Long id);
}
