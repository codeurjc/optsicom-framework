import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Components */
import { ExperimentsListComponent } from './pages/experiments-list/experiments-list.component';
import { ExperimentComponent } from './pages/experiment/experiment.component';
import { ReportComponent } from './pages/report/report.component';
import { LoginComponent } from './pages/login/login.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';

/* Guards */
import { AuthGuard } from './guard/auth.guard';
import { ControlCorrectExperimentGuard } from './guard/correct-content/control-correct-experiment';

const routes: Routes = [
  /* Redirect principal page */
  { path: '', redirectTo: 'experiments', pathMatch: 'full' },

  {
    path: 'experiments',
    children: [
      /* Experiment List page */
      {
        path: '',
        component: ExperimentsListComponent,
      },
      /* Compare experiments report page */
      {
        path: 'comparereport',
        component: ReportComponent,
      },
      /* Experiment page */
      {
        path: ':experimentId',
        component: ExperimentComponent,
        canActivate: [ControlCorrectExperimentGuard]
      },
      /* Experiment single report page */
      {
        path: ':experimentId/report',
        component: ReportComponent,
        canActivate: [ControlCorrectExperimentGuard]
      },
    ]
    , canActivate: [AuthGuard]
  },

  /* Login page */
  { path: 'login', component: LoginComponent, canActivate: [AuthGuard] },

  /* Error pages */
  { path: 'errorpage', component: ErrorPageComponent },
  { path: '**', redirectTo: 'errorpage', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
