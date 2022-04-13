import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypePunch } from '../type-punch.model';
import { TypePunchService } from '../service/type-punch.service';
import { TypePunchDeleteDialogComponent } from '../delete/type-punch-delete-dialog.component';

@Component({
  selector: 'jhi-type-punch',
  templateUrl: './type-punch.component.html',
})
export class TypePunchComponent implements OnInit {
  typePunches?: ITypePunch[];
  isLoading = false;

  constructor(protected typePunchService: TypePunchService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.typePunchService.query().subscribe({
      next: (res: HttpResponse<ITypePunch[]>) => {
        this.isLoading = false;
        this.typePunches = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITypePunch): number {
    return item.id!;
  }

  delete(typePunch: ITypePunch): void {
    const modalRef = this.modalService.open(TypePunchDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.typePunch = typePunch;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
