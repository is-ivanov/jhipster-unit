package by.ivanov.unit.repository;

import by.ivanov.unit.domain.AppUser;
import java.util.List;
import java.util.Optional;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AppUser entity.
 */
@Repository
@JaversSpringDataAuditable
public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {
	default Optional<AppUser> findOneWithEagerRelationships(Long id) {
		return this.findOneWithToOneRelationships(id);
	}

	default List<AppUser> findAllWithEagerRelationships() {
		return this.findAllWithToOneRelationships();
	}

	default Page<AppUser> findAllWithEagerRelationships(Pageable pageable) {
		return this.findAllWithToOneRelationships(pageable);
	}

	@Query(
		value = "select distinct appUser from AppUser appUser left join fetch appUser.company",
		countQuery = "select count(distinct appUser) from AppUser appUser"
	)
	Page<AppUser> findAllWithToOneRelationships(Pageable pageable);

	@Query("select distinct appUser from AppUser appUser left join fetch appUser.company")
	List<AppUser> findAllWithToOneRelationships();

	@Query("select appUser from AppUser appUser left join fetch appUser.company where appUser.id =:id")
	Optional<AppUser> findOneWithToOneRelationships(@Param("id") Long id);

	Optional<AppUser> findByUser_Id(Long id);
}
