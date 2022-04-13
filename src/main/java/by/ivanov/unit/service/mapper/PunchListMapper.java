package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Project;
import by.ivanov.unit.domain.PunchList;
import by.ivanov.unit.service.dto.ProjectDTO;
import by.ivanov.unit.service.dto.PunchListDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PunchList} and its DTO {@link PunchListDTO}.
 */
@Mapper(componentModel = "spring")
public interface PunchListMapper extends EntityMapper<PunchListDTO, PunchList> {
    @Mapping(target = "project", source = "project", qualifiedByName = "projectName")
    PunchListDTO toDto(PunchList s);

    @Named("projectName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProjectDTO toDtoProjectName(Project project);
}
