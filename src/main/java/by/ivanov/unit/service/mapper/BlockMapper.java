package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Block;
import by.ivanov.unit.service.dto.BlockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Block} and its DTO {@link BlockDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProjectMapper.class })
public interface BlockMapper extends EntityMapper<BlockDTO, Block> {
    @Mapping(target = "project", source = "project", qualifiedByName = "name")
    BlockDTO toDto(Block s);

    @Named("number")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "number", source = "number")
    BlockDTO toDtoNumber(Block block);
}
