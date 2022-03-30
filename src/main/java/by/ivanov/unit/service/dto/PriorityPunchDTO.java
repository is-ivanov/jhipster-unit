package by.ivanov.unit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link by.ivanov.unit.domain.PriorityPunch} entity.
 */
public class PriorityPunchDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer priority;

    @NotNull
    @Size(max = 20)
    private String name;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriorityPunchDTO)) {
            return false;
        }

        PriorityPunchDTO priorityPunchDTO = (PriorityPunchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, priorityPunchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriorityPunchDTO{" +
            "id=" + getId() +
            ", priority=" + getPriority() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
