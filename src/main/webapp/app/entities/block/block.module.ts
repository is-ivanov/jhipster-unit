import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BlockComponent } from './list/block.component';
import { BlockDetailComponent } from './detail/block-detail.component';
import { BlockUpdateComponent } from './update/block-update.component';
import { BlockDeleteDialogComponent } from './delete/block-delete-dialog.component';
import { BlockRoutingModule } from './route/block-routing.module';

@NgModule({
  imports: [SharedModule, BlockRoutingModule],
  declarations: [BlockComponent, BlockDetailComponent, BlockUpdateComponent, BlockDeleteDialogComponent],
  entryComponents: [BlockDeleteDialogComponent],
})
export class BlockModule {}
