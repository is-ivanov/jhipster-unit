package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Project;
import by.ivanov.unit.service.dto.ProjectDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
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
}
