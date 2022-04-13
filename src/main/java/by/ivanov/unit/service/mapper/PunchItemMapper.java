package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.Company;
import by.ivanov.unit.domain.Line;
import by.ivanov.unit.domain.PriorityPunch;
import by.ivanov.unit.domain.PunchItem;
import by.ivanov.unit.domain.PunchList;
import by.ivanov.unit.domain.TypePunch;
import by.ivanov.unit.domain.User;
import by.ivanov.unit.service.dto.CompanyDTO;
import by.ivanov.unit.service.dto.LineDTO;
import by.ivanov.unit.service.dto.PriorityPunchDTO;
import by.ivanov.unit.service.dto.PunchItemDTO;
import by.ivanov.unit.service.dto.PunchListDTO;
import by.ivanov.unit.service.dto.TypePunchDTO;
import by.ivanov.unit.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PunchItem} and its DTO {@link PunchItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface PunchItemMapper extends EntityMapper<PunchItemDTO, PunchItem> {
    @Mapping(target = "type", source = "type", qualifiedByName = "typePunchName")
    @Mapping(target = "line", source = "line", qualifiedByName = "lineTag")
    @Mapping(target = "punchList", source = "punchList", qualifiedByName = "punchListNumber")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "priorityPunchName")
    @Mapping(target = "executor", source = "executor", qualifiedByName = "companyShortName")
    @Mapping(target = "author", source = "author", qualifiedByName = "userLastName")
    PunchItemDTO toDto(PunchItem s);

    @Named("typePunchName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TypePunchDTO toDtoTypePunchName(TypePunch typePunch);

    @Named("lineTag")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "tag", source = "tag")
    LineDTO toDtoLineTag(Line line);

    @Named("punchListNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "number", source = "number")
    PunchListDTO toDtoPunchListNumber(PunchList punchList);

    @Named("priorityPunchName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PriorityPunchDTO toDtoPriorityPunchName(PriorityPunch priorityPunch);

    @Named("companyShortName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "shortName", source = "shortName")
    CompanyDTO toDtoCompanyShortName(Company company);

    @Named("userLastName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastName", source = "lastName")
    UserDTO toDtoUserLastName(User user);
}
