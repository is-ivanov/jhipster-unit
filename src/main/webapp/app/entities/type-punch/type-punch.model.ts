export interface ITypePunch {
  id?: number;
  name?: string;
  description?: string | null;
}

export class TypePunch implements ITypePunch {
  constructor(public id?: number, public name?: string, public description?: string | null) {}
}

export function getTypePunchIdentifier(typePunch: ITypePunch): number | undefined {
  return typePunch.id;
}
