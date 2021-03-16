export interface ICweList {
  id?: number;
  cweId?: string | null;
  description?: string | null;
  code?: string | null;
  tags?: string | null;
}

export class CweList implements ICweList {
  constructor(
    public id?: number,
    public cweId?: string | null,
    public description?: string | null,
    public code?: string | null,
    public tags?: string | null
  ) {}
}
