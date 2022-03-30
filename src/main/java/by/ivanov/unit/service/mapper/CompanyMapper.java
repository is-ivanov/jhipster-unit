package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Company;
import by.ivanov.unit.service.dto.CompanyDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoId(Company company);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<CompanyDTO> toDtoIdSet(Set<Company> company);

    @Named("shortName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "shortName", source = "shortName")
    CompanyDTO toDtoShortName(Company company);
}
