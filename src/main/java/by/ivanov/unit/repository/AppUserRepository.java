package by.ivanov.unit.repository;

import by.ivanov.unit.domain.AppUser;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the AppUser entity.
 */
@Repository
@JaversSpringDataAuditable
public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {

	String APP_USERS_BY_LOGIN_CACHE = "appUsersByLogin";

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

	@EntityGraph(attributePaths = {"company", "user", "user.authorities"})
	@Cacheable(cacheNames = APP_USERS_BY_LOGIN_CACHE)
	Optional<AppUser> findOneWithCompanyAndAuthoritiesByUserLogin(String login);

	Optional<AppUser> findOneByUser_Login(String login);
}
