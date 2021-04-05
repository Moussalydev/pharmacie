import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sujet } from 'app/entities/enumerations/sujet.model';
import { IMedicament, Medicament } from '../medicament.model';

import { MedicamentService } from './medicament.service';

describe('Service Tests', () => {
  describe('Medicament Service', () => {
    let service: MedicamentService;
    let httpMock: HttpTestingController;
    let elemDefault: IMedicament;
    let expectedResult: IMedicament | IMedicament[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MedicamentService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
        prix: 0,
        cible: Sujet.Enfant,
        stock: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Medicament', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Medicament()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Medicament', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prix: 1,
            cible: 'BBBBBB',
            stock: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Medicament', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
            prix: 1,
          },
          new Medicament()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Medicament', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prix: 1,
            cible: 'BBBBBB',
            stock: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Medicament', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMedicamentToCollectionIfMissing', () => {
        it('should add a Medicament to an empty array', () => {
          const medicament: IMedicament = { id: 123 };
          expectedResult = service.addMedicamentToCollectionIfMissing([], medicament);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(medicament);
        });

        it('should not add a Medicament to an array that contains it', () => {
          const medicament: IMedicament = { id: 123 };
          const medicamentCollection: IMedicament[] = [
            {
              ...medicament,
            },
            { id: 456 },
          ];
          expectedResult = service.addMedicamentToCollectionIfMissing(medicamentCollection, medicament);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Medicament to an array that doesn't contain it", () => {
          const medicament: IMedicament = { id: 123 };
          const medicamentCollection: IMedicament[] = [{ id: 456 }];
          expectedResult = service.addMedicamentToCollectionIfMissing(medicamentCollection, medicament);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(medicament);
        });

        it('should add only unique Medicament to an array', () => {
          const medicamentArray: IMedicament[] = [{ id: 123 }, { id: 456 }, { id: 99330 }];
          const medicamentCollection: IMedicament[] = [{ id: 123 }];
          expectedResult = service.addMedicamentToCollectionIfMissing(medicamentCollection, ...medicamentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const medicament: IMedicament = { id: 123 };
          const medicament2: IMedicament = { id: 456 };
          expectedResult = service.addMedicamentToCollectionIfMissing([], medicament, medicament2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(medicament);
          expect(expectedResult).toContain(medicament2);
        });

        it('should accept null and undefined values', () => {
          const medicament: IMedicament = { id: 123 };
          expectedResult = service.addMedicamentToCollectionIfMissing([], null, medicament, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(medicament);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
