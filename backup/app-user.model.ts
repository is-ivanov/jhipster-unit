import { IUser } from 'app/entities/user/user.model';
import { ICompany } from 'app/entities/company/company.model';

export interface IAppUser {
  id?: number;
  user?: IUser;
  company?: ICompany;
}

export class AppUser implements IAppUser {
  constructor(public id?: number, public user?: IUser, public company?: ICompany) {}
}

export function getAppUserIdentifier(appUser: IAppUser): number | undefined {
  return appUser.id;
}
