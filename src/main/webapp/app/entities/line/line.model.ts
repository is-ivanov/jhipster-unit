import { IBlock } from 'app/entities/block/block.model';
import { StatusLine } from 'app/entities/enumerations/status-line.model';

export interface ILine {
  id?: number;
  tag?: string;
  revision?: string;
  status?: StatusLine;
  block?: IBlock;
}

export class Line implements ILine {
  constructor(public id?: number, public tag?: string, public revision?: string, public status?: StatusLine, public block?: IBlock) {}
}

export function getLineIdentifier(line: ILine): number | undefined {
  return line.id;
}
