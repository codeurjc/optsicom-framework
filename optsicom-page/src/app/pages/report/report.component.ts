import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReportService } from 'src/app/services/report.service';
import { ShowMethod } from './show-method';
import { ReportRestConfiguration, ReportRest } from 'src/app/classes/report-classes';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {

  public loadingInfo: boolean = false;

  private expIds: Array<number> = new Array;

  public report: ReportRest;
  public bestValues: boolean = false;
  public showMethods: Array<ShowMethod> = new Array;

  constructor(private activatedRoute: ActivatedRoute,
    private reportService: ReportService) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      (params) => {
        let expId = params["experimentId"];
        let expIds = params["experimentsId"];

        if (expId !== undefined) {
          this.expIds.push(expId);
        }

        if (expIds !== undefined) {
          this.expIds.push(expIds);
        }

        this.loadReport(this.expIds);
      }
    );
  }

  loadReport(expIds: Array<number>, methodIds: Array<number> = undefined, bestValues: boolean = false) {
    this.loadingInfo = true;

    this.reportService.getReport(expIds, methodIds, bestValues).subscribe(
      report => {
        this.report = report;
        this.prepareMethods(report.configuration);

        this.loadingInfo = false;
      }
    );
  }

  updateSelectMethods() {
    let methodIds: Array<number> = new Array;

    this.showMethods.forEach(showMethod => {
      if (showMethod.selected) {
        methodIds.push(showMethod.getMethodId());
      }
    });

    this.loadReport(this.expIds, methodIds, this.bestValues);
  }

  private prepareMethods(configuration: ReportRestConfiguration) {
    this.showMethods = new Array;

    configuration.methods.forEach(method => {
      let selected: boolean = configuration.selectedMethods.includes(method.method.id);

      this.showMethods.push(new ShowMethod(method, selected));
    });
  }

}
