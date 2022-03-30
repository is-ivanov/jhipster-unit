import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPriorityPunch } from '../priority-punch.model';
import { PriorityPunchService } from '../service/priority-punch.service';

@Component({
  templateUrl: './priority-punch-delete-dialog.component.html',
})
export class PriorityPunchDeleteDialogComponent {
  priorityPunch?: IPriorityPunch;

  constructor(protected priorityPunchService: PriorityPunchService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.priorityPunchService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
