package by.ivanov.unit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link by.ivanov.unit.domain.CommentPunch} entity.
 */
public class CommentPunchDTO implements Serializable {

    private Long id;

    @NotNull
    private String comment;

    private PunchItemDTO punchItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PunchItemDTO getPunchItem() {
        return punchItem;
    }

    public void setPunchItem(PunchItemDTO punchItem) {
        this.punchItem = punchItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentPunchDTO)) {
            return false;
        }

        CommentPunchDTO commentPunchDTO = (CommentPunchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentPunchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentPunchDTO{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", punchItem=" + getPunchItem() +
            "}";
    }
}
