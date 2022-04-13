import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommentPunch, CommentPunch } from '../comment-punch.model';
import { CommentPunchService } from '../service/comment-punch.service';
import { IPunchItem } from 'app/entities/punch-item/punch-item.model';
import { PunchItemService } from 'app/entities/punch-item/service/punch-item.service';

@Component({
  selector: 'jhi-comment-punch-update',
  templateUrl: './comment-punch-update.component.html',
})
export class CommentPunchUpdateComponent implements OnInit {
  isSaving = false;

  punchItemsSharedCollection: IPunchItem[] = [];

  editForm = this.fb.group({
    id: [],
    comment: [null, [Validators.required]],
    punchItem: [null, Validators.required],
  });

  constructor(
    protected commentPunchService: CommentPunchService,
    protected punchItemService: PunchItemService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commentPunch }) => {
      this.updateForm(commentPunch);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commentPunch = this.createFromForm();
    if (commentPunch.id !== undefined) {
      this.subscribeToSaveResponse(this.commentPunchService.update(commentPunch));
    } else {
      this.subscribeToSaveResponse(this.commentPunchService.create(commentPunch));
    }
  }

  trackPunchItemById(_index: number, item: IPunchItem): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommentPunch>>): void {
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

  protected updateForm(commentPunch: ICommentPunch): void {
    this.editForm.patchValue({
      id: commentPunch.id,
      comment: commentPunch.comment,
      punchItem: commentPunch.punchItem,
    });

    this.punchItemsSharedCollection = this.punchItemService.addPunchItemToCollectionIfMissing(
      this.punchItemsSharedCollection,
      commentPunch.punchItem
    );
  }

  protected loadRelationshipsOptions(): void {
    this.punchItemService
      .query()
      .pipe(map((res: HttpResponse<IPunchItem[]>) => res.body ?? []))
      .pipe(
        map((punchItems: IPunchItem[]) =>
          this.punchItemService.addPunchItemToCollectionIfMissing(punchItems, this.editForm.get('punchItem')!.value)
        )
      )
      .subscribe((punchItems: IPunchItem[]) => (this.punchItemsSharedCollection = punchItems));
  }

  protected createFromForm(): ICommentPunch {
    return {
      ...new CommentPunch(),
      id: this.editForm.get(['id'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      punchItem: this.editForm.get(['punchItem'])!.value,
    };
  }
}
