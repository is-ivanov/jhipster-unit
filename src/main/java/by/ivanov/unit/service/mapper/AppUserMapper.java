package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.AppUser;
import by.ivanov.unit.service.dto.AppUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, CompanyMapper.class })
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "company", source = "company", qualifiedByName = "shortName")
    AppUserDTO toDto(AppUser s);
}
