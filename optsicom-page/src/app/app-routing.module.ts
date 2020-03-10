import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Components */
import { ExperimentsListComponent } from './pages/experiments-list/experiments-list.component';
import { ExperimentComponent } from './pages/experiment/experiment.component';
import { ReportComponent } from './pages/report/report.component';
import { LoginComponent } from './pages/login/login.component';

/* Guards */
import { AuthGuard } from './guard/auth.guard';

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
      },
      /* Experiment single report page */
      {
        path: ':experimentId/report',
        component: ReportComponent,
      },
    ]
    , canActivate: [AuthGuard]
  },

  /* Login page */
  { path: 'login', component: LoginComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
