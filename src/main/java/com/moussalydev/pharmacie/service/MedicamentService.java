package com.moussalydev.pharmacie.service;

import com.moussalydev.pharmacie.service.dto.MedicamentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.moussalydev.pharmacie.domain.Medicament}.
 */
public interface MedicamentService {
    /**
     * Save a medicament.
     *
     * @param medicamentDTO the entity to save.
     * @return the persisted entity.
     */
    MedicamentDTO save(MedicamentDTO medicamentDTO);

    /**
     * Partially updates a medicament.
     *
     * @param medicamentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicamentDTO> partialUpdate(MedicamentDTO medicamentDTO);

    /**
     * Get all the medicaments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicamentDTO> findAll(Pageable pageable);

    Page<MedicamentDTO> findByUserIsCurrentUser(Pageable pageable);

    /**
     * Get the "id" medicament.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicamentDTO> findOne(Long id);

    /**
     * Delete the "id" medicament.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
