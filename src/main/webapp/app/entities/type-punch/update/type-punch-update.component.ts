import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITypePunch, TypePunch } from '../type-punch.model';
import { TypePunchService } from '../service/type-punch.service';

@Component({
  selector: 'jhi-type-punch-update',
  templateUrl: './type-punch-update.component.html',
})
export class TypePunchUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(20)]],
    description: [],
  });

  constructor(protected typePunchService: TypePunchService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typePunch }) => {
      this.updateForm(typePunch);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typePunch = this.createFromForm();
    if (typePunch.id !== undefined) {
      this.subscribeToSaveResponse(this.typePunchService.update(typePunch));
    } else {
      this.subscribeToSaveResponse(this.typePunchService.create(typePunch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypePunch>>): void {
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

  protected updateForm(typePunch: ITypePunch): void {
    this.editForm.patchValue({
      id: typePunch.id,
      name: typePunch.name,
      description: typePunch.description,
    });
  }

  protected createFromForm(): ITypePunch {
    return {
      ...new TypePunch(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
