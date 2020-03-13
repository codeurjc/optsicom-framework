import { Pipe, PipeTransform } from '@angular/core';
import { ReportRestCell, NumberType } from '../classes/report-classes';

@Pipe({ name: 'showRestCell' })
export class ShowRestCell implements PipeTransform {
    transform(cell: ReportRestCell) {

        if(cell.value == null || cell.value == undefined) {
            return "#";
        }

        let value: number = Number(cell.value);
        let numDecimals: number = 2;

        if (cell.format !== null && cell.format !== undefined) {
            numDecimals = cell.format.numDecimals;

            switch (cell.format.type) {
                case NumberType.INTEGER:
                    return value.toFixed(numDecimals);
                case NumberType.DECIMAL:
                    return value.toFixed(numDecimals);
                case NumberType.PERCENT:
                    return (value * 100).toFixed(numDecimals) + "%";
                default:
                    return value.toExponential(numDecimals);
            }
        } else {
            return value.toExponential(numDecimals);
        }
    }
}