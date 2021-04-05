package com.moussalydev.pharmacie.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicamentMapperTest {

    private MedicamentMapper medicamentMapper;

    @BeforeEach
    public void setUp() {
        medicamentMapper = new MedicamentMapperImpl();
    }
}
