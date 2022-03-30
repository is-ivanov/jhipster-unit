package by.ivanov.unit.service.dto;

import by.ivanov.unit.domain.enumeration.StatusLine;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link by.ivanov.unit.domain.Line} entity.
 */
public class LineDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String tag;

    @NotNull
    @Size(max = 20)
    private String revision;

    @NotNull
    private StatusLine status;

    private BlockDTO block;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public StatusLine getStatus() {
        return status;
    }

    public void setStatus(StatusLine status) {
        this.status = status;
    }

    public BlockDTO getBlock() {
        return block;
    }

    public void setBlock(BlockDTO block) {
        this.block = block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineDTO)) {
            return false;
        }

        LineDTO lineDTO = (LineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LineDTO{" +
            "id=" + getId() +
            ", tag='" + getTag() + "'" +
            ", revision='" + getRevision() + "'" +
            ", status='" + getStatus() + "'" +
            ", block=" + getBlock() +
            "}";
    }
}
