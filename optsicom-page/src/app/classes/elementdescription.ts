export class ElementDescription {
    public id: number;
    public name: string;
    public properties: DBProperties;
}

export class DBProperties {
    public name: string;
    public map: Map<string, string>;
    public propsAString: string;
    public sortedProperties: Array<Map<string, string>>;
}