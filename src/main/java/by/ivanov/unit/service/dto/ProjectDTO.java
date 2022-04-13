package by.ivanov.unit.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link by.ivanov.unit.domain.Project} entity.
 */
public class ProjectDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String name;

    private String description;

    private CompanyDTO generalContractor;

    private Set<CompanyDTO> subContractors = new HashSet<>();

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

    public CompanyDTO getGeneralContractor() {
        return generalContractor;
    }

    public void setGeneralContractor(CompanyDTO generalContractor) {
        this.generalContractor = generalContractor;
    }

    public Set<CompanyDTO> getSubContractors() {
        return subContractors;
    }

    public void setSubContractors(Set<CompanyDTO> subContractors) {
        this.subContractors = subContractors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectDTO)) {
            return false;
        }

        ProjectDTO projectDTO = (ProjectDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, projectDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", generalContractor=" + getGeneralContractor() +
            ", subContractors=" + getSubContractors() +
            "}";
    }
}
