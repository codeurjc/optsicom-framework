import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ExperimentsService } from 'src/app/services/experiments.service';
import { NavigationService } from 'src/app/services/navigation.service';
import { Sort } from '@angular/material/sort';
import { YesNoDialogComponent } from 'src/app/common-dialogs/yes-no-dialog/yes-no-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { Experiment, MethodName, ElementDescription } from 'src/app/classes/experiment-clasess';

@Component({
  selector: 'app-experiment',
  templateUrl: './experiment.component.html',
  styleUrls: ['./experiment.component.scss']
})
export class ExperimentComponent implements OnInit {

  public loadingInfo: boolean = false;

  public experiment: Experiment;
  public methods: Array<MethodName>;
  public instances: Array<ElementDescription>;

  constructor(private router: Router,
    private experimentsService: ExperimentsService,
    private navigationService: NavigationService,
    public dialog: MatDialog,
    public snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.loadingInfo = true;

    this.experiment = this.experimentsService.getLoadExperimentRest().experiment;
    this.methods = this.experimentsService.getLoadExperimentRest().methodNames;
    this.instances = this.experimentsService.getLoadExperimentRest().experiment.instances;

    this.navigationService.addExperimentName(this.experiment);
    this.loadingInfo = false;
  }

  sortData(sort: Sort) {
    const data = this.experiment.instances;

    if (!sort.active || sort.direction === '') {
      this.instances = data;
      return;
    }

    this.instances = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'id': return compare(a.id, b.id, isAsc);
        case 'name': return compare(a.name, b.name, isAsc);
        case 'usecase': return compare(a.properties.map["usecase"], b.properties.map["usecase"], isAsc);
        case 'filename': return compare(a.properties.map["filename"], b.properties.map["filename"], isAsc);
        default: return 0;
      }
    });
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
              this.router.navigate(['/experiments']);
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

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}