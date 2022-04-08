import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILine } from '../line.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { LineService } from '../service/line.service';
import { LineDeleteDialogComponent } from '../delete/line-delete-dialog.component';
import { IProject } from '../../project/project.model';
import { ProjectService } from '../../project/service/project.service';
import { IBlock } from '../../block/block.model';
import { Item } from '../../../shared/multi-dropdown/multi-dropdown.model';

@Component({
	selector: 'jhi-line',
	templateUrl: './line.component.html',
})
export class LineComponent implements OnInit {
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
	revisions?: string[] = ['1', '2a', '5FY'];

	items: Item[] = [];
	showSearch = true;
	showError = false;
	showAll = true;
	showStatus = true;

	// TODO https://codeomelet.com/posts/creating-multi-select-dropdown-with-angular-and-bootstrap-5

	constructor(
		protected lineService: LineService,
		protected activatedRoute: ActivatedRoute,
		protected router: Router,
		protected modalService: NgbModal,
		protected projectService: ProjectService
	) {}

	loadPage(page?: number, dontNavigate?: boolean): void {
		this.isLoading = true;
		const pageToLoad: number = page ?? this.page ?? 1;

		this.lineService
			.query({
				page: pageToLoad - 1,
				size: this.itemsPerPage,
				sort: this.sort(),
			})
			.subscribe({
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

	ngOnInit(): void {
		this.handleNavigation();
		// this.projectService.loadProjectsIntoArray();
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

	onUpdateFilterProject(project: IProject): void {
		this.filterProjectId = project.id;
		this.loadPage(1);
	}

	onUpdateFilterStatusLine(status: string): void {
		this.filterStatusLine = status;
		this.loadPage(1);
	}

	onUpdateFilterBlock(block: IBlock): void {
		this.filterBlockId = block.id;
		this.loadPage(1);
	}

	clearFilter(): void {
		this.filterProjectId = undefined;
		this.filterBlockId = undefined;
		this.filterTag = undefined;
		this.filterRevision = undefined;
		this.filterStatusLine = undefined;
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
