import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReportService } from 'src/app/services/report.service';
import { ShowExperimentMethods, ShowMethod } from './show-method';
import { ReportConfResponseDTO, ReportResponseDTO } from 'src/app/classes/report';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { InstanceBasicResponseDTO } from 'src/app/classes/instance';
import { JupyterDTO } from './../../classes/jupyter';
import { JupyterService } from './../../services/jupyter.service';
import { SelectionModel } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { MatMenuModule} from '@angular/material/menu';

const ReportMode = {
  SINGLE: 'single',
  COMPARE: 'compare'
}

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {

  public loadingInfo: boolean = false;

  private compareMode: string;
  private expIds: Array<number> = new Array;

  public report: ReportResponseDTO;

  // Selected Methods
  public showExpMethods: Array<ShowExperimentMethods> = new Array;

  // Selected Instance Table
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  public displayedColumns: string[] = ["id", "name", "select"];
  public dataSource: MatTableDataSource<InstanceBasicResponseDTO>;
  public selection: SelectionModel<InstanceBasicResponseDTO>;
  public noInstancesShow: boolean = false;

  public jupyterInfo: JupyterDTO;

  constructor(private router: Router,
    private activatedRoute: ActivatedRoute,
    private reportService: ReportService,
    private jupyterService: JupyterService) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      (params) => {
        let expId = params["expId"];
        let expIds = params["expIds"];
        let methodIds = params["methodIds"];
        let instanceIds = params["instanceIds"];

        // Clean previous experiment ids
        this.expIds = new Array;

        if (expId !== undefined) {
          this.compareMode = ReportMode.SINGLE;

          this.expIds.push(expId);
        } else if (expIds !== undefined) {
          this.compareMode = ReportMode.COMPARE;

          this.expIds.push(expIds);
        }

        this.loadReport(this.expIds, methodIds, instanceIds);
      }
    );
  }

  loadReport(expIds: Array<number>,
    methodIds: Array<number> = undefined,
    instanceIds: Array<number> = undefined) {
    this.loadingInfo = true;

    this.reportService.getReport(expIds, methodIds, instanceIds).subscribe(
      report => {
        this.report = report;

        this.prepareInstancesTable(report.configuration);
        this.prepareMethods(report.configuration);
        this.prepareJupyter(report.configuration);
        this.loadingInfo = false;
      }
    );
  }

  updateSelectMethods() {
    let params = {};

    let methodIds: Array<number> = this.selectedMethods(this.report.configuration);
    let instancesIds: Array<number> = this.selectedInstances(this.report.configuration);

    if (this.compareMode == ReportMode.SINGLE) {
      if (methodIds !== undefined && methodIds.length > 0) {
        params["methodIds"] = methodIds;
      }

      if (instancesIds !== undefined) {
        params["instanceIds"] = instancesIds;
      }

      this.router.navigate(['/experiments/', this.expIds[0], 'report', params]);
    } else if (this.compareMode == ReportMode.COMPARE) {
      params["expIds"] = this.expIds;

      if (methodIds !== undefined && methodIds.length > 0) {
        params["methodIds"] = methodIds;
      }

      if (instancesIds !== undefined) {
        params["instanceIds"] = instancesIds;
      }

      this.router.navigate(['/experiments/compare', params]);
    }
  }

  /* Prepare Methods */

  private prepareMethods(conf: ReportConfResponseDTO) {
    this.showExpMethods = new Array;

    conf.expMethods.forEach(expMethod => {
      let methods: Array<ShowMethod> = new Array;

      expMethod.methods.forEach(method => methods.push(
        new ShowMethod(method.id, method.methodNameExp, conf.selectedMethods.includes(method.id))
      ));

      this.showExpMethods.push(new ShowExperimentMethods(expMethod.id, expMethod.name, methods));
    });
  }

  /* Obtain selected methods and instances */

  private selectedInstances(conf: ReportConfResponseDTO): number[] {
    let selectedInstanceIds: Array<number> = new Array;
    this.selection.selected.forEach(instance => selectedInstanceIds.push(instance.id));

    return (selectedInstanceIds.length === conf.instances.length) ? undefined : selectedInstanceIds;
  }

  private selectedMethods(conf: ReportConfResponseDTO): number[] {
    let methodIds: Array<number> = new Array;
    let selectedMethodIds: Array<number> = new Array;

    conf.expMethods.forEach(expMethod => {
      expMethod.methods.forEach(method => methodIds.push(method.id));
    });

    this.showExpMethods.forEach(showExpMethod => {
      showExpMethod.methods
        .filter(showMethod => showMethod.selected)
        .forEach(showMethod => selectedMethodIds.push(showMethod.id));
    });

    return (selectedMethodIds.length === methodIds.length) ? undefined : selectedMethodIds;
  }

  /* Selected Instance Table */

  private prepareInstancesTable(conf: ReportConfResponseDTO) {
    // Initialize table
    this.dataSource = new MatTableDataSource(conf.instances);

    // Allow paginator
    this.dataSource.paginator = this.paginator;

    // Allow sort Table
    this.dataSource.sort = this.sort;
    this.addFilter();

    // Select instances
    let selectedInstances: InstanceBasicResponseDTO[] = [];
    conf.instances
      .filter(instance => conf.selectedInstances.includes(instance.id))
      .forEach(instance => selectedInstances.push(instance));
      
    this.selection = new SelectionModel<InstanceBasicResponseDTO>(true, selectedInstances);
  }

  public isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;

    return numSelected === numRows;
  }

  public masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.dataSource.data.forEach(instance => this.selection.select(instance));
  }

  public applyFilter(filterValue: string) {
    if (filterValue.length == 0 && this.dataSource.data.length > 0) {
      this.noInstancesShow = false;
    }

    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  private addFilter() {
    this.dataSource.filterPredicate =
      (data: InstanceBasicResponseDTO, filters: string) => {
        const matchFilter = [];
        const filterArray = filters.split(',');
        const columns = [data.id.toString(), data.name];

        //Main loop
        filterArray.forEach(filter => {
          const customFilter = [];
          columns.forEach(column => customFilter.push(column.toLowerCase().includes(filter)));
          matchFilter.push(customFilter.some(Boolean)); // OR
        });

        if (!matchFilter.includes(true)) {
          this.noInstancesShow = true;
        } else {
          this.noInstancesShow = false;
        }

        return matchFilter.every(Boolean); // AND
      };
  }

  private prepareJupyter(conf: ReportConfResponseDTO){
    this.jupyterService.getJupyterUrl().subscribe((params) =>{
      if(params !== null){
        this.jupyterInfo = params;
        this.jupyterInfo.selectedInstances = conf.selectedInstances.toString();
        this.jupyterInfo.selectedExperiments = conf.expId.toString();
        this.jupyterInfo.selectedMethods = conf.selectedMethods.toString();
      }
    });
  }
}
