import * as dayjs from 'dayjs';
import { IMedicament } from 'app/entities/medicament/medicament.model';

export interface IVente {
  id?: number;
  date?: dayjs.Dayjs | null;
  nombre?: number | null;
  medicament?: IMedicament | null;
}

export class Vente implements IVente {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public nombre?: number | null,
    public medicament?: IMedicament | null
  ) {}
}

export function getVenteIdentifier(vente: IVente): number | undefined {
  return vente.id;
}
