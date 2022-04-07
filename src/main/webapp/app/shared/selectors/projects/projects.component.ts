import { Component, OnInit } from '@angular/core';
import { IProject } from '../../../entities/project/project.model';
import { ProjectService } from '../../../entities/project/service/project.service';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';

@Component({
	selector: 'jhi-projects',
	templateUrl: './projects.component.html',
})
export class ProjectsComponent implements OnInit {
	projects: IProject[] = [];
	selectedProject?: IProject;

	constructor(protected projectService: ProjectService) {
	}

	ngOnInit(): void {
		this.loadProjects();
	}

	loadProjects(): void {
		this.projectService
			.query({
				eagerload: false,
				sort: ['name,asc'],
			})
			.pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
			.subscribe((loadedProjects: IProject[]) => (this.projects = loadedProjects));
	}

	trackProjectById(index: number, item: IProject): number {
		return item.id!;
	}
	
}
