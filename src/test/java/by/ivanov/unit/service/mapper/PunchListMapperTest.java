package by.ivanov.unit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PunchListMapperTest {

    private PunchListMapper punchListMapper;

    @BeforeEach
    public void setUp() {
        punchListMapper = new PunchListMapperImpl();
    }
}
