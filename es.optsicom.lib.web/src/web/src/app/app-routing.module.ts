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

  /* Experiments */
  {
    path: 'experiments',
    children: [
      /* Experiment List page */
      {
        path: '',
        component: ExperimentsListComponent,
      },
      /* Report compare */
      {
        path: 'compare',
        component: ReportComponent,
      },
      /* Experiment page */
      {
        path: ':expId',
        component: ExperimentComponent,
        canActivate: [ControlCorrectExperimentGuard]
      },
      /* Report page */
      {
        path: ':expId/report',
        component: ReportComponent,
        canActivate: [ControlCorrectExperimentGuard]
      }
    ]
    , canActivate: [AuthGuard]
  },

  /* Login page */
  { path: 'login', component: LoginComponent, canActivate: [AuthGuard] },

  /* Error pages */
  // { path: 'errorpage', component: ErrorPageComponent },
  // { path: '**', redirectTo: 'errorpage', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
