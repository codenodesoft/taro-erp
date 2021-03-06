import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILeaveType, LeaveType } from 'app/shared/model/leave-type.model';
import { LeaveTypeService } from './leave-type.service';

@Component({
  selector: 'jhi-leave-type-update',
  templateUrl: './leave-type-update.component.html',
})
export class LeaveTypeUpdateComponent implements OnInit {
  isSaving = false;
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    totalDays: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [],
    status: [null, [Validators.required]],
  });

  constructor(protected leaveTypeService: LeaveTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveType }) => {
      this.updateForm(leaveType);
    });
  }

  updateForm(leaveType: ILeaveType): void {
    this.editForm.patchValue({
      id: leaveType.id,
      name: leaveType.name,
      totalDays: leaveType.totalDays,
      startDate: leaveType.startDate,
      endDate: leaveType.endDate,
      status: leaveType.status,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaveType = this.createFromForm();
    if (leaveType.id !== undefined) {
      this.subscribeToSaveResponse(this.leaveTypeService.update(leaveType));
    } else {
      this.subscribeToSaveResponse(this.leaveTypeService.create(leaveType));
    }
  }

  private createFromForm(): ILeaveType {
    return {
      ...new LeaveType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      totalDays: this.editForm.get(['totalDays'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveType>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
