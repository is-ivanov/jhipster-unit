package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.PunchList;
import by.ivanov.unit.service.dto.PunchListDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PunchList} and its DTO {@link PunchListDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProjectMapper.class })
public interface PunchListMapper extends EntityMapper<PunchListDTO, PunchList> {
    @Mapping(target = "project", source = "project", qualifiedByName = "name")
    PunchListDTO toDto(PunchList s);

    @Named("number")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "number", source = "number")
    PunchListDTO toDtoNumber(PunchList punchList);
}
