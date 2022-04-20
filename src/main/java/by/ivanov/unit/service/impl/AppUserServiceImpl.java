package by.ivanov.unit.service.impl;

import by.ivanov.unit.domain.AppUser;
import by.ivanov.unit.repository.AppUserRepository;
import by.ivanov.unit.repository.UserRepository;
import by.ivanov.unit.security.SecurityUtils;
import by.ivanov.unit.service.AppUserService;
import by.ivanov.unit.service.dto.AppUserDTO;
import by.ivanov.unit.service.mapper.AppUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AppUser}.
 */
@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepository appUserRepository;

    private final AppUserMapper appUserMapper;

    private final UserRepository userRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository, AppUserMapper appUserMapper, UserRepository userRepository) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
        this.userRepository = userRepository;
    }

    @Override
    public AppUserDTO save(AppUserDTO appUserDTO) {
        log.debug("Request to save AppUser : {}", appUserDTO);
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        Long userId = appUserDTO.getUser().getId();
        userRepository.findById(userId).ifPresent(appUser::user);
        appUser = appUserRepository.save(appUser);
        return appUserMapper.toDto(appUser);
    }

    @Override
    public AppUserDTO update(AppUserDTO appUserDTO) {
        log.debug("Request to save AppUser : {}", appUserDTO);
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        Long userId = appUserDTO.getUser().getId();
        userRepository.findById(userId).ifPresent(appUser::user);
        appUser = appUserRepository.save(appUser);
        return appUserMapper.toDto(appUser);
    }

    @Override
    public Optional<AppUserDTO> partialUpdate(AppUserDTO appUserDTO) {
        log.debug("Request to partially update AppUser : {}", appUserDTO);

        return appUserRepository
            .findById(appUserDTO.getId())
            .map(existingAppUser -> {
                appUserMapper.partialUpdate(existingAppUser, appUserDTO);

                return existingAppUser;
            })
            .map(appUserRepository::save)
            .map(appUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppUsers");
        return appUserRepository.findAll(pageable).map(appUserMapper::toDto);
    }

    public Page<AppUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return appUserRepository.findAllWithEagerRelationships(pageable).map(appUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUserDTO> findOne(Long id) {
        log.debug("Request to get AppUser : {}", id);
        return appUserRepository.findOneWithEagerRelationships(id).map(appUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppUser : {}", id);
        appUserRepository.deleteById(id);
    }

	@Override
	@Transactional(readOnly = true)
	public Optional<AppUser> getCurrentUserWithCompanyAndAuthorities() {
		return SecurityUtils.getCurrentUserLogin()
			.flatMap(appUserRepository::findOneWithCompanyAndAuthoritiesByUserLogin);
	}

	@Override
	@Transactional(readOnly = true)
	public AppUser getCurrentUser() {
		return SecurityUtils.getCurrentUserLogin()
			.flatMap(appUserRepository::findOneByUser_Login)
			.orElseThrow(() -> new EntityNotFoundException("Current user not found"));
	}
}
