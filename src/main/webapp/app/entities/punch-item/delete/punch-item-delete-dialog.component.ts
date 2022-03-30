import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPunchItem } from '../punch-item.model';
import { PunchItemService } from '../service/punch-item.service';

@Component({
  templateUrl: './punch-item-delete-dialog.component.html',
})
export class PunchItemDeleteDialogComponent {
  punchItem?: IPunchItem;

  constructor(protected punchItemService: PunchItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.punchItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
