import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPriorityPunch, PriorityPunch } from '../priority-punch.model';
import { PriorityPunchService } from '../service/priority-punch.service';

@Component({
  selector: 'jhi-priority-punch-update',
  templateUrl: './priority-punch-update.component.html',
})
export class PriorityPunchUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    priority: [null, [Validators.required]],
    name: [null, [Validators.required, Validators.maxLength(20)]],
    description: [],
  });

  constructor(protected priorityPunchService: PriorityPunchService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priorityPunch }) => {
      this.updateForm(priorityPunch);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const priorityPunch = this.createFromForm();
    if (priorityPunch.id !== undefined) {
      this.subscribeToSaveResponse(this.priorityPunchService.update(priorityPunch));
    } else {
      this.subscribeToSaveResponse(this.priorityPunchService.create(priorityPunch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPriorityPunch>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(priorityPunch: IPriorityPunch): void {
    this.editForm.patchValue({
      id: priorityPunch.id,
      priority: priorityPunch.priority,
      name: priorityPunch.name,
      description: priorityPunch.description,
    });
  }

  protected createFromForm(): IPriorityPunch {
    return {
      ...new PriorityPunch(),
      id: this.editForm.get(['id'])!.value,
      priority: this.editForm.get(['priority'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
