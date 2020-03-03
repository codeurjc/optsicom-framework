import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

/* Flex Layout */
import { FlexLayoutModule } from '@angular/flex-layout';

/* Material Components */
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';

/* Common components */
import { BreadcrumbComponent } from './breadcrumb/breadcrumb.component';

@NgModule({
  declarations: [
    /* Common components */
    BreadcrumbComponent
  ],
  imports: [
    CommonModule,
    /* FlexLayout */
    FlexLayoutModule,
    /* Material Components */
    MatToolbarModule,
    MatCardModule
  ], exports: [
    /* Common components */
    BreadcrumbComponent,
    /* FlexLayout */
    FlexLayoutModule,
    /* Material Components */
    MatToolbarModule,
    MatCardModule
  ]
})
export class SharedModule { }
