jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VenteService } from '../service/vente.service';
import { IVente, Vente } from '../vente.model';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';

import { VenteUpdateComponent } from './vente-update.component';

describe('Component Tests', () => {
  describe('Vente Management Update Component', () => {
    let comp: VenteUpdateComponent;
    let fixture: ComponentFixture<VenteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let venteService: VenteService;
    let medicamentService: MedicamentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VenteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VenteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VenteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      venteService = TestBed.inject(VenteService);
      medicamentService = TestBed.inject(MedicamentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Medicament query and add missing value', () => {
        const vente: IVente = { id: 456 };
        const medicament: IMedicament = { id: 94926 };
        vente.medicament = medicament;

        const medicamentCollection: IMedicament[] = [{ id: 84361 }];
        spyOn(medicamentService, 'query').and.returnValue(of(new HttpResponse({ body: medicamentCollection })));
        const additionalMedicaments = [medicament];
        const expectedCollection: IMedicament[] = [...additionalMedicaments, ...medicamentCollection];
        spyOn(medicamentService, 'addMedicamentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ vente });
        comp.ngOnInit();

        expect(medicamentService.query).toHaveBeenCalled();
        expect(medicamentService.addMedicamentToCollectionIfMissing).toHaveBeenCalledWith(medicamentCollection, ...additionalMedicaments);
        expect(comp.medicamentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const vente: IVente = { id: 456 };
        const medicament: IMedicament = { id: 24855 };
        vente.medicament = medicament;

        activatedRoute.data = of({ vente });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vente));
        expect(comp.medicamentsSharedCollection).toContain(medicament);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const vente = { id: 123 };
        spyOn(venteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ vente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vente }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(venteService.update).toHaveBeenCalledWith(vente);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const vente = new Vente();
        spyOn(venteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ vente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vente }));
        saveSubject.complete();

        // THEN
        expect(venteService.create).toHaveBeenCalledWith(vente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const vente = { id: 123 };
        spyOn(venteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ vente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(venteService.update).toHaveBeenCalledWith(vente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMedicamentById', () => {
        it('Should return tracked Medicament primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMedicamentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
