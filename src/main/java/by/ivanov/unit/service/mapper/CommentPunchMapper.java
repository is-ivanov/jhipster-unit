package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.CommentPunch;
import by.ivanov.unit.domain.PunchItem;
import by.ivanov.unit.service.dto.CommentPunchDTO;
import by.ivanov.unit.service.dto.PunchItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommentPunch} and its DTO {@link CommentPunchDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentPunchMapper extends EntityMapper<CommentPunchDTO, CommentPunch> {
    @Mapping(target = "punchItem", source = "punchItem", qualifiedByName = "punchItemId")
    CommentPunchDTO toDto(CommentPunch s);

    @Named("punchItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PunchItemDTO toDtoPunchItemId(PunchItem punchItem);
}
