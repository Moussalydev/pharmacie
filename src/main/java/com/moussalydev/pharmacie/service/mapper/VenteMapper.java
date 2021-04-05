package com.moussalydev.pharmacie.service.mapper;

import com.moussalydev.pharmacie.domain.*;
import com.moussalydev.pharmacie.service.dto.VenteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vente} and its DTO {@link VenteDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicamentMapper.class })
public interface VenteMapper extends EntityMapper<VenteDTO, Vente> {
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "nom")
    VenteDTO toDto(Vente s);
}
