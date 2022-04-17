package by.ivanov.unit.service.criteria;

import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link by.ivanov.unit.domain.PunchList} entity. This class is used
 * in {@link by.ivanov.unit.web.rest.PunchListResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /punch-lists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class PunchListCriteria implements Serializable, Criteria {

	private static final long serialVersionUID = 1L;

	private LongFilter id;

	private IntegerFilter number;

	private StringFilter name;

	private StringFilter description;

	private LongFilter projectId;

	private StringFilter authorLastName;

	private StringFilter companyShortName;

	private Boolean distinct;

	public PunchListCriteria() {
	}

	public PunchListCriteria(PunchListCriteria other) {
		this.id = other.id == null ? null : other.id.copy();
		this.number = other.number == null ? null : other.number.copy();
		this.name = other.name == null ? null : other.name.copy();
		this.description = other.description == null ? null : other.description.copy();
		this.projectId = other.projectId == null ? null : other.projectId.copy();
		this.authorLastName = other.authorLastName == null ? null : other.authorLastName.copy();
		this.companyShortName = other.companyShortName == null ? null : other.companyShortName.copy();
		this.distinct = other.distinct;
	}

	@Override
	public PunchListCriteria copy() {
		return new PunchListCriteria(this);
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

	public StringFilter getName() {
		return name;
	}

	public StringFilter name() {
		if (name == null) {
			name = new StringFilter();
		}
		return name;
	}

	public void setName(StringFilter name) {
		this.name = name;
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

	public LongFilter getProjectId() {
		return projectId;
	}

	public LongFilter projectId() {
		if (projectId == null) {
			projectId = new LongFilter();
		}
		return projectId;
	}

	public void setAuthorLastName(StringFilter authorLastName) {
		this.authorLastName = authorLastName;
	}

	public StringFilter getAuthorLastName() {
		return authorLastName;
	}

	public StringFilter authorLastName() {
		if (authorLastName == null) {
			authorLastName = new StringFilter();
		}
		return authorLastName;
	}

	public void setCompanyShortName(StringFilter companyShortName) {
		this.companyShortName = companyShortName;
	}

	public StringFilter getCompanyShortName() {
		return companyShortName;
	}

	public StringFilter companyShortName() {
		if (companyShortName == null) {
			companyShortName = new StringFilter();
		}
		return companyShortName;
	}

	public void setProjectId(LongFilter projectId) {
		this.projectId = projectId;
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
		final PunchListCriteria that = (PunchListCriteria) o;
		return (
			Objects.equals(id, that.id) &&
				Objects.equals(number, that.number) &&
				Objects.equals(name, that.name) &&
				Objects.equals(description, that.description) &&
				Objects.equals(projectId, that.projectId) &&
				Objects.equals(authorLastName, that.authorLastName) &&
				Objects.equals(companyShortName, that.companyShortName) &&
				Objects.equals(distinct, that.distinct)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, number, name, description, projectId, authorLastName, companyShortName, distinct);
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "PunchListCriteria{" +
			(id != null ? "id=" + id + ", " : "") +
			(number != null ? "number=" + number + ", " : "") +
			(name != null ? "name=" + name + ", " : "") +
			(description != null ? "description=" + description + ", " : "") +
			(projectId != null ? "projectId=" + projectId + ", " : "") +
			(authorLastName != null ? "authorLastName=" + authorLastName + ", " : "") +
			(companyShortName != null ? "companyShortName=" + projectId + ", " : "") +
			(distinct != null ? "distinct=" + distinct + ", " : "") +
			"}";
	}
}
