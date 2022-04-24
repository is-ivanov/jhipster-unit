import { IUser } from '../../entities/user/user.model';

export interface IUserAccount extends IUser{
  // id?: number;
  // login?: string;
  // firstName?: string | null;
  // lastName?: string | null;
  email?: string;
  activated?: boolean;
  langKey?: string;
  authorities?: string[];
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
	companyId?: number;
}

export class User implements IUserAccount {
  constructor(
    public id?: number,
    public login?: string,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public activated?: boolean,
    public langKey?: string,
    public authorities?: string[],
    public createdBy?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date,
		public companyId?: number
  ) {}
}
