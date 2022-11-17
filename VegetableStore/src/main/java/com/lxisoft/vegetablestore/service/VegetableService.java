package com.lxisoft.vegetablestore.service;

import com.lxisoft.vegetablestore.domain.Vegetable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Vegetable}.
 */
public interface VegetableService {
    /**
     * Save a vegetable.
     *
     * @param vegetable the entity to save.
     * @return the persisted entity.
     */
    Vegetable save(Vegetable vegetable);

    /**
     * Updates a vegetable.
     *
     * @param vegetable the entity to update.
     * @return the persisted entity.
     */
    Vegetable update(Vegetable vegetable);

    /**
     * Partially updates a vegetable.
     *
     * @param vegetable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Vegetable> partialUpdate(Vegetable vegetable);

    /**
     * Get all the vegetables.
     *
     * @return the list of entities.
     */
    List<Vegetable> findAll();

    /**
     * Get the "id" vegetable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Vegetable> findOne(Long id);

    /**
     * Delete the "id" vegetable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    public List<Vegetable> search(String word);

    public void image(String name, HttpServletResponse response) throws IOException;

}
