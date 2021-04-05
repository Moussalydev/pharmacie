import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MedicamentDetailComponent } from './medicament-detail.component';

describe('Component Tests', () => {
  describe('Medicament Management Detail Component', () => {
    let comp: MedicamentDetailComponent;
    let fixture: ComponentFixture<MedicamentDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MedicamentDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ medicament: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MedicamentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MedicamentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load medicament on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.medicament).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
