import { Component, OnInit, ViewChild } from '@angular/core';
import { IDesignation } from 'app/shared/model/designation.model';
import { DesignationService } from 'app/entities/designation/designation.service';
import { combineLatest, merge, Observable, Subject } from 'rxjs';
import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { debounceTime, distinctUntilChanged, filter, map } from 'rxjs/operators';
import { Select2Data } from 'ng-select2-component';
import { MonthlySalaryService } from 'app/entities/monthly-salary/monthly-salary.service';
import { MonthlySalaryDtlService } from 'app/entities/monthly-salary-dtl/monthly-salary-dtl.service';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IMonthlySalary, MonthlySalary } from 'app/shared/model/monthly-salary.model';
import { IMonthlySalaryDtl } from 'app/shared/model/monthly-salary-dtl.model';
import { EmployeeService } from 'app/entities/employee/employee.service';
import { MonthType } from 'app/shared/model/enumerations/month-type.model';
import { Moment } from 'moment';
import { PayrollManagementService } from 'app/app-components/payroll-management/payroll-management.service';
import { ActivatedRoute, ActivatedRouteSnapshot, Data, ParamMap, Router } from '@angular/router';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import * as moment from 'moment';
import { IDepartment } from 'app/shared/model/department.model';
import { DepartmentService } from 'app/entities/department/department.service';
import { PayrollReportType } from 'app/shared/model/enumerations/payroll-report-type';

@Component({
  selector: 'jhi-payroll-management',
  templateUrl: './payroll-management.component.html',
  styleUrls: ['./payroll-management.component.scss'],
})
export class PayrollManagementComponent implements OnInit {
  years: number[] = [];
  selectedYear?: number;
  departments: IDepartment[] = [];
  selectedDepartmentId?: number;
  selectedDepartment?: IDepartment;
  monthlySalary!: IMonthlySalary;
  monthlySalaryDtls: IMonthlySalaryDtl[] = [];
  selectedMonth?: MonthType;
  fromDate?: Moment;
  toDate?: Moment;
  fromDateStr?: string;
  toDateStr?: string;
  showMonthlySalaryDtl = false;
  showLoader = false;

  constructor(
    private departmentService: DepartmentService,
    private monthlySalaryService: MonthlySalaryService,
    private monthlySalaryDtlService: MonthlySalaryDtlService,
    private jhiAlertService: JhiAlertService,
    private employeeService: EmployeeService,
    private payrollManagementService: PayrollManagementService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private eventManager: JhiEventManager
  ) {}

  ngOnInit(): void {
    this.configureYears();
    this.departmentService.query({ size: 20000 }).subscribe(res => {
      this.departments = res.body!;
    });
    this.handleNavigation();
  }

  public departmentIdChanged(): void {
    this.departmentService.find(this.selectedDepartmentId!).subscribe(res => {
      this.selectedDepartment = res.body!;
    });
  }

  private handleNavigation(): void {
    this.activatedRoute.params.subscribe(params => {
      this.selectedYear = +params['selectedYear'];
      this.selectedMonth = params['selectedMonth'];
      this.selectedDepartmentId = +params['selectedDepartmentId'];
      if (this.selectedDepartmentId) {
        this.departmentService.find(this.selectedDepartmentId).subscribe(res => {
          this.selectedDepartment = res.body!;
          this.fetch();
        });
      }
    });
  }

  configureYears(): void {
    let year = new Date().getFullYear();
    this.selectedYear = new Date().getFullYear();
    this.years.push(year);
    for (let i = 0; i < 3; i++) {
      year -= 1;
      this.years.push(year);
    }
  }

  navigate(): void {
    if (this.selectedYear && this.selectedDepartment && this.selectedMonth) {
      this.selectedDepartmentId = this.selectedDepartment.id;
      this.router.navigate(['/payroll-management', this.selectedYear, this.selectedMonth, this.selectedDepartmentId]).then(() => {
        this.fetch();
      });
    }
  }

