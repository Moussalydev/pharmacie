import { ICategorie } from 'app/entities/categorie/categorie.model';
import { IUser } from 'app/entities/user/user.model';
import { Sujet } from 'app/entities/enumerations/sujet.model';

export interface IMedicament {
  id?: number;
  nom?: string;
  prix?: number;
  cible?: Sujet | null;
  stock?: number;
  categorie?: ICategorie | null;
  user?: IUser | null;
}

export class Medicament implements IMedicament {
  constructor(
    public id?: number,
    public nom?: string,
    public prix?: number,
    public cible?: Sujet | null,
    public stock?: number,
    public categorie?: ICategorie | null,
    public user?: IUser | null
  ) {}
}

export function getMedicamentIdentifier(medicament: IMedicament): number | undefined {
  return medicament.id;
}
