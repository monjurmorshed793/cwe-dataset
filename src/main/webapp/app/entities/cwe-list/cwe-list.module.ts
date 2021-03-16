import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CweListComponent } from './list/cwe-list.component';
import { CweListDetailComponent } from './detail/cwe-list-detail.component';
import { CweListUpdateComponent } from './update/cwe-list-update.component';
import { CweListDeleteDialogComponent } from './delete/cwe-list-delete-dialog.component';
import { CweListRoutingModule } from './route/cwe-list-routing.module';

@NgModule({
  imports: [SharedModule, CweListRoutingModule],
  declarations: [CweListComponent, CweListDetailComponent, CweListUpdateComponent, CweListDeleteDialogComponent],
  entryComponents: [CweListDeleteDialogComponent],
})
export class CweListModule {}