  fetch(): void {
    this.showLoader = true;
    if (this.selectedYear && this.selectedDepartment && this.selectedMonth) {
      this.monthlySalaryService
        .query({
          'year.equals': this.selectedYear,
          'departmentId.equals': this.selectedDepartmentId,
          'month.equals': this.selectedMonth.valueOf(),
        })
        .subscribe(res => {
          if (res.body?.length! > 0) {
            this.monthlySalary = res.body ? res.body[0]! : new MonthlySalary();
            this.showMonthlySalaryDtl = true;
            this.showLoader = false;
            this.router.navigate(['monthly-salary-dtl'], {
              queryParams: { monthlySalaryId: this.monthlySalary.id },
              relativeTo: this.activatedRoute,
            });
          } else {
            this.showMonthlySalaryDtl = false;
            const monthlySalary = new MonthlySalary();
            monthlySalary.year = this.selectedYear;
            monthlySalary.department = this.selectedDepartment;
            monthlySalary.month = this.selectedMonth;
            this.payrollManagementService.createEmptySalaries(monthlySalary).subscribe(response => {
              this.showLoader = false;
              this.fetchExistingData();
            });
          }
        });
    } else {
      this.jhiAlertService.error('Year and Designation must be selected');
    }
  }

  generateSalaries(): void {
    this.showLoader = true;
    this.payrollManagementService.createSalaries(this.monthlySalary).subscribe(
      res => {
        this.eventManager.broadcast('monthlySalaryDtlListModification');
        this.navigate();
        this.showLoader = false;
      },
      err => {
        this.jhiAlertService.error('Error in generating salaries.');
        this.showLoader = false;
      }
    );
  }

  regenerateSalaries(): void {
    // this.monthlySalary.fromDate = moment(this.fromDate, DATE_TIME_FORMAT);
    // this.monthlySalary.toDate = moment(this.toDate, DATE_TIME_FORMAT);
    this.showLoader = true;
    this.payrollManagementService.regenerateSalaries(this.monthlySalary).subscribe(
      res => {
        this.monthlySalary = res.body!;
        this.jhiAlertService.success('Re-generation success');
        this.eventManager.broadcast('monthlySalaryDtlListModification');
        this.showLoader = false;
        this.ngOnInit();
      },
      err => {
        this.showLoader = false;
        this.jhiAlertService.error('Error in re-generating salaries');
      }
    );
  }

  fetchExistingData(): void {
    this.monthlySalaryService
      .query({
        'year.equals': this.selectedYear,
        'departmentId.equals': this.selectedDepartment?.id!,
        'month.equals': this.selectedMonth,
      })
      .subscribe(res => {
        this.monthlySalary = res.body ? res.body[0]! : new MonthlySalary();
        this.showMonthlySalaryDtl = true;
        this.router.navigate(['monthly-salary-dtl'], {
          queryParams: { monthlySalaryId: this.monthlySalary.id },
          relativeTo: this.activatedRoute,
        });
      });
  }

  downloadReport(): void {
    this.monthlySalaryService.downloadSalaryReport(this.monthlySalary.id!).subscribe(data => {
      const file = new Blob([data], { type: 'application/pdf' });
      const pdfData = URL.createObjectURL(file);
      const link = document.createElement('a');
      link.href = pdfData;
      link.download = 'salary-report.pdf';
      link.click();
    });
  }

  downloadDepositSlips(): void {
    this.payrollManagementService.downloadEmployeePaySlipForAll(this.monthlySalary.id!, 'WITH_FULL_CALCULATION').subscribe(data => {
      const file = new Blob([data], { type: 'application/octet-stream' });
      const pdfData = URL.createObjectURL(file);
      const link = document.createElement('a');
      link.href = pdfData;
      link.download = 'pay-slip-all.xls';
      link.click();
    });
  }
}
