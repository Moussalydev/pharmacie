import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MedicamentComponent } from '../list/medicament.component';
import { MedicamentDetailComponent } from '../detail/medicament-detail.component';
import { MedicamentUpdateComponent } from '../update/medicament-update.component';
import { MedicamentRoutingResolveService } from './medicament-routing-resolve.service';

const medicamentRoute: Routes = [
  {
    path: '',
    component: MedicamentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicamentDetailComponent,
    resolve: {
      medicament: MedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicamentUpdateComponent,
    resolve: {
      medicament: MedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicamentUpdateComponent,
    resolve: {
      medicament: MedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(medicamentRoute)],
  exports: [RouterModule],
})
export class MedicamentRoutingModule {}
