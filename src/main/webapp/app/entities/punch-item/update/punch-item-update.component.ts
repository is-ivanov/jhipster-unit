import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPunchItem, PunchItem } from '../punch-item.model';
import { PunchItemService } from '../service/punch-item.service';
import { ITypePunch } from 'app/entities/type-punch/type-punch.model';
import { TypePunchService } from 'app/entities/type-punch/service/type-punch.service';
import { ILine } from 'app/entities/line/line.model';
import { LineService } from 'app/entities/line/service/line.service';
import { IPunchList } from 'app/entities/punch-list/punch-list.model';
import { PunchListService } from 'app/entities/punch-list/service/punch-list.service';
import { IPriorityPunch } from 'app/entities/priority-punch/priority-punch.model';
import { PriorityPunchService } from 'app/entities/priority-punch/service/priority-punch.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { StatusPunch } from 'app/entities/enumerations/status-punch.model';

@Component({
  selector: 'jhi-punch-item-update',
  templateUrl: './punch-item-update.component.html',
})
export class PunchItemUpdateComponent implements OnInit {
  isSaving = false;
  statusPunchValues = Object.keys(StatusPunch);

  typePunchesSharedCollection: ITypePunch[] = [];
  linesSharedCollection: ILine[] = [];
  punchListsSharedCollection: IPunchList[] = [];
  priorityPunchesSharedCollection: IPriorityPunch[] = [];
  companiesSharedCollection: ICompany[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    number: [null, [Validators.required]],
    location: [],
    description: [null, [Validators.required]],
    revisionDrawing: [null, [Validators.maxLength(20)]],
    status: [null, [Validators.required]],
    closedDate: [],
    type: [null, Validators.required],
    line: [],
    punchList: [null, Validators.required],
    priority: [null, Validators.required],
    executor: [],
    author: [null, Validators.required],
  });

  constructor(
    protected punchItemService: PunchItemService,
    protected typePunchService: TypePunchService,
    protected lineService: LineService,
    protected punchListService: PunchListService,
    protected priorityPunchService: PriorityPunchService,
    protected companyService: CompanyService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ punchItem }) => {
      if (punchItem.id === undefined) {
        const today = dayjs().startOf('day');
        punchItem.closedDate = today;
      }

      this.updateForm(punchItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const punchItem = this.createFromForm();
    if (punchItem.id !== undefined) {
      this.subscribeToSaveResponse(this.punchItemService.update(punchItem));
    } else {
      this.subscribeToSaveResponse(this.punchItemService.create(punchItem));
    }
  }

  trackTypePunchById(_index: number, item: ITypePunch): number {
    return item.id!;
  }

  trackLineById(_index: number, item: ILine): number {
    return item.id!;
  }

  trackPunchListById(_index: number, item: IPunchList): number {
    return item.id!;
  }

  trackPriorityPunchById(_index: number, item: IPriorityPunch): number {
    return item.id!;
  }

  trackCompanyById(_index: number, item: ICompany): number {
    return item.id!;
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPunchItem>>): void {
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

  protected updateForm(punchItem: IPunchItem): void {
    this.editForm.patchValue({
      id: punchItem.id,
      number: punchItem.number,
      location: punchItem.location,
      description: punchItem.description,
      revisionDrawing: punchItem.revisionDrawing,
      status: punchItem.status,
      closedDate: punchItem.closedDate ? punchItem.closedDate.format(DATE_TIME_FORMAT) : null,
      type: punchItem.type,
      line: punchItem.line,
      punchList: punchItem.punchList,
      priority: punchItem.priority,
      executor: punchItem.executor,
      author: punchItem.author,
    });

    this.typePunchesSharedCollection = this.typePunchService.addTypePunchToCollectionIfMissing(
      this.typePunchesSharedCollection,
      punchItem.type
    );
    this.linesSharedCollection = this.lineService.addLineToCollectionIfMissing(this.linesSharedCollection, punchItem.line);
    this.punchListsSharedCollection = this.punchListService.addPunchListToCollectionIfMissing(
      this.punchListsSharedCollection,
      punchItem.punchList
    );
    this.priorityPunchesSharedCollection = this.priorityPunchService.addPriorityPunchToCollectionIfMissing(
      this.priorityPunchesSharedCollection,
      punchItem.priority
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(
      this.companiesSharedCollection,
      punchItem.executor
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, punchItem.author);
  }

  protected loadRelationshipsOptions(): void {
    this.typePunchService
      .query()
      .pipe(map((res: HttpResponse<ITypePunch[]>) => res.body ?? []))
      .pipe(
        map((typePunches: ITypePunch[]) =>
          this.typePunchService.addTypePunchToCollectionIfMissing(typePunches, this.editForm.get('type')!.value)
        )
      )
      .subscribe((typePunches: ITypePunch[]) => (this.typePunchesSharedCollection = typePunches));

    this.lineService
      .query()
      .pipe(map((res: HttpResponse<ILine[]>) => res.body ?? []))
      .pipe(map((lines: ILine[]) => this.lineService.addLineToCollectionIfMissing(lines, this.editForm.get('line')!.value)))
      .subscribe((lines: ILine[]) => (this.linesSharedCollection = lines));

    this.punchListService
      .query()
      .pipe(map((res: HttpResponse<IPunchList[]>) => res.body ?? []))
      .pipe(
        map((punchLists: IPunchList[]) =>
          this.punchListService.addPunchListToCollectionIfMissing(punchLists, this.editForm.get('punchList')!.value)
        )
      )
      .subscribe((punchLists: IPunchList[]) => (this.punchListsSharedCollection = punchLists));

    this.priorityPunchService
      .query()
      .pipe(map((res: HttpResponse<IPriorityPunch[]>) => res.body ?? []))
      .pipe(
        map((priorityPunches: IPriorityPunch[]) =>
          this.priorityPunchService.addPriorityPunchToCollectionIfMissing(priorityPunches, this.editForm.get('priority')!.value)
        )
      )
      .subscribe((priorityPunches: IPriorityPunch[]) => (this.priorityPunchesSharedCollection = priorityPunches));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('executor')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('author')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IPunchItem {
    return {
      ...new PunchItem(),
      id: this.editForm.get(['id'])!.value,
      number: this.editForm.get(['number'])!.value,
      location: this.editForm.get(['location'])!.value,
      description: this.editForm.get(['description'])!.value,
      revisionDrawing: this.editForm.get(['revisionDrawing'])!.value,
      status: this.editForm.get(['status'])!.value,
      closedDate: this.editForm.get(['closedDate'])!.value ? dayjs(this.editForm.get(['closedDate'])!.value, DATE_TIME_FORMAT) : undefined,
      type: this.editForm.get(['type'])!.value,
      line: this.editForm.get(['line'])!.value,
      punchList: this.editForm.get(['punchList'])!.value,
      priority: this.editForm.get(['priority'])!.value,
      executor: this.editForm.get(['executor'])!.value,
      author: this.editForm.get(['author'])!.value,
    };
  }
}
