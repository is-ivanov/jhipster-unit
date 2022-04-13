package by.ivanov.unit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PunchItemMapperTest {

    private PunchItemMapper punchItemMapper;

    @BeforeEach
    public void setUp() {
        punchItemMapper = new PunchItemMapperImpl();
    }
}
