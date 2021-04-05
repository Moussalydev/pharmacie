import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedicament } from '../medicament.model';
import { MedicamentService } from '../service/medicament.service';

@Component({
  templateUrl: './medicament-delete-dialog.component.html',
})
export class MedicamentDeleteDialogComponent {
  medicament?: IMedicament;

  constructor(protected medicamentService: MedicamentService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicamentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
