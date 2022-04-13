import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPriorityPunch } from '../priority-punch.model';
import { PriorityPunchService } from '../service/priority-punch.service';
import { PriorityPunchDeleteDialogComponent } from '../delete/priority-punch-delete-dialog.component';

@Component({
  selector: 'jhi-priority-punch',
  templateUrl: './priority-punch.component.html',
})
export class PriorityPunchComponent implements OnInit {
  priorityPunches?: IPriorityPunch[];
  isLoading = false;

  constructor(protected priorityPunchService: PriorityPunchService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.priorityPunchService.query().subscribe({
      next: (res: HttpResponse<IPriorityPunch[]>) => {
        this.isLoading = false;
        this.priorityPunches = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPriorityPunch): number {
    return item.id!;
  }

  delete(priorityPunch: IPriorityPunch): void {
    const modalRef = this.modalService.open(PriorityPunchDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.priorityPunch = priorityPunch;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
