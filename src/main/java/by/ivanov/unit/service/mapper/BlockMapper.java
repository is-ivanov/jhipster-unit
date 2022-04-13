package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Block;
import by.ivanov.unit.domain.Project;
import by.ivanov.unit.service.dto.BlockDTO;
import by.ivanov.unit.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Block} and its DTO {@link BlockDTO}.
 */
@Mapper(componentModel = "spring")
public interface BlockMapper extends EntityMapper<BlockDTO, Block> {
    @Mapping(target = "project", source = "project", qualifiedByName = "projectName")
    BlockDTO toDto(Block s);

    @Named("projectName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProjectDTO toDtoProjectName(Project project);
}
