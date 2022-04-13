import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypePunch } from '../type-punch.model';
import { TypePunchService } from '../service/type-punch.service';

@Component({
  templateUrl: './type-punch-delete-dialog.component.html',
})
export class TypePunchDeleteDialogComponent {
  typePunch?: ITypePunch;

  constructor(protected typePunchService: TypePunchService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typePunchService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
