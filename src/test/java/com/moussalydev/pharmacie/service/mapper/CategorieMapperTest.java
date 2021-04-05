package com.moussalydev.pharmacie.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategorieMapperTest {

    private CategorieMapper categorieMapper;

    @BeforeEach
    public void setUp() {
        categorieMapper = new CategorieMapperImpl();
    }
}
