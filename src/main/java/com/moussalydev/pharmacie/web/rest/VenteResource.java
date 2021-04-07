package com.moussalydev.pharmacie.web.rest;

import com.moussalydev.pharmacie.repository.VenteRepository;
import com.moussalydev.pharmacie.security.SecurityUtils;
import com.moussalydev.pharmacie.service.VenteService;
import com.moussalydev.pharmacie.service.dto.VenteDTO;
import com.moussalydev.pharmacie.web.rest.errors.BadRequestAlertException;
import com.moussalydev.pharmacie.web.rest.errors.StockInsuffisantException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.moussalydev.pharmacie.domain.Vente}.
 */
@RestController
@RequestMapping("/api")
public class VenteResource {

    private final Logger log = LoggerFactory.getLogger(VenteResource.class);

    private static final String ENTITY_NAME = "vente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VenteService venteService;

    private final VenteRepository venteRepository;

    public VenteResource(VenteService venteService, VenteRepository venteRepository) {
        this.venteService = venteService;
        this.venteRepository = venteRepository;
    }

    /**
     * {@code POST  /ventes} : Create a new vente.
     *
     * @param venteDTO the venteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new venteDTO, or with status {@code 400 (Bad Request)} if the vente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ventes")
    public ResponseEntity<VenteDTO> createVente(@RequestBody VenteDTO venteDTO) throws URISyntaxException, StockInsuffisantException {
        log.debug("REST request to save Vente : {}", venteDTO);
        if (venteDTO.getId() != null) {
            throw new BadRequestAlertException("A new vente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (venteDTO.getMedicament().getStock() < venteDTO.getNombre()) {
            throw new StockInsuffisantException("Attention Le stock est insuffisant");
        }
        VenteDTO result = venteService.save(venteDTO);
        return ResponseEntity
            .created(new URI("/api/ventes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ventes/:id} : Updates an existing vente.
     *
     * @param id the id of the venteDTO to save.
     * @param venteDTO the venteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venteDTO,
     * or with status {@code 400 (Bad Request)} if the venteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the venteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ventes/{id}")
    public ResponseEntity<VenteDTO> updateVente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VenteDTO venteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vente : {}, {}", id, venteDTO);
        if (venteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, venteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!venteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VenteDTO result = venteService.save(venteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, venteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ventes/:id} : Partial updates given fields of an existing vente, field will ignore if it is null
     *
     * @param id the id of the venteDTO to save.
     * @param venteDTO the venteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venteDTO,
     * or with status {@code 400 (Bad Request)} if the venteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the venteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the venteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ventes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VenteDTO> partialUpdateVente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VenteDTO venteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vente partially : {}, {}", id, venteDTO);
        if (venteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, venteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!venteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VenteDTO> result = venteService.partialUpdate(venteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, venteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ventes} : get all the ventes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ventes in body.
     */
    @GetMapping("/ventes")
    public ResponseEntity<List<VenteDTO>> getAllVentes(Pageable pageable) {
        log.debug("REST request to get a page of Ventes");
        Page<VenteDTO> page = venteService.findByMedicamentUserLoginOrderByDateAsc(SecurityUtils.getCurrenLogin(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ventes/:id} : get the "id" vente.
     *
     * @param id the id of the venteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the venteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ventes/{id}")
    public ResponseEntity<VenteDTO> getVente(@PathVariable Long id) {
        log.debug("REST request to get Vente : {}", id);
        Optional<VenteDTO> venteDTO = venteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(venteDTO);
    }

    /**
     * {@code DELETE  /ventes/:id} : delete the "id" vente.
     *
     * @param id the id of the venteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ventes/{id}")
    public ResponseEntity<Void> deleteVente(@PathVariable Long id) {
        log.debug("REST request to delete Vente : {}", id);
        venteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
