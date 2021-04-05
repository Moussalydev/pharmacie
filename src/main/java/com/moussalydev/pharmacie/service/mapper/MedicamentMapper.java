package com.moussalydev.pharmacie.service.mapper;

import com.moussalydev.pharmacie.domain.*;
import com.moussalydev.pharmacie.service.dto.MedicamentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medicament} and its DTO {@link MedicamentDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategorieMapper.class, UserMapper.class })
public interface MedicamentMapper extends EntityMapper<MedicamentDTO, Medicament> {
    @Mapping(target = "categorie", source = "categorie", qualifiedByName = "nom")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    MedicamentDTO toDto(Medicament s);

    @Named("nom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    MedicamentDTO toDtoNom(Medicament medicament);
}
