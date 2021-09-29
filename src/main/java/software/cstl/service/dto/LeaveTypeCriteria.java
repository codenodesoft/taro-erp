package software.cstl.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import software.cstl.domain.enumeration.LeaveTypeName;
import software.cstl.domain.enumeration.LeaveTypeStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link software.cstl.domain.LeaveType} entity. This class is used
 * in {@link software.cstl.web.rest.LeaveTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /leave-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaveTypeCriteria implements Serializable, Criteria {
    /**
     * Class for filtering LeaveTypeName
     */
    public static class LeaveTypeNameFilter extends Filter<LeaveTypeName> {

        public LeaveTypeNameFilter() {
        }

        public LeaveTypeNameFilter(LeaveTypeNameFilter filter) {
            super(filter);
        }

        @Override
        public LeaveTypeNameFilter copy() {
            return new LeaveTypeNameFilter(this);
        }

    }
    /**
     * Class for filtering LeaveTypeStatus
     */
    public static class LeaveTypeStatusFilter extends Filter<LeaveTypeStatus> {

        public LeaveTypeStatusFilter() {
        }

        public LeaveTypeStatusFilter(LeaveTypeStatusFilter filter) {
            super(filter);
        }

        @Override
        public LeaveTypeStatusFilter copy() {
            return new LeaveTypeStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LeaveTypeNameFilter name;

    private BigDecimalFilter totalDays;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LeaveTypeStatusFilter status;

    public LeaveTypeCriteria() {
    }

    public LeaveTypeCriteria(LeaveTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.totalDays = other.totalDays == null ? null : other.totalDays.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.status = other.status == null ? null : other.status.copy();
    }

    @Override
    public LeaveTypeCriteria copy() {
        return new LeaveTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LeaveTypeNameFilter getName() {
        return name;
    }

    public void setName(LeaveTypeNameFilter name) {
        this.name = name;
    }

    public BigDecimalFilter getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(BigDecimalFilter totalDays) {
        this.totalDays = totalDays;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public LeaveTypeStatusFilter getStatus() {
        return status;
    }

    public void setStatus(LeaveTypeStatusFilter status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LeaveTypeCriteria that = (LeaveTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(totalDays, that.totalDays) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        totalDays,
        startDate,
        endDate,
        status
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (totalDays != null ? "totalDays=" + totalDays + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
            "}";
    }

}
