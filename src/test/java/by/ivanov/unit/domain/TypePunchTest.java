package by.ivanov.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypePunchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePunch.class);
        TypePunch typePunch1 = new TypePunch();
        typePunch1.setId(1L);
        TypePunch typePunch2 = new TypePunch();
        typePunch2.setId(typePunch1.getId());
        assertThat(typePunch1).isEqualTo(typePunch2);
        typePunch2.setId(2L);
        assertThat(typePunch1).isNotEqualTo(typePunch2);
        typePunch1.setId(null);
        assertThat(typePunch1).isNotEqualTo(typePunch2);
    }
}
