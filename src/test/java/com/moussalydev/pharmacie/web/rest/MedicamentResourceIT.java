package com.moussalydev.pharmacie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.moussalydev.pharmacie.IntegrationTest;
import com.moussalydev.pharmacie.domain.Medicament;
import com.moussalydev.pharmacie.domain.enumeration.Sujet;
import com.moussalydev.pharmacie.repository.MedicamentRepository;
import com.moussalydev.pharmacie.service.dto.MedicamentDTO;
import com.moussalydev.pharmacie.service.mapper.MedicamentMapper;
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

/**
 * Integration tests for the {@link MedicamentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicamentResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX = 1D;
    private static final Double UPDATED_PRIX = 2D;

    private static final Sujet DEFAULT_CIBLE = Sujet.Enfant;
    private static final Sujet UPDATED_CIBLE = Sujet.Adulte;

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    private static final String ENTITY_API_URL = "/api/medicaments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private MedicamentMapper medicamentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicamentMockMvc;

    private Medicament medicament;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicament createEntity(EntityManager em) {
        Medicament medicament = new Medicament().nom(DEFAULT_NOM).prix(DEFAULT_PRIX).cible(DEFAULT_CIBLE).stock(DEFAULT_STOCK);
        return medicament;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicament createUpdatedEntity(EntityManager em) {
        Medicament medicament = new Medicament().nom(UPDATED_NOM).prix(UPDATED_PRIX).cible(UPDATED_CIBLE).stock(UPDATED_STOCK);
        return medicament;
    }

    @BeforeEach
    public void initTest() {
        medicament = createEntity(em);
    }

    @Test
    @Transactional
    void createMedicament() throws Exception {
        int databaseSizeBeforeCreate = medicamentRepository.findAll().size();
        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);
        restMedicamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentDTO)))
            .andExpect(status().isCreated());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeCreate + 1);
        Medicament testMedicament = medicamentList.get(medicamentList.size() - 1);
        assertThat(testMedicament.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testMedicament.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testMedicament.getCible()).isEqualTo(DEFAULT_CIBLE);
        assertThat(testMedicament.getStock()).isEqualTo(DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void createMedicamentWithExistingId() throws Exception {
        // Create the Medicament with an existing ID
        medicament.setId(1L);
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        int databaseSizeBeforeCreate = medicamentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicamentRepository.findAll().size();
        // set the field null
        medicament.setNom(null);

        // Create the Medicament, which fails.
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        restMedicamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentDTO)))
            .andExpect(status().isBadRequest());

        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicamentRepository.findAll().size();
        // set the field null
        medicament.setPrix(null);

        // Create the Medicament, which fails.
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        restMedicamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentDTO)))
            .andExpect(status().isBadRequest());

        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicamentRepository.findAll().size();
        // set the field null
        medicament.setStock(null);

        // Create the Medicament, which fails.
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        restMedicamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentDTO)))
            .andExpect(status().isBadRequest());

        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicaments() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList
        restMedicamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicament.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())))
            .andExpect(jsonPath("$.[*].cible").value(hasItem(DEFAULT_CIBLE.toString())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)));
    }

    @Test
    @Transactional
    void getMedicament() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get the medicament
        restMedicamentMockMvc
            .perform(get(ENTITY_API_URL_ID, medicament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicament.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()))
            .andExpect(jsonPath("$.cible").value(DEFAULT_CIBLE.toString()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK));
    }

    @Test
    @Transactional
    void getNonExistingMedicament() throws Exception {
        // Get the medicament
        restMedicamentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMedicament() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();

        // Update the medicament
        Medicament updatedMedicament = medicamentRepository.findById(medicament.getId()).get();
        // Disconnect from session so that the updates on updatedMedicament are not directly saved in db
        em.detach(updatedMedicament);
        updatedMedicament.nom(UPDATED_NOM).prix(UPDATED_PRIX).cible(UPDATED_CIBLE).stock(UPDATED_STOCK);
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(updatedMedicament);

        restMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
        Medicament testMedicament = medicamentList.get(medicamentList.size() - 1);
        assertThat(testMedicament.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMedicament.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testMedicament.getCible()).isEqualTo(UPDATED_CIBLE);
        assertThat(testMedicament.getStock()).isEqualTo(UPDATED_STOCK);
    }

    @Test
    @Transactional
    void putNonExistingMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicamentWithPatch() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();

        // Update the medicament using partial update
        Medicament partialUpdatedMedicament = new Medicament();
        partialUpdatedMedicament.setId(medicament.getId());

        partialUpdatedMedicament.nom(UPDATED_NOM).prix(UPDATED_PRIX);

        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicament))
            )
            .andExpect(status().isOk());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
        Medicament testMedicament = medicamentList.get(medicamentList.size() - 1);
        assertThat(testMedicament.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMedicament.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testMedicament.getCible()).isEqualTo(DEFAULT_CIBLE);
        assertThat(testMedicament.getStock()).isEqualTo(DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void fullUpdateMedicamentWithPatch() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();

        // Update the medicament using partial update
        Medicament partialUpdatedMedicament = new Medicament();
        partialUpdatedMedicament.setId(medicament.getId());

        partialUpdatedMedicament.nom(UPDATED_NOM).prix(UPDATED_PRIX).cible(UPDATED_CIBLE).stock(UPDATED_STOCK);

        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicament))
            )
            .andExpect(status().isOk());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
        Medicament testMedicament = medicamentList.get(medicamentList.size() - 1);
        assertThat(testMedicament.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMedicament.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testMedicament.getCible()).isEqualTo(UPDATED_CIBLE);
        assertThat(testMedicament.getStock()).isEqualTo(UPDATED_STOCK);
    }

    @Test
    @Transactional
    void patchNonExistingMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicamentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicament() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        int databaseSizeBeforeDelete = medicamentRepository.findAll().size();

        // Delete the medicament
        restMedicamentMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicament.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
