jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MedicamentService } from '../service/medicament.service';

import { MedicamentDeleteDialogComponent } from './medicament-delete-dialog.component';

describe('Component Tests', () => {
  describe('Medicament Management Delete Component', () => {
    let comp: MedicamentDeleteDialogComponent;
    let fixture: ComponentFixture<MedicamentDeleteDialogComponent>;
    let service: MedicamentService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MedicamentDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(MedicamentDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MedicamentDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MedicamentService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
