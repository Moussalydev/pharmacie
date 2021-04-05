package com.moussalydev.pharmacie.service;

import com.moussalydev.pharmacie.service.dto.VenteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.moussalydev.pharmacie.domain.Vente}.
 */
public interface VenteService {
    /**
     * Save a vente.
     *
     * @param venteDTO the entity to save.
     * @return the persisted entity.
     */
    VenteDTO save(VenteDTO venteDTO);

    /**
     * Partially updates a vente.
     *
     * @param venteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VenteDTO> partialUpdate(VenteDTO venteDTO);

    /**
     * Get all the ventes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VenteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VenteDTO> findOne(Long id);

    Page<VenteDTO> findByMedicamentUserLoginOrderByDateAsc(String currentLogin, Pageable pageable);

    /**
     * Delete the "id" vente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
