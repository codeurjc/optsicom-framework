import { Component, OnInit, ViewChild } from '@angular/core';
import { ExperimentsService } from 'src/app/services/experiments.service';
import { Experiment } from 'src/app/classes/experiment';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { YesNoDialogComponent } from 'src/app/common-dialogs/yes-no-dialog/yes-no-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { TransitiveCompileNgModuleMetadata } from '@angular/compiler';
import { Router } from '@angular/router';

@Component({
  selector: 'app-experiments-list',
  templateUrl: './experiments-list.component.html',
  styleUrls: ['./experiments-list.component.scss']
})
export class ExperimentsListComponent implements OnInit {

  public loadingInfo: boolean = false;

  public experimentAlertsShow: boolean = false;
  public experimentAlertsMessage: string;

  // Table
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  public displayedColumns: string[];
  public dataSource: any;
  public selection = new SelectionModel<Experiment>(true, []);

  constructor(private router: Router,
    private experimentsService: ExperimentsService,
    public dialog: MatDialog,
    public snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.loadTable();
  }

  private loadTable() {
    this.loadingInfo = true;

    this.experimentsService.getExperiments().subscribe(
      (experiments: Experiment[]) => {
        if (experiments == null || experiments == undefined || experiments.length == 0) {
          this.experimentAlertsShow = true;
          this.experimentAlertsMessage = "No experiments created in database";
        } else {
          this.experimentAlertsShow = false;
        }


        this.dataSource = new MatTableDataSource(experiments);
        this.displayedColumns = ['select', 'problemName', 'date', 'name', 'instances', 'methods', 'researcher', 'action'];
        this.dataSource.sort = this.sort;
        this.addFilter();

        this.loadingInfo = false;
      }
    );
  }

  public applyFilter(filterValue: string) {
    if (filterValue.length == 0 && this.dataSource.data.length > 0) {
      this.experimentAlertsShow = false;
    }

    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  private addFilter() {
    this.dataSource.filterPredicate =
      (data: Experiment, filters: string) => {
        const matchFilter = [];
        const filterArray = filters.split(',');
        const columns = [data.problemName, data.name, data.instances.length.toString(), data.methods.length.toString(), data.researcher.name];

        //Main loop
        filterArray.forEach(filter => {
          const customFilter = [];
          columns.forEach(column => customFilter.push(column.toLowerCase().includes(filter)));
          matchFilter.push(customFilter.some(Boolean)); // OR
        });

        if (!matchFilter.includes(true)) {
          this.experimentAlertsShow = true;
          this.experimentAlertsMessage = "No matches have been found";
        } else {
          this.experimentAlertsShow = false;
        }

        return matchFilter.every(Boolean); // AND
      };
  }

  public compareExperiments() {
    let compareIds: Array<number> = [];
    this.selection.selected.forEach(experiment => { compareIds.push(experiment.id) });
    this.router.navigate(['/experiments/comparereport', {experimentsId: compareIds}]);
  }

  public removeExperiment(experimentId: number) {
    let message = "Are you sure you want to eliminate the experiment?";

    const dialogRef = this.dialog.open(YesNoDialogComponent, {
      width: '500px',
      data: { message: message, buttonYes: 'delete', buttonNo: 'cancel' }
    });

    dialogRef.afterClosed().subscribe(
      (response: boolean) => {
        if (response !== undefined && response) {
          this.experimentsService.deleteExperiment(experimentId).subscribe(
            () => {
              this.loadTable();
            }, () => {
              this.snackBar.open('Error while deleting experiment, please try again', 'Cerrar', {
                duration: 3000,
                panelClass: "snack-success"
              });
            }
          );
        }
      }
    );
  }
}
