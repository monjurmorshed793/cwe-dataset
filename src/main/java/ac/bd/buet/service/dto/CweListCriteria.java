package ac.bd.buet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ac.bd.buet.domain.CweList} entity. This class is used
 * in {@link ac.bd.buet.web.rest.CweListResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cwe-lists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CweListCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cweId;

    private StringFilter tags;

    public CweListCriteria() {}

    public CweListCriteria(CweListCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cweId = other.cweId == null ? null : other.cweId.copy();
        this.tags = other.tags == null ? null : other.tags.copy();
    }

    @Override
    public CweListCriteria copy() {
        return new CweListCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCweId() {
        return cweId;
    }

    public void setCweId(StringFilter cweId) {
        this.cweId = cweId;
    }

    public StringFilter getTags() {
        return tags;
    }

    public void setTags(StringFilter tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CweListCriteria that = (CweListCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(cweId, that.cweId) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cweId, tags);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CweListCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (cweId != null ? "cweId=" + cweId + ", " : "") +
                (tags != null ? "tags=" + tags + ", " : "") +
            "}";
    }
}
