import { IProject } from 'app/entities/project/project.model';

export interface ICompany {
  id?: number;
  shortName?: string;
  fullName?: string | null;
  email?: string | null;
  projects?: IProject[] | null;
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public shortName?: string,
    public fullName?: string | null,
    public email?: string | null,
    public projects?: IProject[] | null
  ) {}
}

export function getCompanyIdentifier(company: ICompany): number | undefined {
  return company.id;
}
