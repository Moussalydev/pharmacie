import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MedicamentComponent } from './list/medicament.component';
import { MedicamentDetailComponent } from './detail/medicament-detail.component';
import { MedicamentUpdateComponent } from './update/medicament-update.component';
import { MedicamentDeleteDialogComponent } from './delete/medicament-delete-dialog.component';
import { MedicamentRoutingModule } from './route/medicament-routing.module';

@NgModule({
  imports: [SharedModule, MedicamentRoutingModule],
  declarations: [MedicamentComponent, MedicamentDetailComponent, MedicamentUpdateComponent, MedicamentDeleteDialogComponent],
  entryComponents: [MedicamentDeleteDialogComponent],
})
export class MedicamentModule {}
