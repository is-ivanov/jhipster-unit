package by.ivanov.unit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link by.ivanov.unit.domain.AppUser} entity.
 */
public class AppUserDTO implements Serializable {

    private Long id;

    private UserDTO user;

    private CompanyDTO company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUserDTO)) {
            return false;
        }

        AppUserDTO appUserDTO = (AppUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserDTO{" +
            "id=" + getId() +
            ", user=" + getUser() +
            ", company=" + getCompany() +
            "}";
    }
}
