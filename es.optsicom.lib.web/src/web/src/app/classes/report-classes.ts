import { MethodName } from './experiment-clasess';

export class ReportRest {
	public configuration: ReportRestConfiguration;
	public blocksTable: Array<ReportRestBlock>;
}

export class ReportRestConfiguration {
	public expId: Array<number>;
	public selectedMethods: Array<number>;
	public methods: Array<MethodName>;
	public bestValues: boolean;
}

export class ReportRestBlock {
	public name: string;
	public tables: Array<ReportRestTable>;
}

export class ReportRestTable {
	public title: string;
	public cellValues: Array<Array<ReportRestCell>>;
	public rowTitles: Array<ReportRestTitle>;
	public columnTitles: Array<ReportRestTitle>;
}

export class ReportRestCell {
	public value: any;
	public format: NumericFormat;
	public color: string;

	constructor(value: any, format?: NumericFormat, color?: string) {
		this.value = value;
		this.format = format;
		this.color = color;
	}
}

export class ReportRestTitle {
	public attributes: Array<ReportRestAttribute>;
	public infoTitle: string;
}

export class ReportRestAttribute {
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