package by.ivanov.unit.repository;

import by.ivanov.unit.domain.Authority;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
@JaversSpringDataAuditable
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
