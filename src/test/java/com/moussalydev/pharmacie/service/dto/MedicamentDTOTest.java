package com.moussalydev.pharmacie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.moussalydev.pharmacie.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicamentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicamentDTO.class);
        MedicamentDTO medicamentDTO1 = new MedicamentDTO();
        medicamentDTO1.setId(1L);
        MedicamentDTO medicamentDTO2 = new MedicamentDTO();
        assertThat(medicamentDTO1).isNotEqualTo(medicamentDTO2);
        medicamentDTO2.setId(medicamentDTO1.getId());
        assertThat(medicamentDTO1).isEqualTo(medicamentDTO2);
        medicamentDTO2.setId(2L);
        assertThat(medicamentDTO1).isNotEqualTo(medicamentDTO2);
        medicamentDTO1.setId(null);
        assertThat(medicamentDTO1).isNotEqualTo(medicamentDTO2);
    }
}
