package by.ivanov.unit.service.dto;

import by.ivanov.unit.domain.enumeration.StatusPunch;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link by.ivanov.unit.domain.PunchItem} entity.
 */
public class PunchItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer number;

    private String location;

    @NotNull
    private String description;

    @Size(max = 20)
    private String revisionDrawing;

    @NotNull
    private StatusPunch status;

    private Instant closedDate;

    private TypePunchDTO type;

    private LineDTO line;

    private PunchListDTO punchList;

    private PriorityPunchDTO priority;

    private CompanyDTO executor;

    private UserDTO author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRevisionDrawing() {
        return revisionDrawing;
    }

    public void setRevisionDrawing(String revisionDrawing) {
        this.revisionDrawing = revisionDrawing;
    }

    public StatusPunch getStatus() {
        return status;
    }

    public void setStatus(StatusPunch status) {
        this.status = status;
    }

    public Instant getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Instant closedDate) {
        this.closedDate = closedDate;
    }

    public TypePunchDTO getType() {
        return type;
    }

    public void setType(TypePunchDTO type) {
        this.type = type;
    }

    public LineDTO getLine() {
        return line;
    }

    public void setLine(LineDTO line) {
        this.line = line;
    }

    public PunchListDTO getPunchList() {
        return punchList;
    }

    public void setPunchList(PunchListDTO punchList) {
        this.punchList = punchList;
    }

    public PriorityPunchDTO getPriority() {
        return priority;
    }

    public void setPriority(PriorityPunchDTO priority) {
        this.priority = priority;
    }

    public CompanyDTO getExecutor() {
        return executor;
    }

    public void setExecutor(CompanyDTO executor) {
        this.executor = executor;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PunchItemDTO)) {
            return false;
        }

        PunchItemDTO punchItemDTO = (PunchItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, punchItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PunchItemDTO{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", location='" + getLocation() + "'" +
            ", description='" + getDescription() + "'" +
            ", revisionDrawing='" + getRevisionDrawing() + "'" +
            ", status='" + getStatus() + "'" +
            ", closedDate='" + getClosedDate() + "'" +
            ", type=" + getType() +
            ", line=" + getLine() +
            ", punchList=" + getPunchList() +
            ", priority=" + getPriority() +
            ", executor=" + getExecutor() +
            ", author=" + getAuthor() +
            "}";
    }
}
