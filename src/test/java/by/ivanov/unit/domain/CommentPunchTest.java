package by.ivanov.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentPunchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentPunch.class);
        CommentPunch commentPunch1 = new CommentPunch();
        commentPunch1.setId(1L);
        CommentPunch commentPunch2 = new CommentPunch();
        commentPunch2.setId(commentPunch1.getId());
        assertThat(commentPunch1).isEqualTo(commentPunch2);
        commentPunch2.setId(2L);
        assertThat(commentPunch1).isNotEqualTo(commentPunch2);
        commentPunch1.setId(null);
        assertThat(commentPunch1).isNotEqualTo(commentPunch2);
    }
}
