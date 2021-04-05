import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'medicament',
        data: { pageTitle: 'Medicaments' },
        loadChildren: () => import('./medicament/medicament.module').then(m => m.MedicamentModule),
      },
      {
        path: 'categorie',
        data: { pageTitle: 'Categories' },
        loadChildren: () => import('./categorie/categorie.module').then(m => m.CategorieModule),
      },
      {
        path: 'vente',
        data: { pageTitle: 'Ventes' },
        loadChildren: () => import('./vente/vente.module').then(m => m.VenteModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
