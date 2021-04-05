import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicament, getMedicamentIdentifier } from '../medicament.model';

export type EntityResponseType = HttpResponse<IMedicament>;
export type EntityArrayResponseType = HttpResponse<IMedicament[]>;

@Injectable({ providedIn: 'root' })
export class MedicamentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/medicaments');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(medicament: IMedicament): Observable<EntityResponseType> {
    return this.http.post<IMedicament>(this.resourceUrl, medicament, { observe: 'response' });
  }

  update(medicament: IMedicament): Observable<EntityResponseType> {
    return this.http.put<IMedicament>(`${this.resourceUrl}/${getMedicamentIdentifier(medicament) as number}`, medicament, {
      observe: 'response',
    });
  }

  partialUpdate(medicament: IMedicament): Observable<EntityResponseType> {
    return this.http.patch<IMedicament>(`${this.resourceUrl}/${getMedicamentIdentifier(medicament) as number}`, medicament, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedicament>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedicament[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMedicamentToCollectionIfMissing(
    medicamentCollection: IMedicament[],
    ...medicamentsToCheck: (IMedicament | null | undefined)[]
  ): IMedicament[] {
    const medicaments: IMedicament[] = medicamentsToCheck.filter(isPresent);
    if (medicaments.length > 0) {
      const medicamentCollectionIdentifiers = medicamentCollection.map(medicamentItem => getMedicamentIdentifier(medicamentItem)!);
      const medicamentsToAdd = medicaments.filter(medicamentItem => {
        const medicamentIdentifier = getMedicamentIdentifier(medicamentItem);
        if (medicamentIdentifier == null || medicamentCollectionIdentifiers.includes(medicamentIdentifier)) {
          return false;
        }
        medicamentCollectionIdentifiers.push(medicamentIdentifier);
        return true;
      });
      return [...medicamentsToAdd, ...medicamentCollection];
    }
    return medicamentCollection;
  }
}
