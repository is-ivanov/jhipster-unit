import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBlock, Block } from '../block.model';
import { BlockService } from '../service/block.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';

@Component({
	selector: 'jhi-block-update',
	templateUrl: './block-update.component.html',
})
export class BlockUpdateComponent implements OnInit {
	isSaving = false;

	projectsSharedCollection: IProject[] = [];

	editForm = this.fb.group({
		id: [],
		number: [null, [Validators.required]],
		description: [null, [Validators.required]],
		project: [null, Validators.required],
	});

	constructor(
		protected blockService: BlockService,
		protected projectService: ProjectService,
		protected activatedRoute: ActivatedRoute,
		protected fb: FormBuilder
	) {}

	ngOnInit(): void {
		this.activatedRoute.data.subscribe(({ block }) => {
			this.updateForm(block);

			this.loadRelationshipsOptions();
		});
	}

	previousState(): void {
		window.history.back();
	}

	save(): void {
		this.isSaving = true;
		const block = this.createFromForm();
		if (block.id !== undefined) {
			this.subscribeToSaveResponse(this.blockService.update(block));
		} else {
			this.subscribeToSaveResponse(this.blockService.create(block));
		}
	}

  trackProjectById(_index: number, item: IProject): number {
    return item.id!;
  }

	protected subscribeToSaveResponse(result: Observable<HttpResponse<IBlock>>): void {
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

	protected updateForm(block: IBlock): void {
		this.editForm.patchValue({
			id: block.id,
			number: block.number,
			description: block.description,
			project: block.project,
		});

		this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing(
			this.projectsSharedCollection,
			block.project
		);
	}

	protected loadRelationshipsOptions(): void {
		this.projectService
			.query({
				eagerload: false,
				sort: ['name,asc'],
			})
			.pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
			.pipe(
				map((projects: IProject[]) =>
					this.projectService.addProjectToCollectionIfMissing(projects, this.editForm.get('project')!.value)
				)
			)
			.subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));
	}

	protected createFromForm(): IBlock {
		return {
			...new Block(),
			id: this.editForm.get(['id'])!.value,
			number: this.editForm.get(['number'])!.value,
			description: this.editForm.get(['description'])!.value,
			project: this.editForm.get(['project'])!.value,
		};
	}
}
