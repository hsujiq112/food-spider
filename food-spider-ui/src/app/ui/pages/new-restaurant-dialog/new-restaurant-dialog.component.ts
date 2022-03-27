import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AddRestaurantRequest } from 'src/app/request-base';
import { SharedService } from 'src/app/shared.service';

@Component({
  selector: 'new-restaurant-dialog-page',
  templateUrl: './new-restaurant-dialog.component.html',
  styleUrls: ['./new-restaurant-dialog.component.css']
})
export class NewRestaurantDialogComponent implements OnInit {
  
  constructor(
    private service: SharedService,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public request: AddRestaurantRequest,
    private dialogRef: MatDialogRef<NewRestaurantDialogComponent>
  ) {}
  
  restaurantForm: FormGroup;
  action: string;

  ngOnInit(): void {
    this.restaurantForm = this.formBuilder.group({
      restaurantName: [this.request.name, Validators.required],
      restaurantLocation: [this.request.location, Validators.required],
      restaurantDeliveryZones: [this.request.deliveryZones, Validators.required],
    });
  }

  close(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.restaurantForm.markAllAsTouched();
    if (!this.restaurantForm.valid) {
      return;
    }
    for (const field in this.restaurantForm.controls) {
      const control = this.restaurantForm.get(field);
      switch(field) {
        case "restaurantName":
          this.request.name = control?.value;
          break;
        case "restaurantLocation":
          this.request.location = control?.value;
          break;
        case "restaurantDeliveryZones":
          this.request.deliveryZones = control?.value;
          break;
      }
    }
    this.dialogRef.close(this.request);
  }

}
