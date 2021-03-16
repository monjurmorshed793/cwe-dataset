import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICweList } from '../cwe-list.model';
import { CweListService } from '../service/cwe-list.service';

@Component({
  templateUrl: './cwe-list-delete-dialog.component.html',
})
export class CweListDeleteDialogComponent {
  cweList?: ICweList;

  constructor(protected cweListService: CweListService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cweListService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
