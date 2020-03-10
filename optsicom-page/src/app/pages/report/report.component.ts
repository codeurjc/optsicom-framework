import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      (params) => {
        let experimentId = params["experimentId"];
        let experimentIds = params["experimentsId"];

        console.log(experimentId)
        console.log(experimentIds)
      }
    );
  }

}
