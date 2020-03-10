import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

/* Flex Layout */
import { FlexLayoutModule } from '@angular/flex-layout';

/* HTTP Interceptor */
import { XMLRequestInterceptor } from "./http-interceptor/xmlrequest-interceptor";

/* Pipes */
import { ShowExperimentMode } from './pipes/experiment-mode';

/* Material Components */
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';

/* MatDialogs */
import { YesNoDialogComponent } from './common-dialogs/yes-no-dialog/yes-no-dialog.component';

/* Common components */
import { BreadcrumbComponent } from './common-components/breadcrumb/breadcrumb.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

@NgModule({
  declarations: [
    /* Common components */
    BreadcrumbComponent,
    /* MatDialogs */
    YesNoDialogComponent,
    /* Pipes */
    ShowExperimentMode
  ],
  imports: [
    CommonModule,
    /* FlexLayout */
    FlexLayoutModule,
    /* Material Components */
    MatToolbarModule,
    MatCardModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatCheckboxModule,
    MatButtonToggleModule,
    MatSelectModule,
    MatDialogModule,
    MatSnackBarModule,
    MatSortModule,
    MatInputModule,
    MatDividerModule,
    MatListModule
  ], exports: [
    /* Common components */
    BreadcrumbComponent,
    /* Pipes */
    ShowExperimentMode,
    /* MatDialogs */
    YesNoDialogComponent,
    /* FlexLayout */
    FlexLayoutModule,
    /* Material Components */
    MatToolbarModule,
    MatCardModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatCheckboxModule,
    MatButtonToggleModule,
    MatSelectModule,
    MatDialogModule,
    MatSnackBarModule,
    MatSortModule,
    MatInputModule,
    MatDividerModule,
    MatListModule
  ], entryComponents: [
    /* MatDialogs */
    YesNoDialogComponent
  ],
  providers: [
    /* XMLHttpRequest inject in all petitions */
    {
      provide: HTTP_INTERCEPTORS,
      useClass: XMLRequestInterceptor,
      multi: true,
    }
  ]
})
export class SharedModule { }
