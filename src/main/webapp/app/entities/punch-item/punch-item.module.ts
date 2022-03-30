import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PunchItemComponent } from './list/punch-item.component';
import { PunchItemDetailComponent } from './detail/punch-item-detail.component';
import { PunchItemUpdateComponent } from './update/punch-item-update.component';
import { PunchItemDeleteDialogComponent } from './delete/punch-item-delete-dialog.component';
import { PunchItemRoutingModule } from './route/punch-item-routing.module';

@NgModule({
  imports: [SharedModule, PunchItemRoutingModule],
  declarations: [PunchItemComponent, PunchItemDetailComponent, PunchItemUpdateComponent, PunchItemDeleteDialogComponent],
  entryComponents: [PunchItemDeleteDialogComponent],
})
export class PunchItemModule {}
