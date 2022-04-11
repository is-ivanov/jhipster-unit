import { Component, Input } from '@angular/core';
import { StatusLine } from 'app/entities/enumerations/status-line.model';
import { DropdownDataService } from '../../dropdown-data.service';

@Component({
	selector: 'jhi-statuses-lines',
	templateUrl: './statuses-lines.component.html',
})
export class StatusesLinesComponent {
	statuses = Object.keys(StatusLine);
	@Input() selectedStatusLine?: string;

	constructor(protected dropdownDataService: DropdownDataService) {}

	updateFilter(): void {
		this.dropdownDataService.notifyStatusLineChange(this.selectedStatusLine);
	}
}
