import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'yes-no-dialog-page',
  templateUrl: './yes-no-dialog.component.html',
  styleUrls: ['./yes-no-dialog.component.css']
})
export class YesNoDialogComponent implements OnInit {
  
  constructor(
    @Inject(MAT_DIALOG_DATA) public message: string,
    private dialogRef: MatDialogRef<YesNoDialogComponent>
  ) {}

  ngOnInit(): void {
  }

  yes(): void {
    this.dialogRef.close(true);
  }

  no(): void {
    this.dialogRef.close(false);
  }

}
