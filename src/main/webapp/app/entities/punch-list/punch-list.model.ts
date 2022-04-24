import { IProject } from 'app/entities/project/project.model';
import { IAppUser } from '../app-user/app-user.model';

export interface IPunchList {
	id?: number;
	number?: number;
	name?: string | null;
	description?: string | null;
	project?: IProject;
	author?: IAppUser;
}

export class PunchList implements IPunchList {
	constructor(
		public id?: number,
		public number?: number,
		public name?: string | null,
		public description?: string | null,
		public project?: IProject,
		public author?: IAppUser
	) {
	}
}

export function getPunchListIdentifier(punchList: IPunchList): number | undefined {
	return punchList.id;
}
