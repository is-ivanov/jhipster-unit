import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommentPunch } from '../comment-punch.model';
import { CommentPunchService } from '../service/comment-punch.service';

@Component({
  templateUrl: './comment-punch-delete-dialog.component.html',
})
export class CommentPunchDeleteDialogComponent {
  commentPunch?: ICommentPunch;

  constructor(protected commentPunchService: CommentPunchService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.commentPunchService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
