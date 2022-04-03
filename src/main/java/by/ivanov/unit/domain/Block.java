package by.ivanov.unit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * A Block.
 */
@Entity
@Table(name = "block")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Block implements Serializable {

	public static final String COLUMN_ID_NAME = "id";
	public static final String COLUMN_NUMBER_NAME = "number";
	public static final String COLUMN_DESCRIPTION_NAME = "description";
	public static final String COLUMN_PROJECT_NAME = "project_id";

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "block_sequence")
	@SequenceGenerator(name = "block_sequence", sequenceName = "block__seq", allocationSize = 20)
	@Column(name = COLUMN_ID_NAME, nullable = false)
	private Long id;

	@NotNull
	@Column(name = COLUMN_NUMBER_NAME, nullable = false)
	private Integer number;

	@NotNull
	@Column(name = COLUMN_DESCRIPTION_NAME, nullable = false)
	private String description;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = COLUMN_PROJECT_NAME, nullable = false)
	@JsonIgnoreProperties(value = {"generalContractor", "subContractors"}, allowSetters = true)
	private Project project;

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Block id(Long id) {
		this.setId(id);
		return this;
	}

	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Block number(Integer number) {
		this.setNumber(number);
		return this;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Block description(String description) {
		this.setDescription(description);
		return this;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Block project(Project project) {
		this.setProject(project);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Block)) {
			return false;
		}
		return id != null && id.equals(((Block) o).id);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "Block{" +
			"id=" + getId() +
			", number=" + getNumber() +
			", description='" + getDescription() + "'" +
			"}";
	}
}
