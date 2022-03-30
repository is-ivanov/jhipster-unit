import { ICompany } from 'app/entities/company/company.model';

export interface IProject {
  id?: number;
  name?: string;
  description?: string | null;
  generalContractor?: ICompany | null;
  subContractors?: ICompany[] | null;
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public generalContractor?: ICompany | null,
    public subContractors?: ICompany[] | null
  ) {}
}

export function getProjectIdentifier(project: IProject): number | undefined {
  return project.id;
}
