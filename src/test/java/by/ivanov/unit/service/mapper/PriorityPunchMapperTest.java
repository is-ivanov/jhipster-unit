package by.ivanov.unit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PriorityPunchMapperTest {

    private PriorityPunchMapper priorityPunchMapper;

    @BeforeEach
    public void setUp() {
        priorityPunchMapper = new PriorityPunchMapperImpl();
    }
}
