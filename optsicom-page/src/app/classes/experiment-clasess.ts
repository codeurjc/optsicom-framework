export class Experiment {
	public id: number;
    public researcher: Researcher;
    public instances: Array<ElementDescription>;
    public methods: Array<ElementDescription>;
	public date: Date;
	public computer: ElementDescription;
	public timeLimit: number;
	public maxTimeLimit: number;
	public problemBestMode: BestMode;
	public problemName: string;
	public recordEvolution: boolean;
	public name: string;
    public useCase: string;
	public description: string;
	public numExecs: number;
}

export class Researcher {
    public id: number;
	public name: string;
}

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

export enum BestMode {
	MIN_IS_BEST = "MIN_IS_BEST", 
	MAX_IS_BEST = "MAX_IS_BEST", 
}

export class CompleteExperimentInfo {
    public experiment: Experiment;
    public methodNames: Array<ExperimentMethodName>
}

export class ExperimentMethodName {
    public name: string;
    public method: ElementDescription;
}