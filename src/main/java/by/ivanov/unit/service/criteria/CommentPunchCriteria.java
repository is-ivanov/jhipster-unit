package by.ivanov.unit.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link by.ivanov.unit.domain.CommentPunch} entity. This class is used
 * in {@link by.ivanov.unit.web.rest.CommentPunchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comment-punches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CommentPunchCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter comment;

    private LongFilter punchItemId;

    private Boolean distinct;

    public CommentPunchCriteria() {}

    public CommentPunchCriteria(CommentPunchCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.punchItemId = other.punchItemId == null ? null : other.punchItemId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CommentPunchCriteria copy() {
        return new CommentPunchCriteria(this);
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

    public StringFilter getComment() {
        return comment;
    }

    public StringFilter comment() {
        if (comment == null) {
            comment = new StringFilter();
        }
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public LongFilter getPunchItemId() {
        return punchItemId;
    }

    public LongFilter punchItemId() {
        if (punchItemId == null) {
            punchItemId = new LongFilter();
        }
        return punchItemId;
    }

    public void setPunchItemId(LongFilter punchItemId) {
        this.punchItemId = punchItemId;
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
        final CommentPunchCriteria that = (CommentPunchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(punchItemId, that.punchItemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comment, punchItemId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentPunchCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (comment != null ? "comment=" + comment + ", " : "") +
            (punchItemId != null ? "punchItemId=" + punchItemId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
