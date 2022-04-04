package by.ivanov.unit.repository;

import by.ivanov.unit.domain.Company;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface CompanyRepository extends JpaRepository<Company, Long> {}
