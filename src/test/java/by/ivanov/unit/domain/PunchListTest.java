package by.ivanov.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PunchListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PunchList.class);
        PunchList punchList1 = new PunchList();
        punchList1.setId(1L);
        PunchList punchList2 = new PunchList();
        punchList2.setId(punchList1.getId());
        assertThat(punchList1).isEqualTo(punchList2);
        punchList2.setId(2L);
        assertThat(punchList1).isNotEqualTo(punchList2);
        punchList1.setId(null);
        assertThat(punchList1).isNotEqualTo(punchList2);
    }
}
