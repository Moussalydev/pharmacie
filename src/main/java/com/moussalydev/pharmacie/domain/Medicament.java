package com.moussalydev.pharmacie.domain;

import com.moussalydev.pharmacie.domain.enumeration.Sujet;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Medicament.
 */
@Entity
@Table(name = "medicament")
public class Medicament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prix", nullable = false)
    private Double prix;

    @Enumerated(EnumType.STRING)
    @Column(name = "cible")
    private Sujet cible;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne
    private Categorie categorie;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicament id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Medicament nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getPrix() {
        return this.prix;
    }

    public Medicament prix(Double prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Sujet getCible() {
        return this.cible;
    }

    public Medicament cible(Sujet cible) {
        this.cible = cible;
        return this;
    }

    public void setCible(Sujet cible) {
        this.cible = cible;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Medicament stock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }

    public Medicament categorie(Categorie categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public User getUser() {
        return this.user;
    }

    public Medicament user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicament)) {
            return false;
        }
        return id != null && id.equals(((Medicament) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medicament{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prix=" + getPrix() +
            ", cible='" + getCible() + "'" +
            ", stock=" + getStock() +
            "}";
    }
}
