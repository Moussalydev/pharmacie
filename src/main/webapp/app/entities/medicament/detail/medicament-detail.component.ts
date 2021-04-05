import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMedicament } from '../medicament.model';

@Component({
  selector: 'jhi-medicament-detail',
  templateUrl: './medicament-detail.component.html',
})
export class MedicamentDetailComponent implements OnInit {
  medicament: IMedicament | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicament }) => {
      this.medicament = medicament;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
