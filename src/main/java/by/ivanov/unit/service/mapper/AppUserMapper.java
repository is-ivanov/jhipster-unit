package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.AppUser;
import by.ivanov.unit.domain.Company;
import by.ivanov.unit.domain.User;
import by.ivanov.unit.service.dto.AppUserDTO;
import by.ivanov.unit.service.dto.CompanyDTO;
import by.ivanov.unit.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyShortName")
    AppUserDTO toDto(AppUser s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("companyShortName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "shortName", source = "shortName")
    CompanyDTO toDtoCompanyShortName(Company company);
}
