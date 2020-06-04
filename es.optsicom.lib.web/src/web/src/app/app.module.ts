import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

/* Routing */
import { AppRoutingModule } from './app-routing.module';

/* My Components */
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { ExperimentsListComponent } from './pages/experiments-list/experiments-list.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';

/* Shared Modules */
import { SharedModule } from './shared.module';

/* Services */
import { BreadcrumbService } from './common-components/breadcrumb/breadcrumb.service';
import { ExperimentComponent } from './pages/experiment/experiment.component';
import { ReportComponent } from './pages/report/report.component';
import { LoginComponent } from './pages/login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    ExperimentsListComponent,
    ExperimentComponent,
    ReportComponent,
    ErrorPageComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    /* Shared Modules */
    SharedModule
  ],
  providers: [
    BreadcrumbService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
