import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPunchList, PunchList } from '../punch-list.model';
import { PunchListService } from '../service/punch-list.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';

@Component({
  selector: 'jhi-punch-list-update',
  templateUrl: './punch-list-update.component.html',
})
export class PunchListUpdateComponent implements OnInit {
  isSaving = false;

  projectsSharedCollection: IProject[] = [];

  editForm = this.fb.group({
    id: [],
    number: [null, [Validators.required]],
    name: [null, [Validators.maxLength(100)]],
    description: [],
    project: [null, Validators.required],
  });

  constructor(
    protected punchListService: PunchListService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ punchList }) => {
      this.updateForm(punchList);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const punchList = this.createFromForm();
    if (punchList.id !== undefined) {
      this.subscribeToSaveResponse(this.punchListService.update(punchList));
    } else {
      this.subscribeToSaveResponse(this.punchListService.create(punchList));
    }
  }

  trackProjectById(_index: number, item: IProject): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPunchList>>): void {
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

  protected updateForm(punchList: IPunchList): void {
    this.editForm.patchValue({
      id: punchList.id,
      number: punchList.number,
      name: punchList.name,
      description: punchList.description,
      project: punchList.project,
    });

    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing(this.projectsSharedCollection, punchList.project);
  }

  protected loadRelationshipsOptions(): void {
    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(
        map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing(projects, this.editForm.get('project')!.value))
      )
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));
  }

  protected createFromForm(): IPunchList {
    return {
      ...new PunchList(),
      id: this.editForm.get(['id'])!.value,
      number: this.editForm.get(['number'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      project: this.editForm.get(['project'])!.value,
    };
  }
}
