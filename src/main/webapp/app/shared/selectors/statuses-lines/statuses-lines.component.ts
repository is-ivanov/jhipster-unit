import { Component, EventEmitter, Input, Output } from '@angular/core';
import { StatusLine } from 'app/entities/enumerations/status-line.model';

@Component({
	selector: 'jhi-statuses-lines',
	templateUrl: './statuses-lines.component.html',
})
export class StatusesLinesComponent {
	statuses = Object.keys(StatusLine);
	@Input() selectedStatusLine?: string;
	@Output() updateStatusLineInFilter = new EventEmitter<string>();

	updateFilter(): void {
		this.updateStatusLineInFilter.emit(this.selectedStatusLine);
	}
}
