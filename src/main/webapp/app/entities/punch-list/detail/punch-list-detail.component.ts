import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPunchList } from '../punch-list.model';

@Component({
  selector: 'jhi-punch-list-detail',
  templateUrl: './punch-list-detail.component.html',
})
export class PunchListDetailComponent implements OnInit {
  punchList: IPunchList | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ punchList }) => {
      this.punchList = punchList;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
