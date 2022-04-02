import { ICompany } from "../../entities/company/company.model";

export class Registration {
	constructor(
		public firstName: string,
		public lastName: string,
		public login: string,
		public email: string,
		public password: string,
		public company: ICompany,
		public langKey: string
	) {}
}
