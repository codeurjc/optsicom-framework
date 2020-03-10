import { Researcher } from './researcher';
import { BestMode } from './bestmode';
import { ElementDescription } from './elementdescription';

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