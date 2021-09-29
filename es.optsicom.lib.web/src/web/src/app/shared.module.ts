import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

/* Flex Layout */
import { FlexLayoutModule } from '@angular/flex-layout';

/* HTTP Interceptor */
import { XMLRequestInterceptor } from "./http-interceptor/xmlrequest-interceptor";
import { ErrorInterceptor } from "./http-interceptor/error-interceptor";

/* Pipes */
import { ShowExperimentMode } from './pipes/experiment-mode';
import { ShowRestCell } from './pipes/cell-values';

/* Material Components */
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
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
import { MatMenuModule} from '@angular/material/menu';

/* MatDialogs */
import { YesNoDialogComponent } from './common-dialogs/yes-no-dialog/yes-no-dialog.component';

/* Common components */
import { BreadcrumbComponent } from './common-components/breadcrumb/breadcrumb.component';
import { TableComponent } from './common-components/table/table.component';

/* Services */
import { LastPageVisitedService } from './services/last-page-visited.service';

@NgModule({
  declarations: [
    /* Common components */
    BreadcrumbComponent,
    TableComponent,
    /* MatDialogs */
    YesNoDialogComponent,
    /* Pipes */
    ShowExperimentMode,
    ShowRestCell
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    /* FlexLayout */
    FlexLayoutModule,
    /* Material Components */
    MatToolbarModule,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
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
    MatListModule,
    MatMenuModule
  ], exports: [
    /* Common components */
    BreadcrumbComponent,
    TableComponent,
    /* Pipes */
    ShowExperimentMode,
    ShowRestCell,
    /* MatDialogs */
    YesNoDialogComponent,
    /* FlexLayout */
    FlexLayoutModule,
    /* Material Components */
    MatToolbarModule,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
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
    MatListModule,
    MatMenuModule
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
    },
    /* Save the last page visited */
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true,
    },
    /* Last Page Visited Service */
    LastPageVisitedService
  ]
})
export class SharedModule { }
