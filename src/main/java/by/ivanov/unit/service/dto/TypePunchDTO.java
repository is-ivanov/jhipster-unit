package by.ivanov.unit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link by.ivanov.unit.domain.TypePunch} entity.
 */
public class TypePunchDTO implements Serializable {

    private Long id;

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
        if (!(o instanceof TypePunchDTO)) {
            return false;
        }

        TypePunchDTO typePunchDTO = (TypePunchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typePunchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypePunchDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
