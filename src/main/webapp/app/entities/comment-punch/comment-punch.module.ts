import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommentPunchComponent } from './list/comment-punch.component';
import { CommentPunchDetailComponent } from './detail/comment-punch-detail.component';
import { CommentPunchUpdateComponent } from './update/comment-punch-update.component';
import { CommentPunchDeleteDialogComponent } from './delete/comment-punch-delete-dialog.component';
import { CommentPunchRoutingModule } from './route/comment-punch-routing.module';

@NgModule({
  imports: [SharedModule, CommentPunchRoutingModule],
  declarations: [CommentPunchComponent, CommentPunchDetailComponent, CommentPunchUpdateComponent, CommentPunchDeleteDialogComponent],
  entryComponents: [CommentPunchDeleteDialogComponent],
})
export class CommentPunchModule {}
