package com.lxisoft.vegetablestore.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VegetableMapperTest {

    private VegetableMapper vegetableMapper;

    @BeforeEach
    public void setUp() {
        vegetableMapper = new VegetableMapperImpl();
    }
}
