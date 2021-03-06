/*
package software.cstl.service.mediators;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import software.cstl.domain.*;
import software.cstl.domain.enumeration.ActiveStatus;
import software.cstl.domain.enumeration.MonthType;
import software.cstl.domain.enumeration.WeekDay;
import software.cstl.repository.*;
import software.cstl.repository.extended.EmployeeExtRepository;
import software.cstl.security.SecurityUtils;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class PayrollServiceTest {
    @Mock
    private SecurityContext securityContext;
    @Mock
    private MonthlySalaryRepository monthlySalaryRepository;
    @Mock
    private MonthlySalaryDtlRepository monthlySalaryDtlRepository;
    @Mock
    private DesignationRepository designationRepository;
    @Mock
    private EmployeeExtRepository employeeExtRepository;
    @Mock
    private DefaultAllowanceRepository defaultAllowanceRepository;
    @Mock
    private FineRepository fineRepository;
    @Mock
    private FinePaymentHistoryRepository finePaymentHistoryRepository;
    @Mock
    private AdvanceRepository advanceRepository;
    @Mock
    private AdvancePaymentHistoryRepository advancePaymentHistoryRepository;
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Spy
    @InjectMocks
    private PayrollService payrollService;

    DefaultAllowance defaultAllowance;

    @Nested
    class PartialSalaryTest{


        @Test
        public void assignPartialSalaryAndAllowancesTestWithEqualWorkingDays(){
            PartialSalary partialSalary = new PartialSalary();
            partialSalary.setEmployee(TestConfig.employee());
            partialSalary.setFromDate(Instant.now());
            partialSalary.setToDate(Instant.now());
            partialSalary.setTotalMonthDays(4);

            MockedStatic<SecurityUtils> securityUtilsMockedStatic = Mockito.mockStatic(SecurityUtils.class);
            Optional<String> adminUser = Optional.of("admin");
            securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserLogin).thenReturn(adminUser);
            doReturn(TestConfig.defaultAllowance()).when(defaultAllowanceRepository).findDefaultAllowanceByStatus(ActiveStatus.ACTIVE);
            doReturn(2).when(attendanceRepository).countAttendancesByEmployeeAndAttendanceTimeBetween(partialSalary.getEmployee(), partialSalary.getFromDate(), partialSalary.getToDate());
            doReturn(TestConfig.attendances()).when(attendanceRepository).findAllByEmployeeAndAttendanceTimeBetween(partialSalary.getEmployee(), partialSalary.getFromDate(), partialSalary.getToDate());

            partialSalary = payrollService.assignPartialSalaryAndAllowances(partialSalary);

            assertThat(partialSalary.getGross()).isEqualTo(new BigDecimal(4000));
            assertThat(partialSalary.getBasic()).isEqualTo(new BigDecimal(4100/2));
            assertThat(partialSalary.getHouseRent()).isEqualByComparingTo(new BigDecimal(2050.0/2));
            assertThat(partialSalary.getMedicalAllowance()).isEqualByComparingTo(new BigDecimal(600.0/2));
            assertThat(partialSalary.getConvinceAllowance()).isEqualByComparingTo(new BigDecimal(350.0/2));
            assertThat(partialSalary.getFoodAllowance()).isEqualByComparingTo(new BigDecimal(900.0/2));

            securityUtilsMockedStatic.close();
        }

        @Test
        public void assignPartialSalaryAndAllowancesTestWithEqualWorkingDaysAndEmployeeSalaryAllowances(){
            PartialSalary partialSalary = new PartialSalary();
            partialSalary.setEmployee(TestConfig.employee());
            partialSalary.setFromDate(Instant.now());
            partialSalary.setToDate(Instant.now());
            partialSalary.setTotalMonthDays(4);

            MockedStatic<SecurityUtils> securityUtilsMockedStatic = Mockito.mockStatic(SecurityUtils.class);
            Optional<String> adminUser = Optional.of("admin");
            securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserLogin).thenReturn(adminUser);
            doReturn(TestConfig.defaultAllowance()).when(defaultAllowanceRepository).findDefaultAllowanceByStatus(ActiveStatus.ACTIVE);
            doReturn(2).when(attendanceRepository).countAttendancesByEmployeeAndAttendanceTimeBetween(partialSalary.getEmployee(), partialSalary.getFromDate(), partialSalary.getToDate());
            doReturn(TestConfig.attendancesWithEmployeeSalaryWithAllowance()).when(attendanceRepository).findAllByEmployeeAndAttendanceTimeBetween(partialSalary.getEmployee(), partialSalary.getFromDate(), partialSalary.getToDate());

            partialSalary = payrollService.assignPartialSalaryAndAllowances(partialSalary);

            assertThat(partialSalary.getGross()).isEqualTo(new BigDecimal(4000));
            assertThat(partialSalary.getMedicalAllowance()).isEqualByComparingTo(new BigDecimal(650.0/2));
            securityUtilsMockedStatic.close();

        }

        @Test
        public void assignPartialSalaryAndAllowancesTestWithEqualWorkingDaysAndDifferentEmployeeSalaries(){
            PartialSalary partialSalary = new PartialSalary();
            partialSalary.setEmployee(TestConfig.employee());
            partialSalary.setFromDate(Instant.now());
            partialSalary.setToDate(Instant.now());
            partialSalary.setTotalMonthDays(4);

            MockedStatic<SecurityUtils> securityUtilsMockedStatic = Mockito.mockStatic(SecurityUtils.class);
            Optional<String> adminUser = Optional.of("admin");
            securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserLogin).thenReturn(adminUser);
            doReturn(TestConfig.defaultAllowance()).when(defaultAllowanceRepository).findDefaultAllowanceByStatus(ActiveStatus.ACTIVE);
            doReturn(2).when(attendanceRepository).countAttendancesByEmployeeAndAttendanceTimeBetween(partialSalary.getEmployee(), partialSalary.getFromDate(), partialSalary.getToDate());
            doReturn(TestConfig.attendancesWithDifferentEmployeeSalaries()).when(attendanceRepository).findAllByEmployeeAndAttendanceTimeBetween(partialSalary.getEmployee(), partialSalary.getFromDate(), partialSalary.getToDate());

            partialSalary = payrollService.assignPartialSalaryAndAllowances(partialSalary);

            assertThat(partialSalary.getGross()).isEqualTo(new BigDecimal(4025));
            securityUtilsMockedStatic.close();

        }

        @Test
        public void assignPartialSalaryAndAllowancesTestWithLessWorkingDays(){
            PartialSalary partialSalary = new PartialSalary();
            partialSalary.setEmployee(TestConfig.employee());
            partialSalary.setFromDate(Instant.now());
            partialSalary.setToDate(Instant.now());
            partialSalary.setTotalMonthDays(4);

            MockedStatic<SecurityUtils> securityUtilsMockedStatic = Mockito.mockStatic(SecurityUtils.class);
            Optional<String> adminUser = Optional.of("admin");
            securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserLogin).thenReturn(adminUser);
            doReturn(TestConfig.defaultAllowance()).when(defaultAllowanceRepository).findDefaultAllowanceByStatus(ActiveStatus.ACTIVE);
            doReturn(2).when(attendanceRepository).countAttendancesByEmployeeAndAttendanceTimeBetween(partialSalary.getEmployee(), partialSalary.getFromDate(), partialSalary.getToDate());
            doReturn(TestConfig.singleAttendances()).when(attendanceRepository).findAllByEmployeeAndAttendanceTimeBetween(partialSalary.getEmployee(), partialSalary.getFromDate(), partialSalary.getToDate());

            partialSalary = payrollService.assignPartialSalaryAndAllowances(partialSalary);

            assertThat(partialSalary.getGross()).isEqualTo(new BigDecimal(2000));

            securityUtilsMockedStatic.close();

        }
    }



    static class TestConfig{
        public static DefaultAllowance defaultAllowance(){
            DefaultAllowance defaultAllowance = new DefaultAllowance();
            defaultAllowance.setTotalAllowance(new BigDecimal(1850));
            defaultAllowance.setMedicalAllowance(new BigDecimal(600));
            defaultAllowance.setConvinceAllowance(new BigDecimal(350));
            defaultAllowance.setFoodAllowance(new BigDecimal(900));
            return defaultAllowance;
        }

        public static Employee employee(){
            Employee employee = new Employee();
            employee.setId(1L);
            employee.setLocalId("1001");
            employee.setGlobalId("2001");
            employee.setAttendanceMachineId("3001");
            return employee;
        }

        public static EmployeeSalary employeeSalaryWithoutAllowance(){
            EmployeeSalary employeeSalary = new EmployeeSalary();
            employeeSalary.setEmployee(employee());
            employeeSalary.setGross(new BigDecimal(8000));
            employeeSalary.setBasic(new BigDecimal(4100));
            employeeSalary.setHouseRent(new BigDecimal(2050));
            return employeeSalary;
        }

        public static EmployeeSalary employeeSalaryWithAllowance(){
            EmployeeSalary employeeSalary = new EmployeeSalary();
            employeeSalary.setEmployee(employee());
            employeeSalary.setGross(new BigDecimal(8000));
            employeeSalary.setBasic(new BigDecimal(4100));
            employeeSalary.setHouseRent(new BigDecimal(2050));
            employeeSalary.setMedicalAllowance(new BigDecimal(650));
            return employeeSalary;
        }

        public static EmployeeSalary updatedEmployeeSalaryWithoutAllowance(){
            EmployeeSalary employeeSalary = new EmployeeSalary();
            employeeSalary.setEmployee(employee());
            employeeSalary.setGross(new BigDecimal(8100));
            employeeSalary.setBasic(new BigDecimal(4200));
            employeeSalary.setHouseRent(new BigDecimal(2050));
            return employeeSalary;
        }

        public static List<Attendance> attendances(){
            List<Attendance> attendances = new ArrayList<>();
            Attendance attendance1 = new Attendance();
            attendance1.setEmployee(employee());
            attendance1.setEmployeeSalary(employeeSalaryWithoutAllowance());
            attendance1.setAttendanceTime(Instant.now());

            attendances.add(attendance1);

            Attendance attendance2 = new Attendance();
            attendance2.setEmployee(employee());
            attendance2.setEmployeeSalary(employeeSalaryWithoutAllowance());
            attendance2.setAttendanceTime(Instant.now());
            attendances.add(attendance2);

            return attendances;
        }

        public static List<Attendance> attendancesWithEmployeeSalaryWithAllowance(){
            List<Attendance> attendances = new ArrayList<>();
            Attendance attendance1 = new Attendance();
            attendance1.setEmployee(employee());
            attendance1.setEmployeeSalary(employeeSalaryWithAllowance());
            attendance1.setAttendanceTime(Instant.now());

            attendances.add(attendance1);

            Attendance attendance2 = new Attendance();
            attendance2.setEmployee(employee());
            attendance2.setEmployeeSalary(employeeSalaryWithAllowance());
            attendance2.setAttendanceTime(Instant.now());
            attendances.add(attendance2);

            return attendances;
        }

        public static List<Attendance> attendancesWithDifferentEmployeeSalaries(){
            List<Attendance> attendances = new ArrayList<>();
            Attendance attendance1 = new Attendance();
            attendance1.setEmployee(employee());
            attendance1.setEmployeeSalary(employeeSalaryWithoutAllowance());
            attendance1.setAttendanceTime(Instant.now());

            attendances.add(attendance1);

            Attendance attendance2 = new Attendance();
            attendance2.setEmployee(employee());
            attendance2.setEmployeeSalary(updatedEmployeeSalaryWithoutAllowance());
            attendance2.setAttendanceTime(Instant.now());
            attendances.add(attendance2);

            return attendances;
        }

        public static List<Attendance> singleAttendances(){
            List<Attendance> attendances = new ArrayList<>();
            Attendance attendance1 = new Attendance();
            attendance1.setEmployee(employee());
            attendance1.setEmployeeSalary(employeeSalaryWithoutAllowance());
            attendance1.setAttendanceTime(Instant.now());
            attendances.add(attendance1);
            return attendances;
        }

    }

    @Test
    public void weekendTest(){
        WeekDay weekDay = WeekDay.SATURDAY;
        int weekDayOrdinalValue = 0;
        if (weekDay.equals(WeekDay.MONDAY)) {
            weekDayOrdinalValue=1;
        }else if(weekDay.equals(WeekDay.SUNDAY)){
            weekDayOrdinalValue=7;
        }else{
            weekDayOrdinalValue = weekDay.ordinal();
        }

        YearMonth yearMonth = YearMonth.of(2021, (MonthType.JANUARY.ordinal()+1));
        LocalDate firstDay = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
        LocalDate lastDay = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), yearMonth.lengthOfMonth());
        int totalDays = 0;
        while (!firstDay.isAfter(lastDay)){
            if(firstDay.getDayOfWeek().equals(DayOfWeek.of(weekDayOrdinalValue))){
                totalDays+=1;
                System.out.println(firstDay.getDayOfWeek().toString());
            }
            firstDay = firstDay.plusDays(1);
        }

        System.out.println(totalDays);
    }

    @Test
    public void randomTest(){
        Instant instant = Instant.now();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println(localDate.getYear());
        System.out.println(localDate.getMonth().getValue());
        System.out.println(localDate.getDayOfMonth());
    }

}
*/
