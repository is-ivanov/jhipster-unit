package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Block;
import by.ivanov.unit.domain.Line;
import by.ivanov.unit.service.dto.BlockDTO;
import by.ivanov.unit.service.dto.LineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Line} and its DTO {@link LineDTO}.
 */
@Mapper(componentModel = "spring")
public interface LineMapper extends EntityMapper<LineDTO, Line> {
    @Mapping(target = "block", source = "block", qualifiedByName = "blockNumber")
    LineDTO toDto(Line s);

    @Named("blockNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "number", source = "number")
    BlockDTO toDtoBlockNumber(Block block);
}
