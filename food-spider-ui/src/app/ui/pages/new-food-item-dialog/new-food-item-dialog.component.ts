import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SharedService } from 'src/app/shared.service';
import { AddFoodItemRequest } from 'src/app/request-base';

@Component({
  selector: 'new-food-item-dialog-page',
  templateUrl: './new-food-item-dialog.component.html',
  styleUrls: ['./new-food-item-dialog.component.css']
})
export class NewFoodItemDialogComponent implements OnInit {
  
  constructor(
    private service: SharedService,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public request: AddFoodItemRequest,
    private dialogRef: MatDialogRef<NewFoodItemDialogComponent>
  ) {}
  
  foodItemForm: FormGroup;
  ngOnInit(): void {
    this.foodItemForm = this.formBuilder.group({
      foodName: ['', Validators.required],
      foodDescription: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(1)]],
      foodImageLink: ['', Validators.required],
    });
  }

  close(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.foodItemForm.markAllAsTouched();
    if (!this.foodItemForm.valid) {
      return;
    }
    for (const field in this.foodItemForm.controls) {
      const control = this.foodItemForm.get(field);
      switch(field) {
        case "foodName":
          this.request.foodName = control?.value;
          break;
        case "foodDescription":
          this.request.foodDescription = control?.value;
          break;
        case "price":
          this.request.price = control?.value;
          break;
        case "foodImageLink":
          this.request.foodImageLink = control?.value;
          break;
      }
    }
    this.dialogRef.close(this.request);
  }
}