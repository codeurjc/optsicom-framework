import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';

/* Routing */
import { AppRoutingModule } from './app-routing.module';

/* My Components */
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { ExperimentsListComponent } from './pages/experiments-list/experiments-list.component';

/* Shared Modules */
import { SharedModule } from './shared.module';

/* Services */
import { BreadcrumbService } from './breadcrumb/breadcrumb.service';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    ExperimentsListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
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
