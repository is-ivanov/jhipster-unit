export interface IRevision {
	id?: number;
	revision: string;
}

export class Revision implements IRevision {
	constructor(public revision: string, public id?: number) {
	}
}
