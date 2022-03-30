package by.ivanov.unit.service.criteria;

import by.ivanov.unit.domain.enumeration.StatusPunch;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link by.ivanov.unit.domain.PunchItem} entity. This class is used
 * in {@link by.ivanov.unit.web.rest.PunchItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /punch-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class PunchItemCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatusPunch
     */
    public static class StatusPunchFilter extends Filter<StatusPunch> {

        public StatusPunchFilter() {}

        public StatusPunchFilter(StatusPunchFilter filter) {
            super(filter);
        }

        @Override
        public StatusPunchFilter copy() {
            return new StatusPunchFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter number;

    private StringFilter location;

    private StringFilter description;

    private StringFilter revisionDrawing;

    private StatusPunchFilter status;

    private InstantFilter closedDate;

    private LongFilter typeId;

    private LongFilter lineId;

    private LongFilter punchListId;

    private LongFilter priorityId;

    private LongFilter executorId;

    private LongFilter authorId;

    private LongFilter commentsId;

    private Boolean distinct;

    public PunchItemCriteria() {}

    public PunchItemCriteria(PunchItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.revisionDrawing = other.revisionDrawing == null ? null : other.revisionDrawing.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.closedDate = other.closedDate == null ? null : other.closedDate.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.lineId = other.lineId == null ? null : other.lineId.copy();
        this.punchListId = other.punchListId == null ? null : other.punchListId.copy();
        this.priorityId = other.priorityId == null ? null : other.priorityId.copy();
        this.executorId = other.executorId == null ? null : other.executorId.copy();
        this.authorId = other.authorId == null ? null : other.authorId.copy();
        this.commentsId = other.commentsId == null ? null : other.commentsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PunchItemCriteria copy() {
        return new PunchItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getNumber() {
        return number;
    }

    public IntegerFilter number() {
        if (number == null) {
            number = new IntegerFilter();
        }
        return number;
    }

    public void setNumber(IntegerFilter number) {
        this.number = number;
    }

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getRevisionDrawing() {
        return revisionDrawing;
    }

    public StringFilter revisionDrawing() {
        if (revisionDrawing == null) {
            revisionDrawing = new StringFilter();
        }
        return revisionDrawing;
    }

    public void setRevisionDrawing(StringFilter revisionDrawing) {
        this.revisionDrawing = revisionDrawing;
    }

    public StatusPunchFilter getStatus() {
        return status;
    }

    public StatusPunchFilter status() {
        if (status == null) {
            status = new StatusPunchFilter();
        }
        return status;
    }

    public void setStatus(StatusPunchFilter status) {
        this.status = status;
    }

    public InstantFilter getClosedDate() {
        return closedDate;
    }

    public InstantFilter closedDate() {
        if (closedDate == null) {
            closedDate = new InstantFilter();
        }
        return closedDate;
    }

    public void setClosedDate(InstantFilter closedDate) {
        this.closedDate = closedDate;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public LongFilter typeId() {
        if (typeId == null) {
            typeId = new LongFilter();
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getLineId() {
        return lineId;
    }

    public LongFilter lineId() {
        if (lineId == null) {
            lineId = new LongFilter();
        }
        return lineId;
    }

    public void setLineId(LongFilter lineId) {
        this.lineId = lineId;
    }

    public LongFilter getPunchListId() {
        return punchListId;
    }

    public LongFilter punchListId() {
        if (punchListId == null) {
            punchListId = new LongFilter();
        }
        return punchListId;
    }

    public void setPunchListId(LongFilter punchListId) {
        this.punchListId = punchListId;
    }

    public LongFilter getPriorityId() {
        return priorityId;
    }

    public LongFilter priorityId() {
        if (priorityId == null) {
            priorityId = new LongFilter();
        }
        return priorityId;
    }

    public void setPriorityId(LongFilter priorityId) {
        this.priorityId = priorityId;
    }

    public LongFilter getExecutorId() {
        return executorId;
    }

    public LongFilter executorId() {
        if (executorId == null) {
            executorId = new LongFilter();
        }
        return executorId;
    }

    public void setExecutorId(LongFilter executorId) {
        this.executorId = executorId;
    }

    public LongFilter getAuthorId() {
        return authorId;
    }

    public LongFilter authorId() {
        if (authorId == null) {
            authorId = new LongFilter();
        }
        return authorId;
    }

    public void setAuthorId(LongFilter authorId) {
        this.authorId = authorId;
    }

    public LongFilter getCommentsId() {
        return commentsId;
    }

    public LongFilter commentsId() {
        if (commentsId == null) {
            commentsId = new LongFilter();
        }
        return commentsId;
    }

    public void setCommentsId(LongFilter commentsId) {
        this.commentsId = commentsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PunchItemCriteria that = (PunchItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(number, that.number) &&
            Objects.equals(location, that.location) &&
            Objects.equals(description, that.description) &&
            Objects.equals(revisionDrawing, that.revisionDrawing) &&
            Objects.equals(status, that.status) &&
            Objects.equals(closedDate, that.closedDate) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(lineId, that.lineId) &&
            Objects.equals(punchListId, that.punchListId) &&
            Objects.equals(priorityId, that.priorityId) &&
            Objects.equals(executorId, that.executorId) &&
            Objects.equals(authorId, that.authorId) &&
            Objects.equals(commentsId, that.commentsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            number,
            location,
            description,
            revisionDrawing,
            status,
            closedDate,
            typeId,
            lineId,
            punchListId,
            priorityId,
            executorId,
            authorId,
            commentsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PunchItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (revisionDrawing != null ? "revisionDrawing=" + revisionDrawing + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (closedDate != null ? "closedDate=" + closedDate + ", " : "") +
            (typeId != null ? "typeId=" + typeId + ", " : "") +
            (lineId != null ? "lineId=" + lineId + ", " : "") +
            (punchListId != null ? "punchListId=" + punchListId + ", " : "") +
            (priorityId != null ? "priorityId=" + priorityId + ", " : "") +
            (executorId != null ? "executorId=" + executorId + ", " : "") +
            (authorId != null ? "authorId=" + authorId + ", " : "") +
            (commentsId != null ? "commentsId=" + commentsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
