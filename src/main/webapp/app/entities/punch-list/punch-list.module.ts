import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PunchListComponent } from './list/punch-list.component';
import { PunchListDetailComponent } from './detail/punch-list-detail.component';
import { PunchListUpdateComponent } from './update/punch-list-update.component';
import { PunchListDeleteDialogComponent } from './delete/punch-list-delete-dialog.component';
import { PunchListRoutingModule } from './route/punch-list-routing.module';

@NgModule({
  imports: [SharedModule, PunchListRoutingModule],
  declarations: [PunchListComponent, PunchListDetailComponent, PunchListUpdateComponent, PunchListDeleteDialogComponent],
  entryComponents: [PunchListDeleteDialogComponent],
})
export class PunchListModule {}
