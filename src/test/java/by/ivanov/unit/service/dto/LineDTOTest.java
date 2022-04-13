package by.ivanov.unit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineDTO.class);
        LineDTO lineDTO1 = new LineDTO();
        lineDTO1.setId(1L);
        LineDTO lineDTO2 = new LineDTO();
        assertThat(lineDTO1).isNotEqualTo(lineDTO2);
        lineDTO2.setId(lineDTO1.getId());
        assertThat(lineDTO1).isEqualTo(lineDTO2);
        lineDTO2.setId(2L);
        assertThat(lineDTO1).isNotEqualTo(lineDTO2);
        lineDTO1.setId(null);
        assertThat(lineDTO1).isNotEqualTo(lineDTO2);
    }
}
