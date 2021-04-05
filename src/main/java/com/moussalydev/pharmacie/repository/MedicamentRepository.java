package com.moussalydev.pharmacie.repository;

import com.moussalydev.pharmacie.domain.Medicament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Medicament entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
    @Query("select medicament from Medicament medicament where medicament.user.login = ?#{principal.username}")
    Page<Medicament> findByUserIsCurrentUser(Pageable pageable);
}
