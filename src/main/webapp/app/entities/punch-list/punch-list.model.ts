import { IProject } from 'app/entities/project/project.model';

export interface IPunchList {
  id?: number;
  number?: number;
  name?: string | null;
  description?: string | null;
  project?: IProject;
}

export class PunchList implements IPunchList {
  constructor(
    public id?: number,
    public number?: number,
    public name?: string | null,
    public description?: string | null,
    public project?: IProject
  ) {}
}

export function getPunchListIdentifier(punchList: IPunchList): number | undefined {
  return punchList.id;
}
