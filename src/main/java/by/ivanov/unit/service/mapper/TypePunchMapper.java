package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.TypePunch;
import by.ivanov.unit.service.dto.TypePunchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypePunch} and its DTO {@link TypePunchDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypePunchMapper extends EntityMapper<TypePunchDTO, TypePunch> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TypePunchDTO toDtoName(TypePunch typePunch);
}
