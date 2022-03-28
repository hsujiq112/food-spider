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
    @Inject(MAT_DIALOG_DATA) public foodItems: Array<NarrowedFoodItem>,
    private dialogRef: MatDialogRef<NewOrderDialogComponent>
  ) {}
  

  ngOnInit(): void {
    
  }

  get computePrice(): number {
    var price = 0.0;
    this.foodItems.forEach(i => price += i.price);
    return price;
  }

  close(): void {
    this.dialogRef.close(null);
  }

  order(): void {
    if (this.foodItems.length === 0) {
      this.service.openSnackBar("You cannot place an order with 0 items", "Oh :(");
      return;
    }
    var request = new AddOrderRequest();
    request.foodItems = this.foodItems.map(i => i.id);
    this.dialogRef.close(request);
  }

  remove(i: number): void {
    this.foodItems.splice(i, 1);
  }


}