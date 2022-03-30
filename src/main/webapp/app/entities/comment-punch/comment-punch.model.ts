import { IPunchItem } from 'app/entities/punch-item/punch-item.model';

export interface ICommentPunch {
  id?: number;
  comment?: string;
  punchItem?: IPunchItem;
}

export class CommentPunch implements ICommentPunch {
  constructor(public id?: number, public comment?: string, public punchItem?: IPunchItem) {}
}

export function getCommentPunchIdentifier(commentPunch: ICommentPunch): number | undefined {
  return commentPunch.id;
}
