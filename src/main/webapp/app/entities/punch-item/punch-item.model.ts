import dayjs from 'dayjs/esm';
import { ITypePunch } from 'app/entities/type-punch/type-punch.model';
import { ILine } from 'app/entities/line/line.model';
import { IPunchList } from 'app/entities/punch-list/punch-list.model';
import { IPriorityPunch } from 'app/entities/priority-punch/priority-punch.model';
import { ICompany } from 'app/entities/company/company.model';
import { IUser } from 'app/entities/user/user.model';
import { ICommentPunch } from 'app/entities/comment-punch/comment-punch.model';
import { StatusPunch } from 'app/entities/enumerations/status-punch.model';

export interface IPunchItem {
  id?: number;
  number?: number;
  location?: string | null;
  description?: string;
  revisionDrawing?: string | null;
  status?: StatusPunch;
  closedDate?: dayjs.Dayjs | null;
  type?: ITypePunch;
  line?: ILine | null;
  punchList?: IPunchList;
  priority?: IPriorityPunch;
  executor?: ICompany | null;
  author?: IUser;
  comments?: ICommentPunch[] | null;
}

export class PunchItem implements IPunchItem {
  constructor(
    public id?: number,
    public number?: number,
    public location?: string | null,
    public description?: string,
    public revisionDrawing?: string | null,
    public status?: StatusPunch,
    public closedDate?: dayjs.Dayjs | null,
    public type?: ITypePunch,
    public line?: ILine | null,
    public punchList?: IPunchList,
    public priority?: IPriorityPunch,
    public executor?: ICompany | null,
    public author?: IUser,
    public comments?: ICommentPunch[] | null
  ) {}
}

export function getPunchItemIdentifier(punchItem: IPunchItem): number | undefined {
  return punchItem.id;
}
