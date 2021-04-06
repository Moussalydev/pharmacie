package com.moussalydev.pharmacie.service.impl;

import com.moussalydev.pharmacie.domain.Medicament;
import com.moussalydev.pharmacie.repository.MedicamentRepository;
import com.moussalydev.pharmacie.service.MedicamentService;
import com.moussalydev.pharmacie.service.UserService;
import com.moussalydev.pharmacie.service.dto.MedicamentDTO;
import com.moussalydev.pharmacie.service.mapper.MedicamentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Medicament}.
 */
@Service
@Transactional
public class MedicamentServiceImpl implements MedicamentService {

    private final Logger log = LoggerFactory.getLogger(MedicamentServiceImpl.class);

    private final MedicamentRepository medicamentRepository;

    private final UserService userService;

    private final MedicamentMapper medicamentMapper;

    public MedicamentServiceImpl(MedicamentRepository medicamentRepository, MedicamentMapper medicamentMapper, UserService userService) {
        this.medicamentRepository = medicamentRepository;
        this.medicamentMapper = medicamentMapper;
        this.userService = userService;
    }

    @Override
    public MedicamentDTO save(MedicamentDTO medicamentDTO) {
        log.debug("Request to save Medicament : {}", medicamentDTO);
        Medicament medicament = medicamentMapper.toEntity(medicamentDTO);
        medicament = medicamentRepository.save(medicament.user(userService.getUserWithAuthorities().get()));
        return medicamentMapper.toDto(medicament);
    }

    @Override
    public Optional<MedicamentDTO> partialUpdate(MedicamentDTO medicamentDTO) {
        log.debug("Request to partially update Medicament : {}", medicamentDTO);

        return medicamentRepository
            .findById(medicamentDTO.getId())
            .map(
                existingMedicament -> {
                    medicamentMapper.partialUpdate(existingMedicament, medicamentDTO);
                    return existingMedicament;
                }
            )
            .map(medicamentRepository::save)
            .map(medicamentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicamentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Medicaments");
        return medicamentRepository.findAll(pageable).map(medicamentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicamentDTO> findByUserIsCurrentUser(Pageable pageable) {
        log.debug("Request to get all Medicaments");
        return medicamentRepository.findByUserIsCurrentUser(pageable).map(medicamentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicamentDTO> findOne(Long id) {
        log.debug("Request to get Medicament : {}", id);
        return medicamentRepository.findById(id).map(medicamentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Medicament : {}", id);
        medicamentRepository.deleteById(id);
    }
}
