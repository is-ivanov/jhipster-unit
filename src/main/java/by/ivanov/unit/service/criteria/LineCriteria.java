package by.ivanov.unit.service.criteria;

import by.ivanov.unit.domain.enumeration.StatusLine;
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
 * Criteria class for the {@link by.ivanov.unit.domain.Line} entity. This class is used
 * in {@link by.ivanov.unit.web.rest.LineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class LineCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatusLine
     */
    public static class StatusLineFilter extends Filter<StatusLine> {

        public StatusLineFilter() {}

        public StatusLineFilter(StatusLineFilter filter) {
            super(filter);
        }

        @Override
        public StatusLineFilter copy() {
            return new StatusLineFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tag;

    private StringFilter revision;

    private StatusLineFilter status;

    private LongFilter blockId;

    private Boolean distinct;

    public LineCriteria() {}

    public LineCriteria(LineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tag = other.tag == null ? null : other.tag.copy();
        this.revision = other.revision == null ? null : other.revision.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.blockId = other.blockId == null ? null : other.blockId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LineCriteria copy() {
        return new LineCriteria(this);
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

    public StringFilter getTag() {
        return tag;
    }

    public StringFilter tag() {
        if (tag == null) {
            tag = new StringFilter();
        }
        return tag;
    }

    public void setTag(StringFilter tag) {
        this.tag = tag;
    }

    public StringFilter getRevision() {
        return revision;
    }

    public StringFilter revision() {
        if (revision == null) {
            revision = new StringFilter();
        }
        return revision;
    }

    public void setRevision(StringFilter revision) {
        this.revision = revision;
    }

    public StatusLineFilter getStatus() {
        return status;
    }

    public StatusLineFilter status() {
        if (status == null) {
            status = new StatusLineFilter();
        }
        return status;
    }

    public void setStatus(StatusLineFilter status) {
        this.status = status;
    }

    public LongFilter getBlockId() {
        return blockId;
    }

    public LongFilter blockId() {
        if (blockId == null) {
            blockId = new LongFilter();
        }
        return blockId;
    }

    public void setBlockId(LongFilter blockId) {
        this.blockId = blockId;
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
        final LineCriteria that = (LineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tag, that.tag) &&
            Objects.equals(revision, that.revision) &&
            Objects.equals(status, that.status) &&
            Objects.equals(blockId, that.blockId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag, revision, status, blockId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tag != null ? "tag=" + tag + ", " : "") +
            (revision != null ? "revision=" + revision + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (blockId != null ? "blockId=" + blockId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
