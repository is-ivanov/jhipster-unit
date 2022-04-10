import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DropdownDataService {
	// TODO https://stackblogger.com/refresh-component-from-another-rxjs-angular/
	projectNotifier: Subject<number> = new Subject<number>();

	notifyAboutChange(projectId: number | undefined): void {
		if (projectId != null) {
			this.projectNotifier.next(projectId);
		}
	}
}
