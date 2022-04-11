import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { LineService } from '../../../entities/line/service/line.service';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { DropdownDataService } from '../../dropdown-data.service';
import { Subscription } from 'rxjs';

@Component({
	selector: 'jhi-revisions',
	templateUrl: './revisions.component.html'
})
export class RevisionsComponent implements OnInit, OnDestroy {
	revisions: string[] = [];
	dropdownSettings: IDropdownSettings = {};
	@Input() selectedRevisions?: string[];
	selectedProjectId?: number;
	selectedBlockId?: number;

	projectNotifierSubscription: Subscription =
		this.dropdownDataService.projectNotifier.subscribe((projectId) => {
			this.selectedProjectId = projectId;
			this.loadRevisionsByProject(projectId);
		});

	blockNotifierSubscription: Subscription =
		this.dropdownDataService.blockNotifier.subscribe((blockId) => {
			this.selectedBlockId = blockId;
			this.selectedProjectId = undefined;
			this.loadRevisionsByBlock(blockId);
	});

	clearFilterNotifierSubscription: Subscription =
		this.dropdownDataService.clearFilterNotifier
			.subscribe(() => {
				this.selectedProjectId = undefined;
				this.selectedBlockId = undefined;
				this.selectedRevisions = undefined;
				this.loadAllRevisions();
			});

	constructor(protected lineService: LineService,
	            protected dropdownDataService: DropdownDataService) {
	}

	ngOnInit(): void {
		this.loadAllRevisions();
		this.dropdownSettings = {
			idField: 'revision',
			textField: 'revision',
			enableCheckAll: true,
			selectAllText: 'Выделить всё',
			unSelectAllText: 'Снять выделение',
			allowSearchFilter: true,
			searchPlaceholderText: 'Поиск',
			maxHeight: 250,
			itemsShowLimit: 0,
			noFilteredDataAvailablePlaceholderText: 'нет данных'
		};
	}

	updateFilter(): void {
		this.dropdownDataService.notifyRevisionsChange(this.selectedRevisions);
	}

	ngOnDestroy(): void {
		this.projectNotifierSubscription.unsubscribe();
		this.blockNotifierSubscription.unsubscribe();
		this.clearFilterNotifierSubscription.unsubscribe();
	}

	private loadAllRevisions(): void {
		this.lineService.getAllRevisions()
			.subscribe((loadedRevisions: string[]) => (this.revisions = loadedRevisions));
	}

	private loadRevisionsByProject(projectId: number): void {
		this.lineService.getRevisionsByProject(projectId)
			.subscribe((loadedRevisions: string[]) => (this.revisions = loadedRevisions));
	}

	private loadRevisionsByBlock(blockId: number): void {
		this.lineService.getRevisionsByBlock(blockId)
			.subscribe((loadedRevisions: string[]) => (this.revisions = loadedRevisions));
	}
}
