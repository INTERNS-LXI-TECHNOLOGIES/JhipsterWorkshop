package com.lxisoft.vegetablestore.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lxisoft.vegetablestore.IntegrationTest;
import com.lxisoft.vegetablestore.domain.Vegetable;
import com.lxisoft.vegetablestore.repository.VegetableRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link VegetableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VegetableResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE = "BBBBBBBBBB";

    private static final String DEFAULT_STOCK = "AAAAAAAAAA";
    private static final String UPDATED_STOCK = "BBBBBBBBBB";

    private static final String DEFAULT_MIN_ORDER_QUANTITY = "AAAAAAAAAA";
    private static final String UPDATED_MIN_ORDER_QUANTITY = "BBBBBBBBBB";

    private static final String DEFAULT_BASE_64_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_BASE_64_IMAGE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/vegetables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VegetableRepository vegetableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVegetableMockMvc;

    private Vegetable vegetable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vegetable createEntity(EntityManager em) {
        Vegetable vegetable = new Vegetable()
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .stock(DEFAULT_STOCK)
            .minOrderQuantity(DEFAULT_MIN_ORDER_QUANTITY)
            .base64Image(DEFAULT_BASE_64_IMAGE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return vegetable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vegetable createUpdatedEntity(EntityManager em) {
        Vegetable vegetable = new Vegetable()
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .stock(UPDATED_STOCK)
            .minOrderQuantity(UPDATED_MIN_ORDER_QUANTITY)
            .base64Image(UPDATED_BASE_64_IMAGE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return vegetable;
    }

    @BeforeEach
    public void initTest() {
        vegetable = createEntity(em);
    }

    @Test
    @Transactional
    void createVegetable() throws Exception {
        int databaseSizeBeforeCreate = vegetableRepository.findAll().size();
        // Create the Vegetable
        restVegetableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vegetable))
            )
            .andExpect(status().isCreated());

        // Validate the Vegetable in the database
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeCreate + 1);
        Vegetable testVegetable = vegetableList.get(vegetableList.size() - 1);
        assertThat(testVegetable.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVegetable.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testVegetable.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testVegetable.getMinOrderQuantity()).isEqualTo(DEFAULT_MIN_ORDER_QUANTITY);
        assertThat(testVegetable.getBase64Image()).isEqualTo(DEFAULT_BASE_64_IMAGE);
        assertThat(testVegetable.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testVegetable.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createVegetableWithExistingId() throws Exception {
        // Create the Vegetable with an existing ID
        vegetable.setId(1L);

        int databaseSizeBeforeCreate = vegetableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVegetableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vegetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vegetable in the database
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVegetables() throws Exception {
        // Initialize the database
        vegetableRepository.saveAndFlush(vegetable);

        // Get all the vegetableList
        restVegetableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vegetable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].minOrderQuantity").value(hasItem(DEFAULT_MIN_ORDER_QUANTITY)))
            .andExpect(jsonPath("$.[*].base64Image").value(hasItem(DEFAULT_BASE_64_IMAGE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getVegetable() throws Exception {
        // Initialize the database
        vegetableRepository.saveAndFlush(vegetable);

        // Get the vegetable
        restVegetableMockMvc
            .perform(get(ENTITY_API_URL_ID, vegetable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vegetable.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.minOrderQuantity").value(DEFAULT_MIN_ORDER_QUANTITY))
            .andExpect(jsonPath("$.base64Image").value(DEFAULT_BASE_64_IMAGE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingVegetable() throws Exception {
        // Get the vegetable
        restVegetableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVegetable() throws Exception {
        // Initialize the database
        vegetableRepository.saveAndFlush(vegetable);

        int databaseSizeBeforeUpdate = vegetableRepository.findAll().size();

        // Update the vegetable
        Vegetable updatedVegetable = vegetableRepository.findById(vegetable.getId()).get();
        // Disconnect from session so that the updates on updatedVegetable are not directly saved in db
        em.detach(updatedVegetable);
        updatedVegetable
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .stock(UPDATED_STOCK)
            .minOrderQuantity(UPDATED_MIN_ORDER_QUANTITY)
            .base64Image(UPDATED_BASE_64_IMAGE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restVegetableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVegetable.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVegetable))
            )
            .andExpect(status().isOk());

        // Validate the Vegetable in the database
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeUpdate);
        Vegetable testVegetable = vegetableList.get(vegetableList.size() - 1);
        assertThat(testVegetable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVegetable.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVegetable.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testVegetable.getMinOrderQuantity()).isEqualTo(UPDATED_MIN_ORDER_QUANTITY);
        assertThat(testVegetable.getBase64Image()).isEqualTo(UPDATED_BASE_64_IMAGE);
        assertThat(testVegetable.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testVegetable.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingVegetable() throws Exception {
        int databaseSizeBeforeUpdate = vegetableRepository.findAll().size();
        vegetable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVegetableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vegetable.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vegetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vegetable in the database
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVegetable() throws Exception {
        int databaseSizeBeforeUpdate = vegetableRepository.findAll().size();
        vegetable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVegetableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vegetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vegetable in the database
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVegetable() throws Exception {
        int databaseSizeBeforeUpdate = vegetableRepository.findAll().size();
        vegetable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVegetableMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vegetable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vegetable in the database
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVegetableWithPatch() throws Exception {
        // Initialize the database
        vegetableRepository.saveAndFlush(vegetable);

        int databaseSizeBeforeUpdate = vegetableRepository.findAll().size();

        // Update the vegetable using partial update
        Vegetable partialUpdatedVegetable = new Vegetable();
        partialUpdatedVegetable.setId(vegetable.getId());

        partialUpdatedVegetable
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .stock(UPDATED_STOCK)
            .base64Image(UPDATED_BASE_64_IMAGE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restVegetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVegetable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVegetable))
            )
            .andExpect(status().isOk());

        // Validate the Vegetable in the database
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeUpdate);
        Vegetable testVegetable = vegetableList.get(vegetableList.size() - 1);
        assertThat(testVegetable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVegetable.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVegetable.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testVegetable.getMinOrderQuantity()).isEqualTo(DEFAULT_MIN_ORDER_QUANTITY);
        assertThat(testVegetable.getBase64Image()).isEqualTo(UPDATED_BASE_64_IMAGE);
        assertThat(testVegetable.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testVegetable.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateVegetableWithPatch() throws Exception {
        // Initialize the database
        vegetableRepository.saveAndFlush(vegetable);

        int databaseSizeBeforeUpdate = vegetableRepository.findAll().size();

        // Update the vegetable using partial update
        Vegetable partialUpdatedVegetable = new Vegetable();
        partialUpdatedVegetable.setId(vegetable.getId());

        partialUpdatedVegetable
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .stock(UPDATED_STOCK)
            .minOrderQuantity(UPDATED_MIN_ORDER_QUANTITY)
            .base64Image(UPDATED_BASE_64_IMAGE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restVegetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVegetable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVegetable))
            )
            .andExpect(status().isOk());

        // Validate the Vegetable in the database
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeUpdate);
        Vegetable testVegetable = vegetableList.get(vegetableList.size() - 1);
        assertThat(testVegetable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVegetable.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVegetable.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testVegetable.getMinOrderQuantity()).isEqualTo(UPDATED_MIN_ORDER_QUANTITY);
        assertThat(testVegetable.getBase64Image()).isEqualTo(UPDATED_BASE_64_IMAGE);
        assertThat(testVegetable.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testVegetable.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingVegetable() throws Exception {
        int databaseSizeBeforeUpdate = vegetableRepository.findAll().size();
        vegetable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVegetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vegetable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vegetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vegetable in the database
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVegetable() throws Exception {
        int databaseSizeBeforeUpdate = vegetableRepository.findAll().size();
        vegetable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVegetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vegetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vegetable in the database
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVegetable() throws Exception {
        int databaseSizeBeforeUpdate = vegetableRepository.findAll().size();
        vegetable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVegetableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vegetable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vegetable in the database
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVegetable() throws Exception {
        // Initialize the database
        vegetableRepository.saveAndFlush(vegetable);

        int databaseSizeBeforeDelete = vegetableRepository.findAll().size();

        // Delete the vegetable
        restVegetableMockMvc
            .perform(delete(ENTITY_API_URL_ID, vegetable.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vegetable> vegetableList = vegetableRepository.findAll();
        assertThat(vegetableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
