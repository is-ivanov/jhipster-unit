package by.ivanov.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import by.ivanov.unit.domain.Line;
import by.ivanov.unit.domain.enumeration.StatusLine;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LineRepositoryTest extends BaseRepositoryIT {

	@Autowired
	private LineRepository repo;

	@Nested
	@DisplayName("test 'findAllNonDeleted' method")
	class FindAllNonDeletedTest {

		@Test
		@DisplayName("get all lines without blocks")
		void getAllLinesWithoutBlocks() {
			List<Line> allNonDeleted = repo.findAllNonDeleted();
			assertThat(allNonDeleted).hasSize(77);
			assertThat(allNonDeleted).extracting(Line::getStatus).doesNotContain(StatusLine.DELETED);
		}
	}
}
