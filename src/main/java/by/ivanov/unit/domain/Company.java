package by.ivanov.unit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company implements Serializable {

	public static final String COLUMN_ID_NAME = "id";
	public static final String COLUMN_SHORT_NAME_NAME = "short_name";
	public static final String COLUMN_FULL_NAME_NAME = "full_name";
	public static final String COLUMN_EMAIL_NAME = "email";

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_sequence")
	@SequenceGenerator(name = "company_sequence", sequenceName = "company__seq", initialValue = 50)
	@Column(name = COLUMN_ID_NAME, nullable = false)
	private Long id;

	@NotNull
	@Size(max = 20)
	@Column(name = COLUMN_SHORT_NAME_NAME, length = 20, nullable = false, unique = true)
	private String shortName;

	@Column(name = COLUMN_FULL_NAME_NAME)
	private String fullName;

	@Column(name = COLUMN_EMAIL_NAME)
	private String email;

	@ManyToMany(mappedBy = "subContractors")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = {"generalContractor", "subContractors"}, allowSetters = true)
	private Set<Project> projects = new HashSet<>();

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Company id(Long id) {
		this.setId(id);
		return this;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Company shortName(String shortName) {
		this.setShortName(shortName);
		return this;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Company fullName(String fullName) {
		this.setFullName(fullName);
		return this;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Company email(String email) {
		this.setEmail(email);
		return this;
	}

	public Set<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(Set<Project> projects) {
		if (this.projects != null) {
			this.projects.forEach(i -> i.removeSubContractors(this));
		}
		if (projects != null) {
			projects.forEach(i -> i.addSubContractors(this));
		}
		this.projects = projects;
	}

	public Company projects(Set<Project> projects) {
		this.setProjects(projects);
		return this;
	}

	public Company addProjects(Project project) {
		this.projects.add(project);
		project.getSubContractors().add(this);
		return this;
	}

	public Company removeProjects(Project project) {
		this.projects.remove(project);
		project.getSubContractors().remove(this);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Company)) {
			return false;
		}
		return id != null && id.equals(((Company) o).id);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "Company{" +
			"id=" + getId() +
			", shortName='" + getShortName() + "'" +
			", fullName='" + getFullName() + "'" +
			", email='" + getEmail() + "'" +
			"}";
	}
}
