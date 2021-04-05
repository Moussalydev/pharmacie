import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVente, Vente } from '../vente.model';
import { VenteService } from '../service/vente.service';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';

@Component({
  selector: 'jhi-vente-update',
  templateUrl: './vente-update.component.html',
})
export class VenteUpdateComponent implements OnInit {
  isSaving = false;

  medicamentsSharedCollection: IMedicament[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    nombre: [],
    medicament: [],
  });

  constructor(
    protected venteService: VenteService,
    protected medicamentService: MedicamentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vente }) => {
      if (vente.id === undefined) {
        const today = dayjs().startOf('day');
        vente.date = today;
      }

      this.updateForm(vente);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vente = this.createFromForm();
    if (vente.id !== undefined) {
      this.subscribeToSaveResponse(this.venteService.update(vente));
    } else {
      this.subscribeToSaveResponse(this.venteService.create(vente));
    }
  }

  trackMedicamentById(index: number, item: IMedicament): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVente>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vente: IVente): void {
    this.editForm.patchValue({
      id: vente.id,
      date: vente.date ? vente.date.format(DATE_TIME_FORMAT) : null,
      nombre: vente.nombre,
      medicament: vente.medicament,
    });

    this.medicamentsSharedCollection = this.medicamentService.addMedicamentToCollectionIfMissing(
      this.medicamentsSharedCollection,
      vente.medicament
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicamentService
      .query()
      .pipe(map((res: HttpResponse<IMedicament[]>) => res.body ?? []))
      .pipe(
        map((medicaments: IMedicament[]) =>
          this.medicamentService.addMedicamentToCollectionIfMissing(medicaments, this.editForm.get('medicament')!.value)
        )
      )
      .subscribe((medicaments: IMedicament[]) => (this.medicamentsSharedCollection = medicaments));
  }

  protected createFromForm(): IVente {
    return {
      ...new Vente(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      nombre: this.editForm.get(['nombre'])!.value,
      medicament: this.editForm.get(['medicament'])!.value,
    };
  }
}
