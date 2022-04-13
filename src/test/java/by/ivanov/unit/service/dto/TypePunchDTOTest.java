package by.ivanov.unit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypePunchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePunchDTO.class);
        TypePunchDTO typePunchDTO1 = new TypePunchDTO();
        typePunchDTO1.setId(1L);
        TypePunchDTO typePunchDTO2 = new TypePunchDTO();
        assertThat(typePunchDTO1).isNotEqualTo(typePunchDTO2);
        typePunchDTO2.setId(typePunchDTO1.getId());
        assertThat(typePunchDTO1).isEqualTo(typePunchDTO2);
        typePunchDTO2.setId(2L);
        assertThat(typePunchDTO1).isNotEqualTo(typePunchDTO2);
        typePunchDTO1.setId(null);
        assertThat(typePunchDTO1).isNotEqualTo(typePunchDTO2);
    }
}
