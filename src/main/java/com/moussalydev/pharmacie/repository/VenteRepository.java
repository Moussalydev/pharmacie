package com.moussalydev.pharmacie.repository;

import com.moussalydev.pharmacie.domain.Vente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {
    Page<Vente> findByMedicamentUserLoginOrderByDateAsc(String currentLogin, Pageable pageable);
}
