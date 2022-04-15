import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPunchList } from '../punch-list.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { PunchListService } from '../service/punch-list.service';
import { PunchListDeleteDialogComponent } from '../delete/punch-list-delete-dialog.component';
import { DropdownDataService } from '../../../shared/dropdown-data.service';

@Component({
	selector: 'jhi-punch-list',
	templateUrl: './punch-list.component.html'
})
export class PunchListComponent implements OnInit, OnDestroy {
	punchLists?: IPunchList[];
	isLoading = false;
	totalItems = 0;
	itemsPerPage = ITEMS_PER_PAGE;
	page?: number;
	predicate!: string;
	ascending!: boolean;
	ngbPaginationPage = 1;

	filterNumber?: number;
	filterName?: string;
	filterDescription?: string;
	filterProjectId?: number;

	projectNotifierSubscription: Subscription = this.dropdownDataService.projectNotifier.subscribe((projectId) => {
		this.filterProjectId = projectId;
		this.loadPage(1);
	});

	constructor(
		protected punchListService: PunchListService,
		protected activatedRoute: ActivatedRoute,
		protected router: Router,
		protected modalService: NgbModal,
		protected dropdownDataService: DropdownDataService
	) {
	}

	loadPage(page?: number, dontNavigate?: boolean): void {
		this.isLoading = true;
		const pageToLoad: number = page ?? this.page ?? 1;

		const req = {};
		Object.assign(req, { page: pageToLoad - 1 });
		Object.assign(req, { size: this.itemsPerPage });
		Object.assign(req, { sort: this.sort() });

		this.addFiltersParam(req);

		this.punchListService.query(req).subscribe({
			next: (res: HttpResponse<IPunchList[]>) => {
				this.isLoading = false;
				this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
			},
			error: () => {
				this.isLoading = false;
				this.onError();
			}
		});
	}

	ngOnInit(): void {
		this.handleNavigation();
	}

	trackId(_index: number, item: IPunchList): number {
		return item.id!;
	}

	delete(punchList: IPunchList): void {
		const modalRef = this.modalService.open(PunchListDeleteDialogComponent, {
			size: 'lg',
			backdrop: 'static'
		});
		modalRef.componentInstance.punchList = punchList;
		// unsubscribe not needed because closed completes on modal close
		modalRef.closed.subscribe(reason => {
			if (reason === 'deleted') {
				this.loadPage();
			}
		});
	}

	clearFilter(): void {
		this.filterNumber = undefined;
		this.filterName = undefined;
		this.filterDescription = undefined;
		this.filterProjectId = undefined;
		this.loadPage();
	}

	ngOnDestroy(): void {
		this.projectNotifierSubscription.unsubscribe();
	}

	protected sort(): string[] {
		const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
		if (this.predicate !== 'id') {
			result.push('id');
		}
		return result;
	}

	protected handleNavigation(): void {
		combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
			const page = params.get('page');
			const pageNumber = +(page ?? 1);
			const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
			const predicate = sort[0];
			const ascending = sort[1] === ASC;

			this.getFilterParamsFromRoute(params);

			if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
				this.predicate = predicate;
				this.ascending = ascending;
				this.loadPage(pageNumber, true);
			}
		});
	}

	protected onSuccess(data: IPunchList[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
		this.totalItems = Number(headers.get('X-Total-Count'));
		this.page = page;
		if (navigate) {
			this.router.navigate(['/punch-list'], {
				queryParams: this.prepareQueryParam(),
			});
		}
		this.punchLists = data ?? [];
		this.ngbPaginationPage = this.page;
	}

	protected onError(): void {
		this.ngbPaginationPage = this.page ?? 1;
	}

	protected prepareQueryParam(): {} {
		const param = {};
		Object.assign(param, { page: this.page });
		Object.assign(param, { size: this.itemsPerPage });
		Object.assign(param, { sort: this.predicate + ',' + (this.ascending ? ASC : DESC) });
		this.addFiltersParam(param);
		return param;
	}

	private addFiltersParam(param: {}): void {
		if (this.filterNumber) {
			Object.assign(param, { 'number.equals': this.filterNumber });
		}
		if (this.filterName) {
			Object.assign(param, { 'name.contains': this.filterName });
		}
		if (this.filterDescription) {
			Object.assign(param, { 'description.contains': this.filterDescription });
		}
		if (this.filterProjectId) {
			Object.assign(param, { 'projectId.equals': this.filterProjectId });
		}
	}

	private getFilterParamsFromRoute(params: ParamMap): void {
		const number = params.get('number.equals');
		this.filterNumber = number !== null ? +number : undefined;

		this.filterName = params.get('name.contains') ?? undefined;
		this.filterDescription = params.get('description.contains') ?? undefined;

		const projectId = params.get('projectId.equals');
		this.filterProjectId = projectId !== null ? +projectId : undefined;
	}
}
