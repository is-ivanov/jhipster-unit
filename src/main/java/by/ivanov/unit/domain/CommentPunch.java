package by.ivanov.unit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CommentPunch.
 */
@Entity
@Table(name = "comment_punch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommentPunch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "type", "line", "punchList", "priority", "executor", "author", "comments" }, allowSetters = true)
    private PunchItem punchItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CommentPunch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return this.comment;
    }

    public CommentPunch comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
