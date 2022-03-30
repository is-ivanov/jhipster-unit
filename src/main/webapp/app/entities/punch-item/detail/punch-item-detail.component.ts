import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPunchItem } from '../punch-item.model';

@Component({
  selector: 'jhi-punch-item-detail',
  templateUrl: './punch-item-detail.component.html',
})
export class PunchItemDetailComponent implements OnInit {
  punchItem: IPunchItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ punchItem }) => {
      this.punchItem = punchItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
