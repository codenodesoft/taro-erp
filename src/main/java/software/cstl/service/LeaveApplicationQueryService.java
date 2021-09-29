package software.cstl.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import software.cstl.domain.LeaveApplication;
import software.cstl.domain.*; // for static metamodels
import software.cstl.repository.LeaveApplicationRepository;
import software.cstl.service.dto.LeaveApplicationCriteria;

/**
 * Service for executing complex queries for {@link LeaveApplication} entities in the database.
 * The main input is a {@link LeaveApplicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaveApplication} or a {@link Page} of {@link LeaveApplication} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaveApplicationQueryService extends QueryService<LeaveApplication> {

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationQueryService.class);

    private final LeaveApplicationRepository leaveApplicationRepository;

    public LeaveApplicationQueryService(LeaveApplicationRepository leaveApplicationRepository) {
        this.leaveApplicationRepository = leaveApplicationRepository;
    }

    /**
     * Return a {@link List} of {@link LeaveApplication} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaveApplication> findByCriteria(LeaveApplicationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaveApplication> specification = createSpecification(criteria);
        return leaveApplicationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LeaveApplication} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveApplication> findByCriteria(LeaveApplicationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaveApplication> specification = createSpecification(criteria);
        return leaveApplicationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaveApplicationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaveApplication> specification = createSpecification(criteria);
        return leaveApplicationRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaveApplicationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaveApplication> createSpecification(LeaveApplicationCriteria criteria) {
        Specification<LeaveApplication> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaveApplication_.id));
            }
            if (criteria.getFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFrom(), LeaveApplication_.from));
            }
            if (criteria.getTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTo(), LeaveApplication_.to));
            }
            if (criteria.getTotalDays() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalDays(), LeaveApplication_.totalDays));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), LeaveApplication_.status));
            }
            if (criteria.getAppliedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAppliedBy(), LeaveApplication_.appliedBy));
            }
            if (criteria.getAppliedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAppliedOn(), LeaveApplication_.appliedOn));
            }
            if (criteria.getActionTakenBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getActionTakenBy(), LeaveApplication_.actionTakenBy));
            }
            if (criteria.getActionTakenOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActionTakenOn(), LeaveApplication_.actionTakenOn));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployeeId(),
                    root -> root.join(LeaveApplication_.employee, JoinType.LEFT).get(Employee_.id)));
            }
            if (criteria.getEmpId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmpId(),
                    root -> root.join(LeaveApplication_.employee, JoinType.LEFT).get(Employee_.empId)));
            }
            if (criteria.getLeaveTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getLeaveTypeId(),
                    root -> root.join(LeaveApplication_.leaveType, JoinType.LEFT).get(LeaveType_.id)));
            }
            if (criteria.getDepartmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getDepartmentId(),
                    root -> root.join(LeaveApplication_.department, JoinType.LEFT).get(Department_.id)));
            }
            if (criteria.getDesignationId() != null) {
                specification = specification.and(buildSpecification(criteria.getDesignationId(),
                    root -> root.join(LeaveApplication_.designation, JoinType.LEFT).get(Designation_.id)));
            }
        }
        return specification;
    }
}
