import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategorie } from '../categorie.model';
import { CategorieService } from '../service/categorie.service';
import { CategorieDeleteDialogComponent } from '../delete/categorie-delete-dialog.component';

@Component({
  selector: 'jhi-categorie',
  templateUrl: './categorie.component.html',
})
export class CategorieComponent implements OnInit {
  categories?: ICategorie[];
  isLoading = false;

  constructor(protected categorieService: CategorieService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.categorieService.query().subscribe(
      (res: HttpResponse<ICategorie[]>) => {
        this.isLoading = false;
        this.categories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICategorie): number {
    return item.id!;
  }

  delete(categorie: ICategorie): void {
    const modalRef = this.modalService.open(CategorieDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.categorie = categorie;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
