package by.ivanov.unit.domain;

import by.ivanov.unit.domain.enumeration.StatusLine;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * A Line.
 */
@Entity
@Table(name = "line")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Line implements Serializable {

	public static final String COLUMN_ID_NAME = "id";
	public static final String COLUMN_TAG_NAME = "tag";
	public static final String COLUMN_REVISION_NAME = "revision";
	public static final String COLUMN_STATUS_NAME = "status";
	public static final String COLUMN_BLOCK_NAME = "block_id";

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "line_sequence")
	@SequenceGenerator(name = "line_sequence", sequenceName = "line__seq",
		initialValue = 100)
	@Column(name = COLUMN_ID_NAME, nullable = false)
	private Long id;

	@NotNull
	@Size(max = 50)
	@Column(name = COLUMN_TAG_NAME, length = 50, nullable = false)
	private String tag;

	@NotNull
	@Size(max = 20)
	@Column(name = COLUMN_REVISION_NAME, length = 20, nullable = false)
	private String revision;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_STATUS_NAME, nullable = false)
	private StatusLine status;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = COLUMN_BLOCK_NAME, nullable = false)
	@JsonIgnoreProperties(value = {"project"}, allowSetters = true)
	private Block block;

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Line id(Long id) {
		this.setId(id);
		return this;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Line tag(String tag) {
		this.setTag(tag);
		return this;
	}

	public String getRevision() {
		return this.revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public Line revision(String revision) {
		this.setRevision(revision);
		return this;
	}

	public StatusLine getStatus() {
		return this.status;
	}

	public void setStatus(StatusLine status) {
		this.status = status;
	}

	public Line status(StatusLine status) {
		this.setStatus(status);
		return this;
	}

	public Block getBlock() {
		return this.block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public Line block(Block block) {
		this.setBlock(block);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Line)) {
			return false;
		}
		return id != null && id.equals(((Line) o).id);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "Line{" +
			"id=" + getId() +
			", tag='" + getTag() + "'" +
			", revision='" + getRevision() + "'" +
			", status='" + getStatus() + "'" +
			"}";
	}
}
