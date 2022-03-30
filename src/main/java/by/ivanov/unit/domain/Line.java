package by.ivanov.unit.domain;

import by.ivanov.unit.domain.enumeration.StatusLine;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Line.
 */
@Entity
@Table(name = "line")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Line implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "tag", length = 50, nullable = false)
    private String tag;

    @NotNull
    @Size(max = 20)
    @Column(name = "revision", length = 20, nullable = false)
    private String revision;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusLine status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "project" }, allowSetters = true)
    private Block block;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Line id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return this.tag;
    }

    public Line tag(String tag) {
        this.setTag(tag);
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRevision() {
        return this.revision;
    }

    public Line revision(String revision) {
        this.setRevision(revision);
        return this;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public StatusLine getStatus() {
        return this.status;
    }

    public Line status(StatusLine status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusLine status) {
        this.status = status;
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
