package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.TypePunch;
import by.ivanov.unit.service.dto.TypePunchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypePunch} and its DTO {@link TypePunchDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypePunchMapper extends EntityMapper<TypePunchDTO, TypePunch> {}
