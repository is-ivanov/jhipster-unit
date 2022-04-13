package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Company;
import by.ivanov.unit.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {}
