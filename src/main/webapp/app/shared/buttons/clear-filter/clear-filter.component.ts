import { Component, EventEmitter, Output } from '@angular/core';

@Component({
	selector: 'jhi-clear-filter',
	templateUrl: './clear-filter.component.html',
})
export class ClearFilterComponent {
	@Output() clearFilter = new EventEmitter();

	clearFilters(button: HTMLButtonElement): void {
		this.clearFilter.emit();
		button.blur();
	}
}
