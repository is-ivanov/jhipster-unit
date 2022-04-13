import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBlock } from '../block.model';
import { BlockService } from '../service/block.service';

@Component({
  templateUrl: './block-delete-dialog.component.html',
})
export class BlockDeleteDialogComponent {
  block?: IBlock;

  constructor(protected blockService: BlockService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.blockService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
