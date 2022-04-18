package by.ivanov.unit.service;

import by.ivanov.unit.config.Constants;
import by.ivanov.unit.domain.AppUser;
import by.ivanov.unit.domain.Authority;
import by.ivanov.unit.domain.Company;
import by.ivanov.unit.domain.User;
import by.ivanov.unit.repository.AppUserRepository;
import by.ivanov.unit.repository.AuthorityRepository;
import by.ivanov.unit.repository.CompanyRepository;
import by.ivanov.unit.repository.UserRepository;
import by.ivanov.unit.security.AuthoritiesConstants;
import by.ivanov.unit.security.SecurityUtils;
import by.ivanov.unit.service.dto.AdminUserDTO;
import by.ivanov.unit.service.dto.UserDTO;
import by.ivanov.unit.service.exception.MyEntityNotFoundException;
import by.ivanov.unit.web.rest.CompanyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthorityRepository authorityRepository;
	private final CacheManager cacheManager;
	private final CompanyRepository companyRepository;
	private final AppUserRepository appUserRepository;

	public UserService(
		UserRepository userRepository,
		PasswordEncoder passwordEncoder,
		AuthorityRepository authorityRepository,
		CacheManager cacheManager,
		CompanyRepository companyRepository,
		AppUserRepository appUserRepository
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authorityRepository = authorityRepository;
		this.cacheManager = cacheManager;
		this.companyRepository = companyRepository;
		this.appUserRepository = appUserRepository;
	}

	public Optional<User> activateRegistration(String key) {
		log.debug("Activating user for activation key {}", key);
		return userRepository
			.findOneByActivationKey(key)
			.map(user -> {
				// activate given user for the registration key.
				user.setActivated(true);
				user.setActivationKey(null);
				this.clearUserCaches(user);
				log.debug("Activated user: {}", user);
				return user;
			});
	}

	public Optional<User> completePasswordReset(String newPassword, String key) {
		log.debug("Reset user password for reset key {}", key);
		return userRepository
			.findOneByResetKey(key)
			.filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
			.map(user -> {
				user.setPassword(passwordEncoder.encode(newPassword));
				user.setResetKey(null);
				user.setResetDate(null);
				this.clearUserCaches(user);
				return user;
			});
	}

	public Optional<User> requestPasswordReset(String mail) {
		return userRepository
			.findOneByEmailIgnoreCase(mail)
			.filter(User::isActivated)
			.map(user -> {
				user.setResetKey(RandomUtil.generateResetKey());
				user.setResetDate(Instant.now());
				this.clearUserCaches(user);
				return user;
			});
	}

	public User registerUser(AdminUserDTO userDTO, String password) {
		userRepository
			.findOneByLogin(userDTO.getLogin().toLowerCase())
			.ifPresent(existingUser -> {
				boolean removed = removeNonActivatedUser(existingUser);
				if (!removed) {
					throw new UsernameAlreadyUsedException();
				}
			});
		userRepository
			.findOneByEmailIgnoreCase(userDTO.getEmail())
			.ifPresent(existingUser -> {
				boolean removed = removeNonActivatedUser(existingUser);
				if (!removed) {
					throw new EmailAlreadyUsedException();
				}
			});
		User newUser = new User();
		String encryptedPassword = passwordEncoder.encode(password);
		// new user gets initially a generated password
		newUser.setPassword(encryptedPassword);
		setBasicInfoToUser(userDTO, newUser);
		// new user is not active
		newUser.setActivated(false);
		// new user gets registration key
		newUser.setActivationKey(RandomUtil.generateActivationKey());
		Set<Authority> authorities = new HashSet<>();
		authorityRepository.findById(AuthoritiesConstants.ROLE_USER).ifPresent(authorities::add);
		newUser.setAuthorities(authorities);
		userRepository.save(newUser);
		this.clearUserCaches(newUser);
		updateUserCompany(newUser, userDTO.getCompanyId());
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	private boolean removeNonActivatedUser(User existingUser) {
		if (existingUser.isActivated()) {
			return false;
		}
		appUserRepository
			.findById(existingUser.getId())
			.ifPresent(appUser -> {
				appUserRepository.delete(appUser);
				appUserRepository.flush();
			});
		userRepository.delete(existingUser);
		userRepository.flush();
		this.clearUserCaches(existingUser);
		return true;
	}

	public User createUser(AdminUserDTO userDTO) {
		User user = new User();
		setBasicInfoToUser(userDTO, user);
		String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
		user.setPassword(encryptedPassword);
		user.setResetKey(RandomUtil.generateResetKey());
		user.setResetDate(Instant.now());
		user.setActivated(true);
		if (userDTO.getAuthorities() != null) {
			Set<Authority> authorities = userDTO
				.getAuthorities()
				.stream()
				.map(authorityRepository::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toSet());
			user.setAuthorities(authorities);
		}
		userRepository.save(user);
		this.clearUserCaches(user);
		log.debug("Created Information for User: {}", user);
		return user;
	}

	/**
	 * Update all information for a specific user, and return the modified user.
	 *
	 * @param userDTO user to update.
	 * @return updated user.
	 */
	public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
		return Optional
			.of(userRepository.findById(userDTO.getId()))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(user -> {
				this.clearUserCaches(user);
				setBasicInfoToUser(userDTO, user);
				user.setActivated(userDTO.isActivated());
				Set<Authority> managedAuthorities = user.getAuthorities();
				managedAuthorities.clear();
				userDTO
					.getAuthorities()
					.stream()
					.map(authorityRepository::findById)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.forEach(managedAuthorities::add);
				this.clearUserCaches(user);
				log.debug("Changed Information for User: {}", user);
				return updateUserCompany(user, userDTO.getCompanyId());
			})
			.map(AdminUserDTO::new);
	}

	public void deleteUser(String login) {
		userRepository
			.findOneByLogin(login)
			.ifPresent(user -> {
				appUserRepository.findById(user.getId()).ifPresent(appUserRepository::delete);
				userRepository.delete(user);
				this.clearUserCaches(user);
				log.debug("Deleted User: {}", user);
			});
	}

	/**
	 * Update basic information (first name, last name, email, language) for the current user.
	 *
	 * @param firstName first name of user.
	 * @param lastName  last name of user.
	 * @param email     email id of user.
	 * @param langKey   language key.
	 * @param imageUrl  image URL of user.
	 */
	public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
		SecurityUtils
			.getCurrentUserLogin()
			.flatMap(userRepository::findOneByLogin)
			.ifPresent(user -> {
				user.setFirstName(firstName);
				user.setLastName(lastName);
				if (email != null) {
					user.setEmail(email.toLowerCase());
				}
				user.setLangKey(langKey);
				user.setImageUrl(imageUrl);
				this.clearUserCaches(user);
				log.debug("Changed Information for User: {}", user);
			});
	}

	@Transactional
	public void changePassword(String currentClearTextPassword, String newPassword) {
		SecurityUtils
			.getCurrentUserLogin()
			.flatMap(userRepository::findOneByLogin)
			.ifPresent(user -> {
				String currentEncryptedPassword = user.getPassword();
				if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
					throw new InvalidPasswordException();
				}
				String encryptedPassword = passwordEncoder.encode(newPassword);
				user.setPassword(encryptedPassword);
				this.clearUserCaches(user);
				log.debug("Changed password for User: {}", user);
			});
	}

	@Transactional(readOnly = true)
	public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
		return appUserRepository.findAll(pageable).map(AdminUserDTO::new);
	}

	@Transactional(readOnly = true)
	public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
		return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthoritiesByLogin(String login) {
		return userRepository.findOneWithAuthoritiesByLogin(login);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthorities() {
		return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		userRepository
			.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(
				Instant.now().minus(3, ChronoUnit.DAYS)
			)
			.forEach(user -> {
				log.debug("Deleting not activated user {}", user.getLogin());
				appUserRepository.findById(user.getId()).ifPresent(appUserRepository::delete);
				userRepository.delete(user);
				this.clearUserCaches(user);
			});
	}

	/**
	 * Gets a list of all the authorities.
	 *
	 * @return a list of all the authorities.
	 */
	@Transactional(readOnly = true)
	public List<String> getAuthorities() {
		return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
	}

	private void setBasicInfoToUser(AdminUserDTO userDTO, User user) {
		user.setLogin(userDTO.getLogin().toLowerCase());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		if (userDTO.getEmail() != null) {
			user.setEmail(userDTO.getEmail().toLowerCase());
		}
		user.setImageUrl(userDTO.getImageUrl());
		if (userDTO.getLangKey() == null) {
			user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
		} else {
			user.setLangKey(userDTO.getLangKey());
		}
	}

	private void clearUserCaches(User user) {
		Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
		if (user.getEmail() != null) {
			Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
		}
	}

	private AppUser updateUserCompany(User user, Long companyId) {
		AppUser appUser = appUserRepository.findByUser_Id(user.getId()).orElse(new AppUser());
		appUser.setUser(user);
		Company company;
		if (companyId != null) {
			company =
				companyRepository
					.findById(companyId)
					.orElseThrow(() -> {
						throw new MyEntityNotFoundException(CompanyResource.ENTITY_NAME, "id", companyId);
					});
			appUser.setCompany(company);
		}
		appUserRepository.save(appUser);
		log.debug("Save AppUser for User: {}", user.getLogin());
		return appUser;
	}
}
