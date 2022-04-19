package by.ivanov.unit.repository;

import by.ivanov.unit.domain.AppUser;
import by.ivanov.unit.domain.Authority;
import by.ivanov.unit.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

class AppUserRepositoryTest extends BaseRepositoryIT {

	@Autowired
	private AppUserRepository repo;

	@Autowired
	private EntityManager em;

	@Nested
	@DisplayName("test 'findOneWithCompanyAndAuthoritiesByUserLogin' method")
	class FindOneWithCompanyAndAuthoritiesByUserLoginTest {
		@Test
		@DisplayName("get user with his company and authorities by one query")
		void getUserWithHisCompanyAndAuthoritiesByOneQuery() {

			String login = "pais1";
			String companyName = "НПЗ ПАиС";
			Long companyId = 3L;
			String authority = "ROLE_CUSTOMER";

			AppUser appUser = repo.findOneWithCompanyAndAuthoritiesByUserLogin(login).get();
			em.close();

			assertThat(appUser.getCompany().getId()).isEqualTo(companyId);
			assertThat(appUser.getCompany().getShortName()).isEqualTo(companyName);
			User user = appUser.getUser();
			assertThat(user.getLogin()).isEqualTo(login);
			assertThat(user.getAuthorities()).hasSize(1);
			assertThat(user.getAuthorities()).extracting(Authority::getName)
				.containsExactly(authority);
		}
	}
}
