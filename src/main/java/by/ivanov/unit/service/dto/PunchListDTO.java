package by.ivanov.unit.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link by.ivanov.unit.domain.PunchList} entity.
 */
public class PunchListDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer number;

    @Size(max = 100)
    private String name;

    private String description;

	@NotNull
    private ProjectDTO project;

	@NotNull
	private AppUserDTO author;

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

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

	public AppUserDTO getAuthor() {
		return author;
	}

	public void setAuthor(AppUserDTO author) {
		this.author = author;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PunchListDTO)) {
            return false;
        }

        PunchListDTO punchListDTO = (PunchListDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, punchListDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PunchListDTO{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", project=" + getProject() +
            "}";
    }
}
