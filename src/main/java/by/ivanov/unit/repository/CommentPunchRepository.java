package by.ivanov.unit.repository;

import by.ivanov.unit.domain.CommentPunch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommentPunch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentPunchRepository extends JpaRepository<CommentPunch, Long>, JpaSpecificationExecutor<CommentPunch> {}
