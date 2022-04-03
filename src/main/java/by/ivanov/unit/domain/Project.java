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
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project implements Serializable {

	public static final String COLUMN_ID_NAME = "id";
	public static final String COLUMN_DESCRIPTION_NAME = "description";
	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_GENERAL_CONTRACTOR_NAME = "general_contractor_id";

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_sequence")
	@SequenceGenerator(name = "project_sequence", sequenceName = "project__seq",
		initialValue = 100)
	@Column(name = COLUMN_ID_NAME, nullable = false)
	private Long id;

	@NotNull
	@Size(max = 30)
	@Column(name = COLUMN_NAME_NAME, nullable = false, unique = true, length = 30)
	private String name;

	@Column(name = COLUMN_DESCRIPTION_NAME)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = COLUMN_GENERAL_CONTRACTOR_NAME)
	@JsonIgnoreProperties(value = {"projects"}, allowSetters = true)
	private Company generalContractor;

	@ManyToMany
	@JoinTable(
		name = "rel_projects__sub_contractors",
		joinColumns = @JoinColumn(name = "project_id"),
		inverseJoinColumns = @JoinColumn(name = "sub_contractor_id")
	)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = {"projects"}, allowSetters = true)
	private Set<Company> subContractors = new HashSet<>();

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Project id(Long id) {
		this.setId(id);
		return this;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Project name(String name) {
		this.setName(name);
		return this;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Project description(String description) {
		this.setDescription(description);
		return this;
	}

	public Company getGeneralContractor() {
		return this.generalContractor;
	}

	public void setGeneralContractor(Company company) {
		this.generalContractor = company;
	}

	public Project generalContractor(Company company) {
		this.setGeneralContractor(company);
		return this;
	}

	public Set<Company> getSubContractors() {
		return this.subContractors;
	}

	public void setSubContractors(Set<Company> companies) {
		this.subContractors = companies;
	}

	public Project subContractors(Set<Company> companies) {
		this.setSubContractors(companies);
		return this;
	}

	public Project addSubContractors(Company company) {
		this.subContractors.add(company);
		company.getProjects().add(this);
		return this;
	}

	public Project removeSubContractors(Company company) {
		this.subContractors.remove(company);
		company.getProjects().remove(this);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Project)) {
			return false;
		}
		return id != null && id.equals(((Project) o).id);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "Project{" +
			"id=" + getId() +
			", name='" + getName() + "'" +
			", description='" + getDescription() + "'" +
			"}";
	}
}
