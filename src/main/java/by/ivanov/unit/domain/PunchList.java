package by.ivanov.unit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * A PunchList.
 */
@Entity
@Table(name = "punch_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PunchList implements Serializable {

	public static final String COLUMN_ID_NAME = "id";
	public static final String COLUMN_NUMBER_NAME = "number";
	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_DESCRIPTION_NAME = "description";
	public static final String COLUMN_PROJECT_NAME = "project_id";
	public static final String COLUMN_AUTHOR_NAME = "author_id";

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "punch_list_sequence")
	@SequenceGenerator(name = "punch_list_sequence", sequenceName = "punch_list__seq",
		initialValue = 100)
	@Column(name = COLUMN_ID_NAME, nullable = false)
	private Long id;

	@NotNull
	@Column(name = COLUMN_NUMBER_NAME, nullable = false)
	private Integer number;

	@Size(max = 100)
	@Column(name = COLUMN_NAME_NAME, length = 100)
	private String name;

	@Column(name = COLUMN_DESCRIPTION_NAME)
	private String description;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = COLUMN_PROJECT_NAME, nullable = false)
	@JsonIgnoreProperties(value = {"generalContractor", "subContractors"}, allowSetters = true)
	private Project project;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = COLUMN_AUTHOR_NAME, nullable = false)
	private AppUser author;

// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PunchList id(Long id) {
		this.setId(id);
		return this;
	}

	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public PunchList number(Integer number) {
		this.setNumber(number);
		return this;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PunchList name(String name) {
		this.setName(name);
		return this;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PunchList description(String description) {
		this.setDescription(description);
		return this;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public PunchList project(Project project) {
		this.setProject(project);
		return this;
	}

	public AppUser getAuthor() {
		return author;
	}

	public void setAuthor(AppUser author) {
		this.author = author;
	}

	public PunchList author(AppUser author) {
		this.author = author;
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PunchList)) {
			return false;
		}
		return id != null && id.equals(((PunchList) o).id);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "PunchList{" +
			"id=" + getId() +
			", number=" + getNumber() +
			", name='" + getName() + "'" +
			", description='" + getDescription() + "'" +
			"}";
	}
}
