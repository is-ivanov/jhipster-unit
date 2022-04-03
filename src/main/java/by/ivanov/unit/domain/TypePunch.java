package by.ivanov.unit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * A TypePunch.
 */
@Entity
@Table(name = "type_punch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypePunch implements Serializable {

	public static final String COLUMN_ID_NAME = "id";
	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_DESCRIPTION_NAME = "description";

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "type_punch_sequence")
	@SequenceGenerator(name = "type_punch_sequence", sequenceName = "type_punch__seq", initialValue = 100)
	@Column(name = COLUMN_ID_NAME, nullable = false)
	private Long id;

	@NotNull
	@Size(max = 20)
	@Column(name = COLUMN_NAME_NAME, nullable = false, unique = true, length = 20)
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

	public TypePunch id(Long id) {
		this.setId(id);
		return this;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TypePunch name(String name) {
		this.setName(name);
		return this;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TypePunch description(String description) {
		this.setDescription(description);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof TypePunch)) {
			return false;
		}
		return id != null && id.equals(((TypePunch) o).id);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "TypePunch{" +
			"id=" + getId() +
			", name='" + getName() + "'" +
			", description='" + getDescription() + "'" +
			"}";
	}
}
