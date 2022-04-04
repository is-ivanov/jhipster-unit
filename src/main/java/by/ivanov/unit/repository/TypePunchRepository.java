package by.ivanov.unit.repository;

import by.ivanov.unit.domain.TypePunch;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypePunch entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface TypePunchRepository extends JpaRepository<TypePunch, Long> {}
