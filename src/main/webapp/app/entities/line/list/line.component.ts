import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILine } from '../line.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { LineService } from '../service/line.service';
import { LineDeleteDialogComponent } from '../delete/line-delete-dialog.component';
import { ProjectService } from '../../project/service/project.service';
import { IBlock } from '../../block/block.model';
import { IRevision } from '../../../shared/selectors/revisions/revision.model';
import { DropdownDataService } from '../../../shared/dropdown-data.service';

@Component({
	selector: 'jhi-line',
	templateUrl: './line.component.html',
})
export class LineComponent implements OnInit, OnDestroy {
	lines?: ILine[];
	isLoading = false;
	totalItems = 0;
	itemsPerPage = ITEMS_PER_PAGE;
	page?: number;
	predicate!: string;
	ascending!: boolean;
	ngbPaginationPage = 1;

	filterProjectId?: number;
	filterTag?: string;
	filterRevision?: string[];
	filterStatusLine?: string;
	filterBlockId?: number;
	filterBlocks?: IBlock[];

	projectNotifierSubscription: Subscription = this.dropdownDataService.blocksNotifier.subscribe((blocks) => {
		this.filterBlockId = undefined;
		this.filterBlocks = blocks;
		this.loadPage(1);
	});

	blockNotifierSubscription: Subscription = this.dropdownDataService.blockNotifier.subscribe((blockId) => {
		this.filterBlocks = undefined;
		this.filterBlockId = blockId;
		this.loadPage(1);
	});

	statusLineNotifierSubscription: Subscription = this.dropdownDataService.statusLineNotifier.subscribe((status) => {
		this.filterStatusLine = status;
		this.loadPage(1);
	});

	constructor(
		protected lineService: LineService,
		protected activatedRoute: ActivatedRoute,
		protected router: Router,
		protected modalService: NgbModal,
		protected projectService: ProjectService,
		protected dropdownDataService: DropdownDataService
	) {}

	ngOnInit(): void {
		this.handleNavigation();
	}

	loadPage(page?: number, dontNavigate?: boolean): void {
		this.isLoading = true;
		const pageToLoad: number = page ?? this.page ?? 1;

		const req = {};
		Object.assign(req, { page: pageToLoad - 1 });
		Object.assign(req, { size: this.itemsPerPage });
		Object.assign(req, { sort: this.sort() });

		if (this.filterBlocks) {
			const filterBlockIds: (number | undefined)[] = this.filterBlocks.map((block) => block.id);
			Object.assign(req, { 'blockId.in': filterBlockIds });
		}

		if (this.filterTag) {
			Object.assign(req, { 'tag.contains': this.filterTag });
		}
		if (this.filterRevision && this.filterRevision.length > 0) {
			Object.assign(req, { 'revision.in': this.filterRevision });
		}

		if (this.filterStatusLine) {
			Object.assign(req, { 'status.equals': this.filterStatusLine });
		}
		if (this.filterBlockId) {
			Object.assign(req, { 'blockId.equals': this.filterBlockId });
		}

		this.lineService.query(req).subscribe({
			next: (res: HttpResponse<ILine[]>) => {
				this.isLoading = false;
				this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
			},
			error: () => {
				this.isLoading = false;
				this.onError();
			},
		});
	}

	trackId(index: number, item: ILine): number {
		return item.id!;
	}

	delete(line: ILine): void {
		const modalRef = this.modalService.open(LineDeleteDialogComponent, {
			size: 'lg',
			backdrop: 'static',
		});
		modalRef.componentInstance.line = line;
		// unsubscribe not needed because closed completes on modal close
		modalRef.closed.subscribe((reason) => {
			if (reason === 'deleted') {
				this.loadPage();
			}
		});
	}

	onUpdateFilterRevision(revisions: IRevision[]): void {
		this.filterRevision = [];
		// eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
		if (revisions !== undefined && revisions.length > 0) {
			revisions.forEach((rev) => this.filterRevision?.push(rev.revision));
		}
		this.loadPage(1);
	}

	clearFilter(): void {
		this.filterProjectId = undefined;
		this.filterBlockId = undefined;
		this.filterTag = undefined;
		this.filterRevision = undefined;
		this.filterStatusLine = undefined;
		this.filterBlocks = undefined;
		this.loadPage(1);
	}

	ngOnDestroy(): void {
		this.projectNotifierSubscription.unsubscribe();
		this.blockNotifierSubscription.unsubscribe();
		this.statusLineNotifierSubscription.unsubscribe();
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

	protected onSuccess(data: ILine[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
		this.totalItems = Number(headers.get('X-Total-Count'));
		this.page = page;
		if (navigate) {
			this.router.navigate(['/line'], {
				queryParams: {
					page: this.page,
					size: this.itemsPerPage,
					sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
				},
			});
		}
		this.lines = data ?? [];
		this.ngbPaginationPage = this.page;
	}

	protected onError(): void {
		this.ngbPaginationPage = this.page ?? 1;
	}
}
