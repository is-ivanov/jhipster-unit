package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Company;
import by.ivanov.unit.domain.Project;
import by.ivanov.unit.service.dto.CompanyDTO;
import by.ivanov.unit.service.dto.ProjectDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
    @Mapping(target = "generalContractor", source = "generalContractor", qualifiedByName = "companyId")
	@Mapping(target = "subContractors", ignore = true)
    ProjectDTO toDto(Project s);

	@Mapping(target = "generalContractor", source = "generalContractor",
		qualifiedByName = "companyName")
	@Mapping(target = "subContractors", source = "subContractors", qualifiedByName = "companyIdSet")
	ProjectDTO toDtoWithGenContractorName(Project s);

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

	@Named("companyName")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "id")
	@Mapping(target = "shortName", source = "shortName")
	CompanyDTO toDtoCompanyName(Company company);

	@Override
	default List<ProjectDTO> toDto(List<Project> entityList) {
		return entityList.stream().map(this::toDto).collect(Collectors.toList());
	}
}
