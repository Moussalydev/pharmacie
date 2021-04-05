package com.moussalydev.pharmacie.service.mapper;

import com.moussalydev.pharmacie.domain.*;
import com.moussalydev.pharmacie.service.dto.CategorieDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Categorie} and its DTO {@link CategorieDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategorieMapper extends EntityMapper<CategorieDTO, Categorie> {
    @Named("nom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    CategorieDTO toDtoNom(Categorie categorie);
}
