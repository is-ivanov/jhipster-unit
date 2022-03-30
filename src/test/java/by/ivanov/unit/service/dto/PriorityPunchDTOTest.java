package by.ivanov.unit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PriorityPunchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriorityPunchDTO.class);
        PriorityPunchDTO priorityPunchDTO1 = new PriorityPunchDTO();
        priorityPunchDTO1.setId(1L);
        PriorityPunchDTO priorityPunchDTO2 = new PriorityPunchDTO();
        assertThat(priorityPunchDTO1).isNotEqualTo(priorityPunchDTO2);
        priorityPunchDTO2.setId(priorityPunchDTO1.getId());
        assertThat(priorityPunchDTO1).isEqualTo(priorityPunchDTO2);
        priorityPunchDTO2.setId(2L);
        assertThat(priorityPunchDTO1).isNotEqualTo(priorityPunchDTO2);
        priorityPunchDTO1.setId(null);
        assertThat(priorityPunchDTO1).isNotEqualTo(priorityPunchDTO2);
    }
}
