package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Company;
import by.ivanov.unit.domain.Project;
import by.ivanov.unit.service.dto.CompanyDTO;
import by.ivanov.unit.service.dto.ProjectDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
    @Mapping(target = "generalContractor", source = "generalContractor", qualifiedByName = "companyId")
    @Mapping(target = "subContractors", source = "subContractors", qualifiedByName = "companyIdSet")
    ProjectDTO toDto(Project s);

    @Mapping(target = "removeSubContractors", ignore = true)
    Project toEntity(ProjectDTO projectDTO);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);

    @Named("companyIdSet")
    default Set<CompanyDTO> toDtoCompanyIdSet(Set<Company> company) {
        return company.stream().map(this::toDtoCompanyId).collect(Collectors.toSet());
    }
}
