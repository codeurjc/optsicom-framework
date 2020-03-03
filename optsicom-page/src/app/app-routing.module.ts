import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Components */
import { ExperimentsListComponent } from './pages/experiments-list/experiments-list.component';

const routes: Routes = [
  /* Redirect principal page */
  { path: '', redirectTo: 'experiments', pathMatch: 'full' },

  /* Experiment List page */
  { path: 'experiments', component: ExperimentsListComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
