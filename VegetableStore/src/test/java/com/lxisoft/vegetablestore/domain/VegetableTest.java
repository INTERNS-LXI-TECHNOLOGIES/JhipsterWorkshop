package com.lxisoft.vegetablestore.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lxisoft.vegetablestore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VegetableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vegetable.class);
        Vegetable vegetable1 = new Vegetable();
        vegetable1.setId(1L);
        Vegetable vegetable2 = new Vegetable();
        vegetable2.setId(vegetable1.getId());
        assertThat(vegetable1).isEqualTo(vegetable2);
        vegetable2.setId(2L);
        assertThat(vegetable1).isNotEqualTo(vegetable2);
        vegetable1.setId(null);
        assertThat(vegetable1).isNotEqualTo(vegetable2);
    }
}
