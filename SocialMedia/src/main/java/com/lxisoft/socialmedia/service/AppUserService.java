package com.lxisoft.socialmedia.service;

import com.lxisoft.socialmedia.domain.AppUser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.lxisoft.socialmedia.domain.AppUser}.
 */
public interface AppUserService {
    /**
     * Save a appUser.
     *
     * @param appUser the entity to save.
     * @return the persisted entity.
     */
    AppUser save(AppUser appUser);

    /**
     * Updates a appUser.
     *
     * @param appUser the entity to update.
     * @return the persisted entity.
     */
    AppUser update(AppUser appUser);

    /**
     * Partially updates a appUser.
     *
     * @param appUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppUser> partialUpdate(AppUser appUser);

    /**
     * Get all the appUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppUser> findAll(Pageable pageable);

    /**
     * Get all the appUsers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppUser> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" appUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppUser> findOne(Long id);

    /**
     * Delete the "id" appUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
