package com.moussalydev.pharmacie.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.moussalydev.pharmacie.domain.Vente} entity.
 */
public class VenteDTO implements Serializable {

    private Long id;

    private ZonedDateTime date;

    private Integer nombre;

    private MedicamentDTO medicament;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    public MedicamentDTO getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentDTO medicament) {
        this.medicament = medicament;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VenteDTO)) {
            return false;
        }

        VenteDTO venteDTO = (VenteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, venteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VenteDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", nombre=" + getNombre() +
            ", medicament=" + getMedicament() +
            "}";
    }
}
