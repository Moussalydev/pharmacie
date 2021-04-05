export interface ICategorie {
  id?: number;
  nom?: string;
}

export class Categorie implements ICategorie {
  constructor(public id?: number, public nom?: string) {}
}

export function getCategorieIdentifier(categorie: ICategorie): number | undefined {
  return categorie.id;
}
