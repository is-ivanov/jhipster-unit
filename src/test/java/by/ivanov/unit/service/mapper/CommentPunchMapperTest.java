package by.ivanov.unit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentPunchMapperTest {

    private CommentPunchMapper commentPunchMapper;

    @BeforeEach
    public void setUp() {
        commentPunchMapper = new CommentPunchMapperImpl();
    }
}
