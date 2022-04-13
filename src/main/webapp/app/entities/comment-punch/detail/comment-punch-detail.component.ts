import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommentPunch } from '../comment-punch.model';

@Component({
  selector: 'jhi-comment-punch-detail',
  templateUrl: './comment-punch-detail.component.html',
})
export class CommentPunchDetailComponent implements OnInit {
  commentPunch: ICommentPunch | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commentPunch }) => {
      this.commentPunch = commentPunch;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
