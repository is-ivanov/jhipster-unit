import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILine, Line } from '../line.model';
import { LineService } from '../service/line.service';
import { IBlock } from 'app/entities/block/block.model';
import { BlockService } from 'app/entities/block/service/block.service';
import { StatusLine } from 'app/entities/enumerations/status-line.model';

@Component({
  selector: 'jhi-line-update',
  templateUrl: './line-update.component.html',
})
export class LineUpdateComponent implements OnInit {
  isSaving = false;
  statusLineValues = Object.keys(StatusLine);

  blocksSharedCollection: IBlock[] = [];

  editForm = this.fb.group({
    id: [],
    tag: [null, [Validators.required, Validators.maxLength(50)]],
    revision: [null, [Validators.required, Validators.maxLength(20)]],
    status: [null, [Validators.required]],
    block: [null, Validators.required],
  });

  constructor(
    protected lineService: LineService,
    protected blockService: BlockService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ line }) => {
      this.updateForm(line);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const line = this.createFromForm();
    if (line.id !== undefined) {
      this.subscribeToSaveResponse(this.lineService.update(line));
    } else {
      this.subscribeToSaveResponse(this.lineService.create(line));
    }
  }

  trackBlockById(index: number, item: IBlock): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILine>>): void {
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

  protected updateForm(line: ILine): void {
    this.editForm.patchValue({
      id: line.id,
      tag: line.tag,
      revision: line.revision,
      status: line.status,
      block: line.block,
    });

    this.blocksSharedCollection = this.blockService.addBlockToCollectionIfMissing(this.blocksSharedCollection, line.block);
  }

  protected loadRelationshipsOptions(): void {
    this.blockService
      .query()
      .pipe(map((res: HttpResponse<IBlock[]>) => res.body ?? []))
      .pipe(map((blocks: IBlock[]) => this.blockService.addBlockToCollectionIfMissing(blocks, this.editForm.get('block')!.value)))
      .subscribe((blocks: IBlock[]) => (this.blocksSharedCollection = blocks));
  }

  protected createFromForm(): ILine {
    return {
      ...new Line(),
      id: this.editForm.get(['id'])!.value,
      tag: this.editForm.get(['tag'])!.value,
      revision: this.editForm.get(['revision'])!.value,
      status: this.editForm.get(['status'])!.value,
      block: this.editForm.get(['block'])!.value,
    };
  }
}
