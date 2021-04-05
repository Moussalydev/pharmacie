package com.moussalydev.pharmacie.service;

import com.moussalydev.pharmacie.domain.Medicament;
import com.moussalydev.pharmacie.domain.Vente;
import com.moussalydev.pharmacie.repository.MedicamentRepository;
import com.moussalydev.pharmacie.repository.VenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);

    private final VenteRepository venteRepository;

    private final MedicamentRepository medicamentRepository;

    private Integer newstock;

    public StockService(VenteRepository venteRepository, MedicamentRepository medicamentRepository) {
        this.venteRepository = venteRepository;
        this.medicamentRepository = medicamentRepository;
    }

    public Vente Vendre(Vente vente) {
        vente = venteRepository.save(vente);
        Medicament medicament = medicamentRepository.findById(vente.getMedicament().getId()).get();
        newstock = medicament.getStock() - vente.getNombre();
        medicament.setStock(newstock);
        return vente;
    }
}
