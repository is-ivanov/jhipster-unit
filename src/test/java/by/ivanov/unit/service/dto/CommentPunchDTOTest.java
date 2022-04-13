package by.ivanov.unit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentPunchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentPunchDTO.class);
        CommentPunchDTO commentPunchDTO1 = new CommentPunchDTO();
        commentPunchDTO1.setId(1L);
        CommentPunchDTO commentPunchDTO2 = new CommentPunchDTO();
        assertThat(commentPunchDTO1).isNotEqualTo(commentPunchDTO2);
        commentPunchDTO2.setId(commentPunchDTO1.getId());
        assertThat(commentPunchDTO1).isEqualTo(commentPunchDTO2);
        commentPunchDTO2.setId(2L);
        assertThat(commentPunchDTO1).isNotEqualTo(commentPunchDTO2);
        commentPunchDTO1.setId(null);
        assertThat(commentPunchDTO1).isNotEqualTo(commentPunchDTO2);
    }
}
