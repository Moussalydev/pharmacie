package com.moussalydev.pharmacie.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A Vente.
 */
@Entity
@Table(name = "vente")
public class Vente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "nombre")
    private Integer nombre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "categorie", "user" }, allowSetters = true)
    private Medicament medicament;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vente id(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Vente date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getNombre() {
        return this.nombre;
    }

    public Vente nombre(Integer nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    public Medicament getMedicament() {
        return this.medicament;
    }

    public Vente medicament(Medicament medicament) {
        this.setMedicament(medicament);
        return this;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vente)) {
            return false;
        }
        return id != null && id.equals(((Vente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vente{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", nombre=" + getNombre() +
            "}";
    }
}
