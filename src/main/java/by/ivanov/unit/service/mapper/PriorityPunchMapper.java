package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.PriorityPunch;
import by.ivanov.unit.service.dto.PriorityPunchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PriorityPunch} and its DTO {@link PriorityPunchDTO}.
 */
@Mapper(componentModel = "spring")
public interface PriorityPunchMapper extends EntityMapper<PriorityPunchDTO, PriorityPunch> {}
