import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPunchList } from '../punch-list.model';
import { PunchListService } from '../service/punch-list.service';

@Component({
  templateUrl: './punch-list-delete-dialog.component.html',
})
export class PunchListDeleteDialogComponent {
  punchList?: IPunchList;

  constructor(protected punchListService: PunchListService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.punchListService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
