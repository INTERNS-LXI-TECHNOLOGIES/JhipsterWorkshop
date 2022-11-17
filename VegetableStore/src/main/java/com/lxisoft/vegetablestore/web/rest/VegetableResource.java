package com.lxisoft.vegetablestore.web.rest;

import com.lxisoft.vegetablestore.domain.Vegetable;
import com.lxisoft.vegetablestore.repository.VegetableRepository;
import com.lxisoft.vegetablestore.service.VegetableService;
import com.lxisoft.vegetablestore.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lxisoft.vegetablestore.domain.Vegetable}.
 */
@RestController
@RequestMapping("/api")
public class VegetableResource {

    private final Logger log = LoggerFactory.getLogger(VegetableResource.class);

    private static final String ENTITY_NAME = "vegetable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VegetableService vegetableService;

    private final VegetableRepository vegetableRepository;

    public VegetableResource(VegetableService vegetableService, VegetableRepository vegetableRepository) {
        this.vegetableService = vegetableService;
        this.vegetableRepository = vegetableRepository;
    }

    /**
     * {@code POST  /vegetables} : Create a new vegetable.
     *
     * @param vegetable the vegetable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vegetable, or with status {@code 400 (Bad Request)} if the vegetable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vegetables")
    public ResponseEntity<Vegetable> createVegetable(@RequestBody Vegetable vegetable) throws URISyntaxException {
        log.debug("REST request to save Vegetable : {}", vegetable);
        if (vegetable.getId() != null) {
            throw new BadRequestAlertException("A new vegetable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vegetable result = vegetableService.save(vegetable);
        return ResponseEntity
            .created(new URI("/api/vegetables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vegetables/:id} : Updates an existing vegetable.
     *
     * @param id the id of the vegetable to save.
     * @param vegetable the vegetable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vegetable,
     * or with status {@code 400 (Bad Request)} if the vegetable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vegetable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vegetables/{id}")
    public ResponseEntity<Vegetable> updateVegetable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vegetable vegetable
    ) throws URISyntaxException {
        log.debug("REST request to update Vegetable : {}, {}", id, vegetable);
        if (vegetable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vegetable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vegetableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vegetable result = vegetableService.update(vegetable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vegetable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vegetables/:id} : Partial updates given fields of an existing vegetable, field will ignore if it is null
     *
     * @param id the id of the vegetable to save.
     * @param vegetable the vegetable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vegetable,
     * or with status {@code 400 (Bad Request)} if the vegetable is not valid,
     * or with status {@code 404 (Not Found)} if the vegetable is not found,
     * or with status {@code 500 (Internal Server Error)} if the vegetable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vegetables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vegetable> partialUpdateVegetable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vegetable vegetable
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vegetable partially : {}, {}", id, vegetable);
        if (vegetable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vegetable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vegetableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vegetable> result = vegetableService.partialUpdate(vegetable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vegetable.getId().toString())
        );
    }

    /**
     * {@code GET  /vegetables} : get all the vegetables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vegetables in body.
     */
    @GetMapping("/vegetables")
    public List<Vegetable> getAllVegetables() {
        log.debug("REST request to get all Vegetables");
        return vegetableService.findAll();
    }

    /**
     * {@code GET  /vegetables/:id} : get the "id" vegetable.
     *
     * @param id the id of the vegetable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vegetable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vegetables/{id}")
    public ResponseEntity<Vegetable> getVegetable(@PathVariable Long id) {
        log.debug("REST request to get Vegetable : {}", id);
        Optional<Vegetable> vegetable = vegetableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vegetable);
    }

    /**
     * {@code DELETE  /vegetables/:id} : delete the "id" vegetable.
     *
     * @param id the id of the vegetable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vegetables/{id}")
    public ResponseEntity<Void> deleteVegetable(@PathVariable Long id) {
        log.debug("REST request to delete Vegetable : {}", id);
        vegetableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
