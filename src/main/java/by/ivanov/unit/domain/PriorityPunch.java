package by.ivanov.unit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * A PriorityPunch.
 */
@Entity
@Table(name = "priority_punch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PriorityPunch implements Serializable {

	public static final String COLUMN_ID_NAME = "id";
	public static final String COLUMN_PRIORITY_NAME = "priority";
	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_DESCRIPTION_NAME = "description";

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "priority_punch_sequence")
	@SequenceGenerator(name = "priority_punch_sequence", sequenceName = "priority_punch__seq",
		initialValue = 100)
	@Column(name = COLUMN_ID_NAME, nullable = false)
	private Long id;

	@NotNull
	@Column(name = COLUMN_PRIORITY_NAME, nullable = false, unique = true)
	private Integer priority;

	@NotNull
	@Size(max = 20)
	@Column(name = COLUMN_NAME_NAME, length = 20, nullable = false, unique = true)
	private String name;

	@Column(name = COLUMN_DESCRIPTION_NAME)
	private String description;

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PriorityPunch id(Long id) {
		this.setId(id);
		return this;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public PriorityPunch priority(Integer priority) {
		this.setPriority(priority);
		return this;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PriorityPunch name(String name) {
		this.setName(name);
		return this;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PriorityPunch description(String description) {
		this.setDescription(description);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PriorityPunch)) {
			return false;
		}
		return id != null && id.equals(((PriorityPunch) o).id);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "PriorityPunch{" +
			"id=" + getId() +
			", priority=" + getPriority() +
			", name='" + getName() + "'" +
			", description='" + getDescription() + "'" +
			"}";
	}
}
