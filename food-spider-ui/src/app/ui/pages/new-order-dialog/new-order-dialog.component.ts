import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SharedService } from 'src/app/shared.service';
import {ENTER, COMMA, SPACE} from '@angular/cdk/keycodes';
import { AddOrderRequest } from 'src/app/request-base';
import { NarrowedFoodItem } from 'src/app/api-model';

@Component({
  selector: 'new-order-dialog-page',
  templateUrl: './new-order-dialog.component.html',
  styleUrls: ['./new-order-dialog.component.css']
})
export class NewOrderDialogComponent implements OnInit {
  
  constructor(
    private service: SharedService,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<NewOrderDialogComponent>
  ) {}
  
  dialogData = {
    foodItems: Array<NarrowedFoodItem>(),
    isForView: true
  }

  orderDetailsForm: FormGroup;

  ngOnInit(): void {
    this.dialogData.foodItems = this.data.foodItems;
    this.dialogData.isForView = this.data.isForView;
    this.orderDetailsForm = this.formBuilder.group({
      address: ['', [Validators.required]],
      specialDetails: ['']
    });
  }

  get computePrice(): number {
    var price = 0.0;
    this.dialogData.foodItems.forEach(i => price += i.price);
    return price;
  }

  close(): void {
    this.dialogRef.close(null);
  }

  order(): void {
    this.orderDetailsForm.markAllAsTouched();
    if(!this.orderDetailsForm.valid) {
      this.service.openSnackBar("ERROR: Please fix the issues before pressing the button", "OK");
      return;
    }
    if (this.dialogData.foodItems.length === 0) {
      this.service.openSnackBar("You cannot place an order with 0 items", "Oh :(");
      return;
    }
    var request = new AddOrderRequest();
    for (const field in this.orderDetailsForm.controls) {
      const control = this.orderDetailsForm.get(field); 
      switch(field) {
        case "address":
          request.address = control?.value;
          break;
        case "specialDetails":
          request.specialDetails = control?.value;
          break;
      }
    }
    request.foodItems = this.dialogData.foodItems.map(i => i.id);
    this.dialogRef.close(request);
  }

  remove(i: number): void {
    this.dialogData.foodItems.splice(i, 1);
  }


}