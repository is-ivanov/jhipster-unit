package by.ivanov.unit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link by.ivanov.unit.domain.Company} entity.
 */
public class CompanyDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String shortName;

    private String fullName;

    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyDTO)) {
            return false;
        }

        CompanyDTO companyDTO = (CompanyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id=" + getId() +
            ", shortName='" + getShortName() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
