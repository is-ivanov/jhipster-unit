package by.ivanov.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PriorityPunchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriorityPunch.class);
        PriorityPunch priorityPunch1 = new PriorityPunch();
        priorityPunch1.setId(1L);
        PriorityPunch priorityPunch2 = new PriorityPunch();
        priorityPunch2.setId(priorityPunch1.getId());
        assertThat(priorityPunch1).isEqualTo(priorityPunch2);
        priorityPunch2.setId(2L);
        assertThat(priorityPunch1).isNotEqualTo(priorityPunch2);
        priorityPunch1.setId(null);
        assertThat(priorityPunch1).isNotEqualTo(priorityPunch2);
    }
}
