package by.ivanov.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Line.class);
        Line line1 = new Line();
        line1.setId(1L);
        Line line2 = new Line();
        line2.setId(line1.getId());
        assertThat(line1).isEqualTo(line2);
        line2.setId(2L);
        assertThat(line1).isNotEqualTo(line2);
        line1.setId(null);
        assertThat(line1).isNotEqualTo(line2);
    }
}
