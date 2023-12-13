package com.lxisoft.socialmedia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lxisoft.socialmedia.IntegrationTest;
import com.lxisoft.socialmedia.domain.Reaction;
import com.lxisoft.socialmedia.domain.enumeration.ReactionMode;
import com.lxisoft.socialmedia.repository.ReactionRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReactionResourceIT {

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ReactionMode DEFAULT_REACTION_MODE = ReactionMode.LIKE;
    private static final ReactionMode UPDATED_REACTION_MODE = ReactionMode.HEART;

    private static final String ENTITY_API_URL = "/api/reactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReactionMockMvc;

    private Reaction reaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createEntity(EntityManager em) {
        Reaction reaction = new Reaction().time(DEFAULT_TIME).reactionMode(DEFAULT_REACTION_MODE);
        return reaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createUpdatedEntity(EntityManager em) {
        Reaction reaction = new Reaction().time(UPDATED_TIME).reactionMode(UPDATED_REACTION_MODE);
        return reaction;
    }

    @BeforeEach
    public void initTest() {
        reaction = createEntity(em);
    }

    @Test
    @Transactional
    void createReaction() throws Exception {
        int databaseSizeBeforeCreate = reactionRepository.findAll().size();
        // Create the Reaction
        restReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isCreated());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeCreate + 1);
        Reaction testReaction = reactionList.get(reactionList.size() - 1);
        assertThat(testReaction.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testReaction.getReactionMode()).isEqualTo(DEFAULT_REACTION_MODE);
    }

    @Test
    @Transactional
    void createReactionWithExistingId() throws Exception {
        // Create the Reaction with an existing ID
        reaction.setId(1L);

        int databaseSizeBeforeCreate = reactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReactions() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList
        restReactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].reactionMode").value(hasItem(DEFAULT_REACTION_MODE.toString())));
    }

    @Test
    @Transactional
    void getReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get the reaction
        restReactionMockMvc
            .perform(get(ENTITY_API_URL_ID, reaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reaction.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.reactionMode").value(DEFAULT_REACTION_MODE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReaction() throws Exception {
        // Get the reaction
        restReactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();

        // Update the reaction
        Reaction updatedReaction = reactionRepository.findById(reaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReaction are not directly saved in db
        em.detach(updatedReaction);
        updatedReaction.time(UPDATED_TIME).reactionMode(UPDATED_REACTION_MODE);

        restReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReaction))
            )
            .andExpect(status().isOk());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
        Reaction testReaction = reactionList.get(reactionList.size() - 1);
        assertThat(testReaction.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testReaction.getReactionMode()).isEqualTo(UPDATED_REACTION_MODE);
    }

    @Test
    @Transactional
    void putNonExistingReaction() throws Exception {
        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();
        reaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReaction() throws Exception {
        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();
        reaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReaction() throws Exception {
        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();
        reaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReactionWithPatch() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();

        // Update the reaction using partial update
        Reaction partialUpdatedReaction = new Reaction();
        partialUpdatedReaction.setId(reaction.getId());

        partialUpdatedReaction.time(UPDATED_TIME).reactionMode(UPDATED_REACTION_MODE);

        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReaction))
            )
            .andExpect(status().isOk());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
        Reaction testReaction = reactionList.get(reactionList.size() - 1);
        assertThat(testReaction.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testReaction.getReactionMode()).isEqualTo(UPDATED_REACTION_MODE);
    }

    @Test
    @Transactional
    void fullUpdateReactionWithPatch() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();

        // Update the reaction using partial update
        Reaction partialUpdatedReaction = new Reaction();
        partialUpdatedReaction.setId(reaction.getId());

        partialUpdatedReaction.time(UPDATED_TIME).reactionMode(UPDATED_REACTION_MODE);

        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReaction))
            )
            .andExpect(status().isOk());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
        Reaction testReaction = reactionList.get(reactionList.size() - 1);
        assertThat(testReaction.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testReaction.getReactionMode()).isEqualTo(UPDATED_REACTION_MODE);
    }

    @Test
    @Transactional
    void patchNonExistingReaction() throws Exception {
        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();
        reaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReaction() throws Exception {
        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();
        reaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReaction() throws Exception {
        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();
        reaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        int databaseSizeBeforeDelete = reactionRepository.findAll().size();

        // Delete the reaction
        restReactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, reaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
