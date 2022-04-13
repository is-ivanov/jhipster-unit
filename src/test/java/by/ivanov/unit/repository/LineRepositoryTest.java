package by.ivanov.unit.repository;

import by.ivanov.unit.domain.Line;
import by.ivanov.unit.domain.enumeration.StatusLine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

	@Nested
	@DisplayName("test 'findAllRevisions' method")
	class FindAllRevisionsTest {
		@Test
		@DisplayName("get all revisions")
		void getAllRevisions() {
			List<String> allRevisions = repo.findAllRevisions();
			assertThat(allRevisions).hasSize(19);
			assertThat(allRevisions)
				.containsExactly("0", "1", "2", "3", "4", "5пр АН68", "5пр АН70", "АН20",
					"АН38", "АН42", "АН52", "АН54", "АН60", "АН8", "АН80", "АН81",
					"АН85", "АН87", "АН99");
		}
	}

	@Nested
	@DisplayName("test 'findAllRevisionsWithProject' method")
	class FindAllRevisionsWithProjectTest {
		@Test
		@DisplayName("get all revisions with project_id = 9")
		void getAllRevisionsWithProjectId9() {
			List<String> revisions = repo.findAllRevisionsWithProject(9L);
			assertThat(revisions).hasSize(7);
			assertThat(revisions)
				.containsExactly("0", "1", "2", "3", "4", "АН42", "АН8");
		}
	}

	@Nested
	@DisplayName("test 'findAllRevisionsWithBlock' method")
	class FindAllRevisionsWithBlockTest {
		@Test
		@DisplayName("get all revisions with block_id = 15")
		void getAllRevisionsWithBlockId15() {
			List<String> revisions = repo.findAllRevisionsWithBlock(15L);
			assertThat(revisions).hasSize(2);
			assertThat(revisions)
				.containsExactly("3", "5пр АН70");
		}
	}
}
