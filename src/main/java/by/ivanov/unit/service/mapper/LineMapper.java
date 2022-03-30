package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Line;
import by.ivanov.unit.service.dto.LineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Line} and its DTO {@link LineDTO}.
 */
@Mapper(componentModel = "spring", uses = { BlockMapper.class })
public interface LineMapper extends EntityMapper<LineDTO, Line> {
    @Mapping(target = "block", source = "block", qualifiedByName = "number")
    LineDTO toDto(Line s);

    @Named("tag")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "tag", source = "tag")
    LineDTO toDtoTag(Line line);
}
