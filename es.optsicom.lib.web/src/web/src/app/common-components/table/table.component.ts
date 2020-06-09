import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ReportRestTable, ReportRestAttribute, ReportRestCell } from 'src/app/classes/report-classes';
import { Sort } from '@angular/material/sort';
import { Column, CellValue } from './table';
import { FormControl } from '@angular/forms';
import { MatSelectChange } from '@angular/material/select';

@Component({
  selector: 'optsicom-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnChanges {

  @Input() table: ReportRestTable;

  public showColumnsForm: FormControl;
  public allSelectColumns: Array<string>;

  public columnsTitles: Array<Array<Column>>;
  public cellValues: Array<Array<CellValue>>;

  constructor() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.table !== undefined) {
      this.initColumns();
      this.initCellValues();
    }
  }

  private initColumns() {
    this.columnsTitles = new Array();
    let colSpan: Array<number> = new Array();
    let nColumnTitles: number = this.table.columnTitles[0].attributes.length;

    // Array init with number of columns title
    for (let i = 0; i < nColumnTitles; i++) {
      colSpan[i] = 1;
      this.columnsTitles[i] = new Array();
    }

    // Add row titles
    if (this.table.rowTitles.length > 0) {
      this.table.rowTitles[0].attributes.forEach(attribute => {
        for (let i = 0; i < nColumnTitles; i++) {
          let value: string = "";

          if (i == (nColumnTitles - 1)) {
            value = attribute.name;
          }

          this.columnsTitles[i].push(new Column(value, 1));
        }
      });
    }

    // Add column titles
    for (let i = 0; i < this.table.columnTitles.length; i++) {
      let column = this.table.columnTitles[i];

      for (let j = 0; j < column.attributes.length; j++) {
        let attribute: ReportRestAttribute = column.attributes[j];
        let value: string = attribute.value;
        let valueNextAtribute: string = undefined;

        if ((i + 1) < this.table.columnTitles.length) {
          valueNextAtribute = this.table.columnTitles[i + 1].attributes[j].value;
        }

        if (value !== valueNextAtribute) {
          this.columnsTitles[j].push(new Column(value, colSpan[j]));
          colSpan[j] = 1;
        } else {
          colSpan[j]++;
        }
      }
    }

    // Associated columns
    if (this.columnsTitles.length > 1) {
      for (let i = 0; i < this.columnsTitles.length - 1; i++) {
        let pointer: number = 0;

        this.columnsTitles[i].forEach(column => {
          let colSpan: number = column.colSpan;
          let limit: number = (pointer + colSpan);
          let associate: Array<string> = new Array();

          for (pointer; pointer < limit; pointer++) {
            associate.push(this.columnsTitles[this.columnsTitles.length - 1][pointer].name);
          }

          column.associate = associate;
        });
      }
    }

    // Selected columns
    this.allSelectColumns = new Array();
    let selectedColumns: Array<string> = new Array();

    this.columnsTitles[this.columnsTitles.length - 1].forEach(column => {
      if (!this.allSelectColumns.includes(column.name)) {
        this.allSelectColumns.push(column.name);
        selectedColumns.push(column.name);
      }
    });

    this.showColumnsForm = new FormControl(selectedColumns);
  }

  private initCellValues() {
    this.cellValues = new Array();
    let values: Array<CellValue> = new Array();

    for (let i = 0; i < this.table.cellValues.length; i++) {
      let nCol: number = 0;

      this.table.rowTitles[i].attributes.forEach(attribute => {
        let colName = this.columnsTitles[this.columnsTitles.length - 1][nCol].name;

        values.push(new CellValue(new ReportRestCell(attribute.value), colName, true));
        nCol++;
      });

      this.table.cellValues[i].forEach(value => {
        let colName = this.columnsTitles[this.columnsTitles.length - 1][nCol].name;

        values.push(new CellValue(value, colName, false));
        nCol++;
      });

      this.cellValues.push(values);
      values = new Array();
    }
  }

  sortData(sort: Sort) {
    let sortColumn = sort.active.replace('col-', '');

    this.cellValues.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      return compare(String(a[sortColumn].cell.value), String(b[sortColumn].cell.value), isAsc)
    })
  }

  selectColumns(event: MatSelectChange) {
    let selectedArray: Array<string> = event.value;

    // Associated columns
    if (this.columnsTitles.length > 1) {
      for (let i = 0; i < this.columnsTitles.length - 1; i++) {
        this.columnsTitles[i].forEach(column => {
          let colSpan = 0;

          selectedArray.forEach(show => {
            if (column.associate.includes(show)) {
              colSpan++;
            }
          });

          if (colSpan == 0) {
            column.hideCell();
          } else {
            column.showCell();
          }

          column.colSpan = colSpan;
        });
      }
    }

    this.columnsTitles[this.columnsTitles.length - 1].forEach(column => {
      if (selectedArray.includes(column.name)) {
        column.showCell();

        this.cellValues.forEach(cell => {
          let values: Array<CellValue> = cell.filter(value => value.colName == column.name);
          values.forEach(value => { value.showCell() });
        });
      } else {
        column.hideCell();

        this.cellValues.forEach(cell => {
          let values: Array<CellValue> = cell.filter(value => value.colName == column.name);
          values.forEach(value => { value.hideCell() });
        });
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}