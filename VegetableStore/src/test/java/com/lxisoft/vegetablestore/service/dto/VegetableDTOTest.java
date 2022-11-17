package com.lxisoft.vegetablestore.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lxisoft.vegetablestore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VegetableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VegetableDTO.class);
        VegetableDTO vegetableDTO1 = new VegetableDTO();
        vegetableDTO1.setId(1L);
        VegetableDTO vegetableDTO2 = new VegetableDTO();
        assertThat(vegetableDTO1).isNotEqualTo(vegetableDTO2);
        vegetableDTO2.setId(vegetableDTO1.getId());
        assertThat(vegetableDTO1).isEqualTo(vegetableDTO2);
        vegetableDTO2.setId(2L);
        assertThat(vegetableDTO1).isNotEqualTo(vegetableDTO2);
        vegetableDTO1.setId(null);
        assertThat(vegetableDTO1).isNotEqualTo(vegetableDTO2);
    }
}
