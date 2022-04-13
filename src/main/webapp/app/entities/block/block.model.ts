import { IProject } from 'app/entities/project/project.model';

export interface IBlock {
  id?: number;
  number?: number;
  description?: string;
  project?: IProject;
}

export class Block implements IBlock {
  constructor(public id?: number, public number?: number, public description?: string, public project?: IProject) {}
}

export function getBlockIdentifier(block: IBlock): number | undefined {
  return block.id;
}
