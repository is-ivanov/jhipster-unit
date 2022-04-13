package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.PunchItem;
import by.ivanov.unit.service.dto.PunchItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PunchItem} and its DTO {@link PunchItemDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        TypePunchMapper.class, LineMapper.class, PunchListMapper.class, PriorityPunchMapper.class, CompanyMapper.class, UserMapper.class,
    }
)
public interface PunchItemMapper extends EntityMapper<PunchItemDTO, PunchItem> {
    @Mapping(target = "type", source = "type", qualifiedByName = "name")
    @Mapping(target = "line", source = "line", qualifiedByName = "tag")
    @Mapping(target = "punchList", source = "punchList", qualifiedByName = "number")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "name")
    @Mapping(target = "executor", source = "executor", qualifiedByName = "shortName")
    @Mapping(target = "author", source = "author", qualifiedByName = "lastName")
    PunchItemDTO toDto(PunchItem s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PunchItemDTO toDtoId(PunchItem punchItem);
}
