import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypePunch } from '../type-punch.model';

@Component({
  selector: 'jhi-type-punch-detail',
  templateUrl: './type-punch-detail.component.html',
})
export class TypePunchDetailComponent implements OnInit {
  typePunch: ITypePunch | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typePunch }) => {
      this.typePunch = typePunch;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
