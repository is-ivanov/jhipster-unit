package by.ivanov.unit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypePunchMapperTest {

    private TypePunchMapper typePunchMapper;

    @BeforeEach
    public void setUp() {
        typePunchMapper = new TypePunchMapperImpl();
    }
}
