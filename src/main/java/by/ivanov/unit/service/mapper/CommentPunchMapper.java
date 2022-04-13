package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.CommentPunch;
import by.ivanov.unit.service.dto.CommentPunchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommentPunch} and its DTO {@link CommentPunchDTO}.
 */
@Mapper(componentModel = "spring", uses = { PunchItemMapper.class })
public interface CommentPunchMapper extends EntityMapper<CommentPunchDTO, CommentPunch> {
    @Mapping(target = "punchItem", source = "punchItem", qualifiedByName = "id")
    CommentPunchDTO toDto(CommentPunch s);
}
