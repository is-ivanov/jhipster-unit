import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPriorityPunch } from '../priority-punch.model';

@Component({
  selector: 'jhi-priority-punch-detail',
  templateUrl: './priority-punch-detail.component.html',
})
export class PriorityPunchDetailComponent implements OnInit {
  priorityPunch: IPriorityPunch | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priorityPunch }) => {
      this.priorityPunch = priorityPunch;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
