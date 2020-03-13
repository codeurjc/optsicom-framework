import { ReportRestCell } from 'src/app/classes/report-classes';

export class Column {
    public name: string;
    public colSpan: number;
    public associate: Array<string>;
    public ngStyle: object;

    constructor(name: string, colSpan: number, associate?: Array<string>) {
        this.name = name;
        this.colSpan = colSpan;
        this.associate = new Array();
        this.showCell();

        if (associate !== undefined) {
            this.associate = associate;
        }
    }

    public showCell() {
        this.ngStyle = { display: 'table-cell' };
    }

    public hideCell() {
        this.ngStyle = { display: 'none' };
    }
}

export class CellValue {
    public cell: ReportRestCell;
    public colName: string;
    public synthetic: boolean;
    public ngStyle: object;

    constructor(cell: ReportRestCell, colName: string, synthetic: boolean) {
        this.cell = cell;
        this.colName = colName;
        this.synthetic = synthetic;
        this.showCell();
    }

    public showCell() {
        if (this.cell.color !== null) {
            this.ngStyle = { "display": 'table-cell', "background-color": this.cell.color }
        } else {
            this.ngStyle = { display: 'table-cell' };
        }
    }

    public hideCell() {
        this.ngStyle = { display: 'none' };
    }
}