package com.lxisoft.socialmedia.service.impl;

import com.lxisoft.socialmedia.domain.AppUser;
import com.lxisoft.socialmedia.repository.AppUserRepository;
import com.lxisoft.socialmedia.service.AppUserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lxisoft.socialmedia.domain.AppUser}.
 */
@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser save(AppUser appUser) {
        log.debug("Request to save AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser update(AppUser appUser) {
        log.debug("Request to update AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    @Override
    public Optional<AppUser> partialUpdate(AppUser appUser) {
        log.debug("Request to partially update AppUser : {}", appUser);

        return appUserRepository
            .findById(appUser.getId())
            .map(existingAppUser -> {
                if (appUser.getPhone() != null) {
                    existingAppUser.setPhone(appUser.getPhone());
                }

                return existingAppUser;
            })
            .map(appUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppUser> findAll(Pageable pageable) {
        log.debug("Request to get all AppUsers");
        return appUserRepository.findAll(pageable);
    }

    public Page<AppUser> findAllWithEagerRelationships(Pageable pageable) {
        return appUserRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUser> findOne(Long id) {
        log.debug("Request to get AppUser : {}", id);
        return appUserRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppUser : {}", id);
        appUserRepository.deleteById(id);
    }
}
