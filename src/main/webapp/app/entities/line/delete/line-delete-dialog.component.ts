import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILine } from '../line.model';
import { LineService } from '../service/line.service';

@Component({
  templateUrl: './line-delete-dialog.component.html',
})
export class LineDeleteDialogComponent {
  line?: ILine;

  constructor(protected lineService: LineService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lineService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
