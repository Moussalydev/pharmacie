package com.moussalydev.pharmacie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.moussalydev.pharmacie.IntegrationTest;
import com.moussalydev.pharmacie.domain.Categorie;
import com.moussalydev.pharmacie.repository.CategorieRepository;
import com.moussalydev.pharmacie.service.dto.CategorieDTO;
import com.moussalydev.pharmacie.service.mapper.CategorieMapper;
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
 * Integration tests for the {@link CategorieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategorieResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private CategorieMapper categorieMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategorieMockMvc;

    private Categorie categorie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorie createEntity(EntityManager em) {
        Categorie categorie = new Categorie().nom(DEFAULT_NOM);
        return categorie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorie createUpdatedEntity(EntityManager em) {
        Categorie categorie = new Categorie().nom(UPDATED_NOM);
        return categorie;
    }

    @BeforeEach
    public void initTest() {
        categorie = createEntity(em);
    }

    @Test
    @Transactional
    void createCategorie() throws Exception {
        int databaseSizeBeforeCreate = categorieRepository.findAll().size();
        // Create the Categorie
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);
        restCategorieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categorieDTO)))
            .andExpect(status().isCreated());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeCreate + 1);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createCategorieWithExistingId() throws Exception {
        // Create the Categorie with an existing ID
        categorie.setId(1L);
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);

        int databaseSizeBeforeCreate = categorieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategorieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categorieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieRepository.findAll().size();
        // set the field null
        categorie.setNom(null);

        // Create the Categorie, which fails.
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);

        restCategorieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categorieDTO)))
            .andExpect(status().isBadRequest());

        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategories() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList
        restCategorieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get the categorie
        restCategorieMockMvc
            .perform(get(ENTITY_API_URL_ID, categorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categorie.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingCategorie() throws Exception {
        // Get the categorie
        restCategorieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();

        // Update the categorie
        Categorie updatedCategorie = categorieRepository.findById(categorie.getId()).get();
        // Disconnect from session so that the updates on updatedCategorie are not directly saved in db
        em.detach(updatedCategorie);
        updatedCategorie.nom(UPDATED_NOM);
        CategorieDTO categorieDTO = categorieMapper.toDto(updatedCategorie);

        restCategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categorieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieDTO))
            )
            .andExpect(status().isOk());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();
        categorie.setId(count.incrementAndGet());

        // Create the Categorie
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categorieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();
        categorie.setId(count.incrementAndGet());

        // Create the Categorie
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();
        categorie.setId(count.incrementAndGet());

        // Create the Categorie
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categorieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategorieWithPatch() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();

        // Update the categorie using partial update
        Categorie partialUpdatedCategorie = new Categorie();
        partialUpdatedCategorie.setId(categorie.getId());

        restCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorie))
            )
            .andExpect(status().isOk());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdateCategorieWithPatch() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();

        // Update the categorie using partial update
        Categorie partialUpdatedCategorie = new Categorie();
        partialUpdatedCategorie.setId(categorie.getId());

        partialUpdatedCategorie.nom(UPDATED_NOM);

        restCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorie))
            )
            .andExpect(status().isOk());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();
        categorie.setId(count.incrementAndGet());

        // Create the Categorie
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categorieDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();
        categorie.setId(count.incrementAndGet());

        // Create the Categorie
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();
        categorie.setId(count.incrementAndGet());

        // Create the Categorie
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(categorieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        int databaseSizeBeforeDelete = categorieRepository.findAll().size();

        // Delete the categorie
        restCategorieMockMvc
            .perform(delete(ENTITY_API_URL_ID, categorie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
