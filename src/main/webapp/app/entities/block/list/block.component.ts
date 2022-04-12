import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBlock } from '../block.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { BlockService } from '../service/block.service';
import { BlockDeleteDialogComponent } from '../delete/block-delete-dialog.component';
import { ProjectService } from '../../project/service/project.service';
import { DropdownDataService } from '../../../shared/dropdown-data.service';

@Component({
	selector: 'jhi-block',
	templateUrl: './block.component.html',
})
export class BlockComponent implements OnInit {
	blocks?: IBlock[];
	isLoading = false;
	totalItems = 0;
	itemsPerPage = ITEMS_PER_PAGE;
	page?: number;
	predicate!: string;
	ascending!: boolean;
	ngbPaginationPage = 1;

	filterNumber?: number;
	filterDescription?: string;
	filterProjectId?: number;

	projectNotifierSubscription: Subscription = this.dropdownDataService.projectNotifier.subscribe((projectId) => {
		this.filterProjectId = projectId;
		this.loadPage(1);
	});

	constructor(
		protected blockService: BlockService,
		protected activatedRoute: ActivatedRoute,
		protected router: Router,
		protected modalService: NgbModal,
		protected projectService: ProjectService,
		protected dropdownDataService: DropdownDataService
	) {}

	loadPage(page?: number, dontNavigate?: boolean): void {
		this.isLoading = true;
		const pageToLoad: number = page ?? this.page ?? 1;

		const req = {};
		Object.assign(req, { page: pageToLoad - 1 });
		Object.assign(req, { size: this.itemsPerPage });
		Object.assign(req, { sort: this.sort() });

		this.addFiltersParam(req);

		this.blockService.query(req).subscribe({
			next: (res: HttpResponse<IBlock[]>) => {
				this.isLoading = false;
				this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
			},
			error: () => {
				this.isLoading = false;
				this.onError();
			},
		});
	}

	ngOnInit(): void {
		this.handleNavigation();
	}

	trackId(index: number, item: IBlock): number {
		return item.id!;
	}

	delete(block: IBlock): void {
		const modalRef = this.modalService.open(BlockDeleteDialogComponent, {
			size: 'lg',
			backdrop: 'static',
		});
		modalRef.componentInstance.block = block;
		// unsubscribe not needed because closed completes on modal close
		modalRef.closed.subscribe((reason) => {
			if (reason === 'deleted') {
				this.loadPage();
			}
		});
	}

	clearFilter(): void {
		this.filterNumber = undefined;
		this.filterDescription = undefined;
		this.filterProjectId = undefined;
		this.loadPage();
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
			if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
				this.predicate = predicate;
				this.ascending = ascending;
				this.loadPage(pageNumber, true);
			}
		});
	}

	protected onSuccess(data: IBlock[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
		this.totalItems = Number(headers.get('X-Total-Count'));
		this.page = page;
		if (navigate) {
			this.router.navigate(['/block'], {
				queryParams: this.prepareQueryParam(),
			});
		}
		this.blocks = data ?? [];
		this.ngbPaginationPage = this.page;
	}

	protected onError(): void {
		this.ngbPaginationPage = this.page ?? 1;
	}

	protected prepareQueryParam(): any {
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
		if (this.filterDescription) {
			Object.assign(param, { 'description.contains': this.filterDescription });
		}
		if (this.filterProjectId) {
			Object.assign(param, { 'projectId.equals': this.filterProjectId });
		}
	}
}
