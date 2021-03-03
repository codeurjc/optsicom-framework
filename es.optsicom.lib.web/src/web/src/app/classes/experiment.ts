import { InstanceExtendResponseDTO } from "./instance";
import { MethodBasicResponseDTO, MethodExtendResponseDTO } from "./method";

export class ExperimentBasicResponseDTO {
    public id: number;
	public researcherName: string;
	public nInstances: number;
	public nMethods: number;
	public date: Date;
	public problemName: string;
	public name: string;
}

export class ExperimentExtendResponseDTO {
	public id: number;
	public researcherName: string;
	public instances: Array<InstanceExtendResponseDTO>;
	public methods: Array<MethodExtendResponseDTO>;
	public date: Date;
	public computerName: string;
	public timeLimit: number;
	public maxTimeLimit: number;
	public problemBestMode: string;
	public problemName: string;
	public recordEvolution: boolean;
	public name: string;
	public useCase: string;
	public description: string;
	public numExecs: number;
}

export class ExperimentMethodBasicResponseDTO {
	public id: number;
	public methods: Array<MethodBasicResponseDTO>;
	public name: string;
}
