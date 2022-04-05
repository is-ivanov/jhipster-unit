package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Project;
import by.ivanov.unit.service.dto.ProjectDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
	@Named("generalContractorId")
	@Mapping(target = "generalContractor", source = "generalContractor", qualifiedByName = "id")
	@Mapping(target = "subContractors", source = "subContractors", qualifiedByName = "idSet")
	ProjectDTO toDto(Project s);

	@Mapping(target = "removeSubContractors", ignore = true)
	Project toEntity(ProjectDTO projectDTO);

	@Named("name")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "id")
	@Mapping(target = "name", source = "name")
	ProjectDTO toDtoName(Project project);

	@Mapping(target = "generalContractor", source = "generalContractor", qualifiedByName = "shortName")
	@Mapping(target = "subContractors", source = "subContractors", qualifiedByName = "idSet")
	ProjectDTO toDtoGenContractorName(Project project);
}
