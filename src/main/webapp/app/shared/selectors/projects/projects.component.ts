import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { IProject } from '../../../entities/project/project.model';
import { ProjectService } from '../../../entities/project/service/project.service';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { DropdownDataService } from '../../dropdown-data.service';
import { Subscription } from 'rxjs';

@Component({
	selector: 'jhi-projects',
	templateUrl: './projects.component.html',
})
export class ProjectsComponent implements OnInit, OnDestroy {
	projects: IProject[] = [];
	@Input() selectedProjectId?: number;

	clearFilterNotifierSubscription: Subscription =
		this.dropdownDataService.clearFilterNotifier
			.subscribe(() => this.selectedProjectId = undefined);

	constructor(protected projectService: ProjectService,
	            protected dropdownDataService: DropdownDataService) {}

	ngOnInit(): void {
		this.loadProjects();
	}

	updateFilter(): void {
		this.dropdownDataService.notifyProjectChange(this.selectedProjectId);
	}

	trackProjectById(index: number, item: IProject): number {
		return item.id!;
	}

	ngOnDestroy(): void {
		this.clearFilterNotifierSubscription.unsubscribe();
	}

	private loadProjects(): void {
		this.projectService
			.query({
				eagerload: false,
				sort: ['name,asc'],
			})
			.pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
			.subscribe((loadedProjects: IProject[]) => (this.projects = loadedProjects));
	}
}
