package software.cstl.service;

import software.cstl.domain.Employee;
import software.cstl.domain.LeaveApplication;
import software.cstl.domain.enumeration.LeaveApplicationStatus;
import software.cstl.repository.LeaveApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link LeaveApplication}.
 */
@Service
@Transactional
public class LeaveApplicationService {

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationService.class);

    private final LeaveApplicationRepository leaveApplicationRepository;

    public LeaveApplicationService(LeaveApplicationRepository leaveApplicationRepository) {
        this.leaveApplicationRepository = leaveApplicationRepository;
    }

    /**
     * Save a leaveApplication.
     *
     * @param leaveApplication the entity to save.
     * @return the persisted entity.
     */
    public LeaveApplication save(LeaveApplication leaveApplication) {
        log.debug("Request to save LeaveApplication : {}", leaveApplication);
        return leaveApplicationRepository.save(leaveApplication);
    }

    /**
     * Get all the leaveApplications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveApplication> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveApplications");
        return leaveApplicationRepository.findAll(pageable);
    }


    /**
     * Get one leaveApplication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeaveApplication> findOne(Long id) {
        log.debug("Request to get LeaveApplication : {}", id);
        return leaveApplicationRepository.findById(id);
    }

    /**
     * Delete the leaveApplication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LeaveApplication : {}", id);
        leaveApplicationRepository.deleteById(id);
    }

    /**
     * Get alll leaveApplications.
     *
     * @return the list of entities.
     */
    public List<LeaveApplication> findAll() {
        return leaveApplicationRepository.findAll();
    }

    /**
     * Get list of leaveApplications.
     *
     * @param startDate the startDate.
     * @param endDate the endDate.
     * @param status the status.
     * @return the list of entities.
     */
    public List<LeaveApplication> findAll(LocalDate startDate, LocalDate endDate, LeaveApplicationStatus status) {
        return findAll().stream()
                        .filter(leaveApplication -> (leaveApplication.getFrom().isAfter(startDate) && leaveApplication.getFrom().isBefore(endDate) && leaveApplication.getStatus().equals(status)))
                        .collect(Collectors.toList());
    }

    /**
     * Get list of leaveApplications.
     *
     * @param employee the employee.
     * @param status the status.
     * @return the list of entities.
     */
    public List<LeaveApplication> findAll(Employee employee, LeaveApplicationStatus status) {
        return leaveApplicationRepository.findAllByEmployeeEqualsAndStatusEquals(employee, status);
    }
}
