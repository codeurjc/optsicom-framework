import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

export interface Data {
  message: string,
  buttonYes: string,
  buttonNo: string
}

@Component({
  selector: 'yes-no-dialog',
  templateUrl: './yes-no-dialog.component.html',
  styleUrls: ['./yes-no-dialog.component.scss']
})
export class YesNoDialogComponent {
  constructor(public dialogRef: MatDialogRef<YesNoDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public messageYesNo: Data) {
    if (this.messageYesNo.buttonYes == undefined || this.messageYesNo.buttonYes == '') {
      this.messageYesNo.buttonYes = 'aceptar';
    }

    if (this.messageYesNo.buttonNo == undefined || this.messageYesNo.buttonNo == '') {
      this.messageYesNo.buttonNo = 'cancelar';
    }
  }
}