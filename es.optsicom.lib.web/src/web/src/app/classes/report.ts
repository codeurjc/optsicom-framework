import { ExperimentMethodBasicResponseDTO } from './experiment';
import { InstanceBasicResponseDTO } from './instance';

export class ReportResponseDTO {
	public configuration: ReportConfResponseDTO;
	public blocksTable: Array<ReportBlockResponseDTO>;
}

export class ReportConfResponseDTO {
	public expId: Array<number>;
	public selectedMethods: Array<number>;
	public expMethods: Array<ExperimentMethodBasicResponseDTO>;
	public selectedInstances: Array<number>;
	public instances: Array<InstanceBasicResponseDTO>;
}

export class ReportBlockResponseDTO {
	public name: string;
	public tables: Array<ReportTableResponseDTO>;
}

export class ReportTableResponseDTO {
	public title: string;
	public cellValues: Array<Array<ReportCellResponseDTO>>;
	public rowTitles: Array<ReportTitleResponseDTO>;
	public columnTitles: Array<ReportTitleResponseDTO>;
}

export class ReportCellResponseDTO {
	public value: any;
	public format: NumericFormat;
	public color: string;

	constructor(value: any, format?: NumericFormat, color?: string) {
		this.value = value;
		this.format = format;
		this.color = color;
	}
}

export class ReportTitleResponseDTO {
	public attributes: Array<ReportAttributeResponseDTO>;
	public infoTitle: string;
}

export class ReportAttributeResponseDTO {
	public name: string;
	public value: string;
}

export class NumericFormat {
	public type: NumberType;
	public numDecimals: number;
}

export enum NumberType {
	INTEGER = "INTEGER",
	DECIMAL = "DECIMAL",
	PERCENT = "PERCENT",
	TIME = "TIME"
}
