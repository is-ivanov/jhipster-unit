package by.ivanov.unit.repository;

import by.ivanov.unit.domain.AppUser;
import by.ivanov.unit.domain.User;
import by.ivanov.unit.service.dto.AdminUserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AppUserRepositoryTest extends BaseRepositoryIT {

	@Autowired
	private AppUserRepository repo;

	@Test
	@DisplayName("Test name")
	@Sql("/sql/user-test-data.sql")
	void testName() {
		AppUser admin = repo.findOneWithAuthoritiesAndCompanyByUser_Login("admin").get();

		assertThat(admin.getId()).isEqualTo(1);
		assertThat(admin.getUser().getAuthorities()).hasSize(2);
		assertThat(admin.getCompany().getId()).isEqualTo(1);
		assertThat(admin.getCompany().getShortName()).isEqualTo("google");
	}
}
