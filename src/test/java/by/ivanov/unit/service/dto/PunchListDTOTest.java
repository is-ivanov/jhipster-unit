package by.ivanov.unit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PunchListDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PunchListDTO.class);
        PunchListDTO punchListDTO1 = new PunchListDTO();
        punchListDTO1.setId(1L);
        PunchListDTO punchListDTO2 = new PunchListDTO();
        assertThat(punchListDTO1).isNotEqualTo(punchListDTO2);
        punchListDTO2.setId(punchListDTO1.getId());
        assertThat(punchListDTO1).isEqualTo(punchListDTO2);
        punchListDTO2.setId(2L);
        assertThat(punchListDTO1).isNotEqualTo(punchListDTO2);
        punchListDTO1.setId(null);
        assertThat(punchListDTO1).isNotEqualTo(punchListDTO2);
    }
}
