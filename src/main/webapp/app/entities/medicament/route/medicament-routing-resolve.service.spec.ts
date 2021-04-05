jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMedicament, Medicament } from '../medicament.model';
import { MedicamentService } from '../service/medicament.service';

import { MedicamentRoutingResolveService } from './medicament-routing-resolve.service';

describe('Service Tests', () => {
  describe('Medicament routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MedicamentRoutingResolveService;
    let service: MedicamentService;
    let resultMedicament: IMedicament | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MedicamentRoutingResolveService);
      service = TestBed.inject(MedicamentService);
      resultMedicament = undefined;
    });

    describe('resolve', () => {
      it('should return IMedicament returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicament = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedicament).toEqual({ id: 123 });
      });

      it('should return new IMedicament if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicament = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMedicament).toEqual(new Medicament());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicament = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedicament).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
