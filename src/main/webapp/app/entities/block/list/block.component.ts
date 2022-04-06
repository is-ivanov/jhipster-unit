import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBlock } from '../block.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { BlockService } from '../service/block.service';
import { BlockDeleteDialogComponent } from '../delete/block-delete-dialog.component';
import { IProject } from '../../project/project.model';
import { ProjectService } from '../../project/service/project.service';
import { map } from 'rxjs/operators';

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
	projectsSharedCollection: IProject[] = [];
	filterNumber?: number;
	filterDescription?: string;
	filterProjectId?: IProject;

	constructor(
		protected blockService: BlockService,
		protected activatedRoute: ActivatedRoute,
		protected router: Router,
		protected modalService: NgbModal,
		protected projectService: ProjectService
	) {}

	loadPage(page?: number, dontNavigate?: boolean): void {
		this.isLoading = true;
		const pageToLoad: number = page ?? this.page ?? 1;

		const req = {};
		Object.assign(req, { page: pageToLoad - 1 });
		Object.assign(req, { size: this.itemsPerPage });
		Object.assign(req, { sort: this.sort() });

		if (this.filterNumber) {
			Object.assign(req, { 'number.equals': this.filterNumber });
		}
		if (this.filterDescription) {
			Object.assign(req, { 'description.contains': this.filterDescription });
		}
		if (this.filterProjectId) {
			Object.assign(req, { 'projectId.equals': this.filterProjectId });
		}

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
		this.loadRelationshipsOptions();
	}

	trackId(index: number, item: IBlock): number {
		return item.id!;
	}

	trackProjectById(index: number, item: IProject): number {
		return item.id!;
	}

	delete(block: IBlock): void {
		const modalRef = this.modalService.open(BlockDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
		modalRef.componentInstance.block = block;
		// unsubscribe not needed because closed completes on modal close
		modalRef.closed.subscribe((reason) => {
			if (reason === 'deleted') {
				this.loadPage();
			}
		});
	}

	filter(): void {
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

	protected loadRelationshipsOptions(): void {
		this.projectService
			.query({ sort: ['name,asc'] })
			.pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
			.subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));
	}

	protected onSuccess(data: IBlock[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
		this.totalItems = Number(headers.get('X-Total-Count'));
		this.page = page;
		if (navigate) {
			this.router.navigate(['/block'], {
				queryParams: {
					page: this.page,
					size: this.itemsPerPage,
					sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
				},
			});
		}
		this.blocks = data ?? [];
		this.ngbPaginationPage = this.page;
	}

	protected onError(): void {
		this.ngbPaginationPage = this.page ?? 1;
	}
}
