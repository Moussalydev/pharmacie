package com.moussalydev.pharmacie.web.rest;

import com.moussalydev.pharmacie.repository.CategorieRepository;
import com.moussalydev.pharmacie.service.CategorieService;
import com.moussalydev.pharmacie.service.dto.CategorieDTO;
import com.moussalydev.pharmacie.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.moussalydev.pharmacie.domain.Categorie}.
 */
@RestController
@RequestMapping("/api")
public class CategorieResource {

    private final Logger log = LoggerFactory.getLogger(CategorieResource.class);

    private static final String ENTITY_NAME = "categorie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategorieService categorieService;

    private final CategorieRepository categorieRepository;

    public CategorieResource(CategorieService categorieService, CategorieRepository categorieRepository) {
        this.categorieService = categorieService;
        this.categorieRepository = categorieRepository;
    }

    /**
     * {@code POST  /categories} : Create a new categorie.
     *
     * @param categorieDTO the categorieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categorieDTO, or with status {@code 400 (Bad Request)} if the categorie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categories")
    public ResponseEntity<CategorieDTO> createCategorie(@Valid @RequestBody CategorieDTO categorieDTO) throws URISyntaxException {
        log.debug("REST request to save Categorie : {}", categorieDTO);
        if (categorieDTO.getId() != null) {
            throw new BadRequestAlertException("A new categorie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategorieDTO result = categorieService.save(categorieDTO);
        return ResponseEntity
            .created(new URI("/api/categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categories/:id} : Updates an existing categorie.
     *
     * @param id the id of the categorieDTO to save.
     * @param categorieDTO the categorieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorieDTO,
     * or with status {@code 400 (Bad Request)} if the categorieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categorieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategorieDTO> updateCategorie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategorieDTO categorieDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Categorie : {}, {}", id, categorieDTO);
        if (categorieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategorieDTO result = categorieService.save(categorieDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, categorieDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categories/:id} : Partial updates given fields of an existing categorie, field will ignore if it is null
     *
     * @param id the id of the categorieDTO to save.
     * @param categorieDTO the categorieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorieDTO,
     * or with status {@code 400 (Bad Request)} if the categorieDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categorieDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categorieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CategorieDTO> partialUpdateCategorie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategorieDTO categorieDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Categorie partially : {}, {}", id, categorieDTO);
        if (categorieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategorieDTO> result = categorieService.partialUpdate(categorieDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, categorieDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /categories} : get all the categories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("/categories")
    public List<CategorieDTO> getAllCategories() {
        log.debug("REST request to get all Categories");
        return categorieService.findAll();
    }

    /**
     * {@code GET  /categories/:id} : get the "id" categorie.
     *
     * @param id the id of the categorieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categorieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategorieDTO> getCategorie(@PathVariable Long id) {
        log.debug("REST request to get Categorie : {}", id);
        Optional<CategorieDTO> categorieDTO = categorieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categorieDTO);
    }

    /**
     * {@code DELETE  /categories/:id} : delete the "id" categorie.
     *
     * @param id the id of the categorieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategorie(@PathVariable Long id) {
        log.debug("REST request to delete Categorie : {}", id);
        categorieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
