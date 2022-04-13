export interface IPriorityPunch {
  id?: number;
  priority?: number;
  name?: string;
  description?: string | null;
}

export class PriorityPunch implements IPriorityPunch {
  constructor(public id?: number, public priority?: number, public name?: string, public description?: string | null) {}
}

export function getPriorityPunchIdentifier(priorityPunch: IPriorityPunch): number | undefined {
  return priorityPunch.id;
}
