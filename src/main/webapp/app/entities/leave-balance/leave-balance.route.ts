import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILeaveBalance, LeaveBalance } from 'app/shared/model/leave-balance.model';
import { LeaveBalanceService } from './leave-balance.service';
import { LeaveBalanceComponent } from './leave-balance.component';
import { LeaveBalanceDetailComponent } from './leave-balance-detail.component';
import { LeaveBalanceUpdateComponent } from './leave-balance-update.component';

@Injectable({ providedIn: 'root' })
export class LeaveBalanceResolve implements Resolve<ILeaveBalance> {
  constructor(private service: LeaveBalanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaveBalance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((leaveBalance: HttpResponse<LeaveBalance>) => {
          if (leaveBalance.body) {
            return of(leaveBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaveBalance());
  }
}

export const leaveBalanceRoute: Routes = [
  {
    path: '',
    component: LeaveBalanceComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'LeaveBalances',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveBalanceDetailComponent,
    resolve: {
      leaveBalance: LeaveBalanceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeaveBalances',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveBalanceUpdateComponent,
    resolve: {
      leaveBalance: LeaveBalanceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeaveBalances',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveBalanceUpdateComponent,
    resolve: {
      leaveBalance: LeaveBalanceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeaveBalances',
    },
    canActivate: [UserRouteAccessService],
  },
];
