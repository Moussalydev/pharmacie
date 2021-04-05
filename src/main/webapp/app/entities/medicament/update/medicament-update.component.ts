import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMedicament, Medicament } from '../medicament.model';
import { MedicamentService } from '../service/medicament.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-medicament-update',
  templateUrl: './medicament-update.component.html',
})
export class MedicamentUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategorie[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    prix: [null, [Validators.required]],
    cible: [],
    stock: [null, [Validators.required]],
    categorie: [],
    user: [],
  });

  constructor(
    protected medicamentService: MedicamentService,
    protected categorieService: CategorieService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicament }) => {
      this.updateForm(medicament);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicament = this.createFromForm();
    if (medicament.id !== undefined) {
      this.subscribeToSaveResponse(this.medicamentService.update(medicament));
    } else {
      this.subscribeToSaveResponse(this.medicamentService.create(medicament));
    }
  }

  trackCategorieById(index: number, item: ICategorie): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicament>>): void {
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

  protected updateForm(medicament: IMedicament): void {
    this.editForm.patchValue({
      id: medicament.id,
      nom: medicament.nom,
      prix: medicament.prix,
      cible: medicament.cible,
      stock: medicament.stock,
      categorie: medicament.categorie,
      user: medicament.user,
    });

    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing(
      this.categoriesSharedCollection,
      medicament.categorie
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, medicament.user);
  }

  protected loadRelationshipsOptions(): void {
    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing(categories, this.editForm.get('categorie')!.value)
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IMedicament {
    return {
      ...new Medicament(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      cible: this.editForm.get(['cible'])!.value,
      stock: this.editForm.get(['stock'])!.value,
      categorie: this.editForm.get(['categorie'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
