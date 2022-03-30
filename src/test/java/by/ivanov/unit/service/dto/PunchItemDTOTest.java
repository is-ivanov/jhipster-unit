package by.ivanov.unit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PunchItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PunchItemDTO.class);
        PunchItemDTO punchItemDTO1 = new PunchItemDTO();
        punchItemDTO1.setId(1L);
        PunchItemDTO punchItemDTO2 = new PunchItemDTO();
        assertThat(punchItemDTO1).isNotEqualTo(punchItemDTO2);
        punchItemDTO2.setId(punchItemDTO1.getId());
        assertThat(punchItemDTO1).isEqualTo(punchItemDTO2);
        punchItemDTO2.setId(2L);
        assertThat(punchItemDTO1).isNotEqualTo(punchItemDTO2);
        punchItemDTO1.setId(null);
        assertThat(punchItemDTO1).isNotEqualTo(punchItemDTO2);
    }
}
