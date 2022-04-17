package by.ivanov.unit.service.mapper;

import by.ivanov.unit.domain.AppUser;
import by.ivanov.unit.domain.Company;
import by.ivanov.unit.domain.Project;
import by.ivanov.unit.domain.PunchList;
import by.ivanov.unit.service.dto.AppUserDTO;
import by.ivanov.unit.service.dto.CompanyDTO;
import by.ivanov.unit.service.dto.ProjectDTO;
import by.ivanov.unit.service.dto.PunchListDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link PunchList} and its DTO {@link PunchListDTO}.
 */
@Mapper(componentModel = "spring")
public interface PunchListMapper extends EntityMapper<PunchListDTO, PunchList> {
    @Mapping(target = "project", source = "project", qualifiedByName = "projectName")
	@Mapping(target = "author", source = "author", qualifiedByName = "userLastNameAndCompanyName")
    PunchListDTO toDto(PunchList s);

    @Named("projectName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProjectDTO toDtoProjectName(Project project);

	@Named("userLastNameAndCompanyName")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "id")
	@Mapping(target = "user.lastName", source = "user.lastName")
	@Mapping(target = "company", source = "company", qualifiedByName = "companyName")
	AppUserDTO toDtoUserLastNameAndCompanyName(AppUser user);

	@Named("companyName")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "id")
	@Mapping(target = "shortName", source = "shortName")
	CompanyDTO toDtoCompanyName(Company company);
}
