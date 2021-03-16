import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICweList, CweList } from '../cwe-list.model';
import { CweListService } from '../service/cwe-list.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-cwe-list-update',
  templateUrl: './cwe-list-update.component.html',
})
export class CweListUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    cweId: [],
    description: [],
    code: [],
    tags: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected cweListService: CweListService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cweList }) => {
      this.updateForm(cweList);
    });
  }

  updateForm(cweList: ICweList): void {
    this.editForm.patchValue({
      id: cweList.id,
      cweId: cweList.cweId,
      description: cweList.description,
      code: cweList.code,
      tags: cweList.tags,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('cwedatasetApp.error', { message: err.message })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cweList = this.createFromForm();
    if (cweList.id !== undefined) {
      this.subscribeToSaveResponse(this.cweListService.update(cweList));
    } else {
      this.subscribeToSaveResponse(this.cweListService.create(cweList));
    }
  }

  private createFromForm(): ICweList {
    return {
      ...new CweList(),
      id: this.editForm.get(['id'])!.value,
      cweId: this.editForm.get(['cweId'])!.value,
      description: this.editForm.get(['description'])!.value,
      code: this.editForm.get(['code'])!.value,
      tags: this.editForm.get(['tags'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICweList>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
