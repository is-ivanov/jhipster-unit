import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { LineService } from '../../../entities/line/service/line.service';
import { IRevision, Revision } from './revision.model';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { Observable } from 'rxjs';

@Component({
	selector: 'jhi-revisions',
	templateUrl: './revisions.component.html',
	styleUrls: ['./revision.component.scss']
})
export class RevisionsComponent implements OnInit {
	revisions: IRevision[] = [];
	dropdownSettings: IDropdownSettings = {};
	@Input() selectedRevisions?: IRevision[];
	@Output() updateRevisionsInFilter = new EventEmitter<IRevision[]>();

	constructor(private lineService: LineService) {
	}

	ngOnInit(): void {
		this.loadData();
		this.dropdownSettings = {
			idField: 'id',
			textField: 'revision',
			enableCheckAll: true,
			selectAllText: 'Выделить всё',
			unSelectAllText: 'Снять выделение',
			allowSearchFilter: true,
			searchPlaceholderText: 'Поиск',
			maxHeight: 250,
			itemsShowLimit: 0,
			noFilteredDataAvailablePlaceholderText: 'нет данных',
		};
	}

	loadData(): void {
		const revisions: Observable<string[]> = this.lineService.getAllRevisions();

		revisions.subscribe((loadedRevisions: string[]) => (
			this.revisions =
				loadedRevisions.map((value, index) => new Revision(value, index))
		));
	}

	updateFilter(): void {
		this.updateRevisionsInFilter.emit(this.selectedRevisions);
	}
}
