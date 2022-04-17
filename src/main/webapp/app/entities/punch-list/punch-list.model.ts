import { IProject } from 'app/entities/project/project.model';
import { IAppUser } from '../app-user/app-user.model';

export interface IPunchList {
	id?: number;
	number?: number;
	name?: string | null;
	description?: string | null;
	project?: IProject;
	author?: IAppUser;

	getAuthorFullName(): string;
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

	getAuthorFullName(): string {
		const author = this.author?.user;
		return `${author?.firstName?.charAt(0) ?? ''}.${author?.lastName ?? 'undefined'}`;
	}
}

export function getPunchListIdentifier(punchList: IPunchList): number | undefined {
	return punchList.id;
}
