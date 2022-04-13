import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypePunchComponent } from './list/type-punch.component';
import { TypePunchDetailComponent } from './detail/type-punch-detail.component';
import { TypePunchUpdateComponent } from './update/type-punch-update.component';
import { TypePunchDeleteDialogComponent } from './delete/type-punch-delete-dialog.component';
import { TypePunchRoutingModule } from './route/type-punch-routing.module';

@NgModule({
  imports: [SharedModule, TypePunchRoutingModule],
  declarations: [TypePunchComponent, TypePunchDetailComponent, TypePunchUpdateComponent, TypePunchDeleteDialogComponent],
  entryComponents: [TypePunchDeleteDialogComponent],
})
export class TypePunchModule {}
