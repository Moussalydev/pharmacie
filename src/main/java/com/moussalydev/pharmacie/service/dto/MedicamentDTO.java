package com.moussalydev.pharmacie.service.dto;

import com.moussalydev.pharmacie.domain.enumeration.Sujet;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.moussalydev.pharmacie.domain.Medicament} entity.
 */
public class MedicamentDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private Double prix;

    private Sujet cible;

    @NotNull
    private Integer stock;

    private CategorieDTO categorie;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Sujet getCible() {
        return cible;
    }

    public void setCible(Sujet cible) {
        this.cible = cible;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public CategorieDTO getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieDTO categorie) {
        this.categorie = categorie;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicamentDTO)) {
            return false;
        }

        MedicamentDTO medicamentDTO = (MedicamentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicamentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicamentDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prix=" + getPrix() +
            ", cible='" + getCible() + "'" +
            ", stock=" + getStock() +
            ", categorie=" + getCategorie() +
            ", user=" + getUser() +
            "}";
    }
}
