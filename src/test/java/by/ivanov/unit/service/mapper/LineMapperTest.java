package by.ivanov.unit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineMapperTest {

    private LineMapper lineMapper;

    @BeforeEach
    public void setUp() {
        lineMapper = new LineMapperImpl();
    }
}
