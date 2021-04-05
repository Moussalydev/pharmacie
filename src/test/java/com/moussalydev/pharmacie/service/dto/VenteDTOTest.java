package com.moussalydev.pharmacie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.moussalydev.pharmacie.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VenteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VenteDTO.class);
        VenteDTO venteDTO1 = new VenteDTO();
        venteDTO1.setId(1L);
        VenteDTO venteDTO2 = new VenteDTO();
        assertThat(venteDTO1).isNotEqualTo(venteDTO2);
        venteDTO2.setId(venteDTO1.getId());
        assertThat(venteDTO1).isEqualTo(venteDTO2);
        venteDTO2.setId(2L);
        assertThat(venteDTO1).isNotEqualTo(venteDTO2);
        venteDTO1.setId(null);
        assertThat(venteDTO1).isNotEqualTo(venteDTO2);
    }
}
