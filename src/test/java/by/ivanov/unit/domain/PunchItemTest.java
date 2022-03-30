package by.ivanov.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PunchItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PunchItem.class);
        PunchItem punchItem1 = new PunchItem();
        punchItem1.setId(1L);
        PunchItem punchItem2 = new PunchItem();
        punchItem2.setId(punchItem1.getId());
        assertThat(punchItem1).isEqualTo(punchItem2);
        punchItem2.setId(2L);
        assertThat(punchItem1).isNotEqualTo(punchItem2);
        punchItem1.setId(null);
        assertThat(punchItem1).isNotEqualTo(punchItem2);
    }
}
