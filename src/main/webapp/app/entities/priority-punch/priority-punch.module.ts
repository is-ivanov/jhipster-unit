import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PriorityPunchComponent } from './list/priority-punch.component';
import { PriorityPunchDetailComponent } from './detail/priority-punch-detail.component';
import { PriorityPunchUpdateComponent } from './update/priority-punch-update.component';
import { PriorityPunchDeleteDialogComponent } from './delete/priority-punch-delete-dialog.component';
import { PriorityPunchRoutingModule } from './route/priority-punch-routing.module';

@NgModule({
  imports: [SharedModule, PriorityPunchRoutingModule],
  declarations: [PriorityPunchComponent, PriorityPunchDetailComponent, PriorityPunchUpdateComponent, PriorityPunchDeleteDialogComponent],
  entryComponents: [PriorityPunchDeleteDialogComponent],
})
export class PriorityPunchModule {}
