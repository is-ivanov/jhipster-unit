package by.ivanov.unit.domain;

import by.ivanov.unit.domain.enumeration.StatusPunch;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PunchItem.
 */
@Entity
@Table(name = "punch_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PunchItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "location")
    private String location;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Size(max = 20)
    @Column(name = "revision_drawing", length = 20)
    private String revisionDrawing;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusPunch status;

    @Column(name = "closed_date")
    private Instant closedDate;

    @ManyToOne(optional = false)
    @NotNull
    private TypePunch type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "block" }, allowSetters = true)
    private Line line;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "project" }, allowSetters = true)
    private PunchList punchList;

    @ManyToOne(optional = false)
    @NotNull
    private PriorityPunch priority;

    @ManyToOne
    @JsonIgnoreProperties(value = { "projects" }, allowSetters = true)
    private Company executor;

    @ManyToOne(optional = false)
    @NotNull
    private User author;

    @OneToMany(mappedBy = "punchItem")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "punchItem" }, allowSetters = true)
    private Set<CommentPunch> comments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PunchItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return this.number;
    }

    public PunchItem number(Integer number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLocation() {
        return this.location;
    }

    public PunchItem location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return this.description;
    }

    public PunchItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRevisionDrawing() {
        return this.revisionDrawing;
    }

    public PunchItem revisionDrawing(String revisionDrawing) {
        this.setRevisionDrawing(revisionDrawing);
        return this;
    }

    public void setRevisionDrawing(String revisionDrawing) {
        this.revisionDrawing = revisionDrawing;
    }

    public StatusPunch getStatus() {
        return this.status;
    }

    public PunchItem status(StatusPunch status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusPunch status) {
        this.status = status;
    }

    public Instant getClosedDate() {
        return this.closedDate;
    }

    public PunchItem closedDate(Instant closedDate) {
        this.setClosedDate(closedDate);
        return this;
    }

    public void setClosedDate(Instant closedDate) {
        this.closedDate = closedDate;
    }

    public TypePunch getType() {
        return this.type;
    }

    public void setType(TypePunch typePunch) {
        this.type = typePunch;
    }

    public PunchItem type(TypePunch typePunch) {
        this.setType(typePunch);
        return this;
    }

    public Line getLine() {
        return this.line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public PunchItem line(Line line) {
        this.setLine(line);
        return this;
    }

    public PunchList getPunchList() {
        return this.punchList;
    }

    public void setPunchList(PunchList punchList) {
        this.punchList = punchList;
    }

    public PunchItem punchList(PunchList punchList) {
        this.setPunchList(punchList);
        return this;
    }

    public PriorityPunch getPriority() {
        return this.priority;
    }

    public void setPriority(PriorityPunch priorityPunch) {
        this.priority = priorityPunch;
    }

    public PunchItem priority(PriorityPunch priorityPunch) {
        this.setPriority(priorityPunch);
        return this;
    }

    public Company getExecutor() {
        return this.executor;
    }

    public void setExecutor(Company company) {
        this.executor = company;
    }

    public PunchItem executor(Company company) {
        this.setExecutor(company);
        return this;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public PunchItem author(User user) {
        this.setAuthor(user);
        return this;
    }

    public Set<CommentPunch> getComments() {
        return this.comments;
    }

    public void setComments(Set<CommentPunch> commentPunches) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setPunchItem(null));
        }
        if (commentPunches != null) {
            commentPunches.forEach(i -> i.setPunchItem(this));
        }
        this.comments = commentPunches;
    }

    public PunchItem comments(Set<CommentPunch> commentPunches) {
        this.setComments(commentPunches);
        return this;
    }

    public PunchItem addComments(CommentPunch commentPunch) {
        this.comments.add(commentPunch);
        commentPunch.setPunchItem(this);
        return this;
    }

    public PunchItem removeComments(CommentPunch commentPunch) {
        this.comments.remove(commentPunch);
        commentPunch.setPunchItem(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PunchItem)) {
            return false;
        }
        return id != null && id.equals(((PunchItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PunchItem{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", location='" + getLocation() + "'" +
            ", description='" + getDescription() + "'" +
            ", revisionDrawing='" + getRevisionDrawing() + "'" +
            ", status='" + getStatus() + "'" +
            ", closedDate='" + getClosedDate() + "'" +
            "}";
    }
}
