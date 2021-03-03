export class ShowExperimentMethods {
    public id: number;
    public name: string;
    public methods: Array<ShowMethod>;

    constructor(id: number, name: string, methods: Array<ShowMethod>) {
        this.id = id;
        this.name = name;
        this.methods = methods;
    }
}

export class ShowMethod {
    public id: number;
    public name: string;
    public selected: boolean;

    constructor(id: number, name: string, selected: boolean) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }
}
