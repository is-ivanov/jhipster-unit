package by.ivanov.unit.repository;

import by.ivanov.unit.domain.PriorityPunch;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PriorityPunch entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface PriorityPunchRepository extends JpaRepository<PriorityPunch, Long> {}
