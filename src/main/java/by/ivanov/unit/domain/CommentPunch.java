package by.ivanov.unit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * A CommentPunch.
 */
@Entity
@Table(name = "comment_punch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommentPunch implements Serializable {

	public static final String COLUMN_ID_NAME = "id";
	public static final String COLUMN_COMMENT_NAME = "comment";
	public static final String COLUMN_PUNCH_ITEM_NAME = "punch_item_id";

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_punch_sequence")
	@SequenceGenerator(name = "comment_punch_sequence", sequenceName = "comment_punch__seq")
	@Column(name = COLUMN_ID_NAME, nullable = false)
	private Long id;

	@NotNull
	@Column(name = COLUMN_COMMENT_NAME, nullable = false)
	private String comment;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = COLUMN_PUNCH_ITEM_NAME, nullable = false)
	@NotNull
	@JsonIgnoreProperties(value = {"type", "line", "punchList", "priority", "executor",
		"author", "comments"}, allowSetters = true)
	private PunchItem punchItem;

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CommentPunch id(Long id) {
		this.setId(id);
		return this;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public CommentPunch comment(String comment) {
		this.setComment(comment);
		return this;
	}

	public PunchItem getPunchItem() {
		return this.punchItem;
	}

	public void setPunchItem(PunchItem punchItem) {
		this.punchItem = punchItem;
	}

	public CommentPunch punchItem(PunchItem punchItem) {
		this.setPunchItem(punchItem);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CommentPunch)) {
			return false;
		}
		return id != null && id.equals(((CommentPunch) o).id);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "CommentPunch{" +
			"id=" + getId() +
			", comment='" + getComment() + "'" +
			"}";
	}
}
