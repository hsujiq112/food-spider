import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { AddRestaurantRequest } from 'src/app/request-base';
import { SharedService } from 'src/app/shared.service';
import {ENTER, COMMA} from '@angular/cdk/keycodes';
import { CategoryEnum } from 'src/app/api-model';

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
  categories = new FormControl();
  deliveryZones: string[] = [];
  removable: boolean = true;
  separatorKeysCodes = [ENTER, COMMA];
  categoriesList: string[] = ["Breakfast", "Lunch", "Dinner", "Dessert", "Beverages", "Trending"]
  ngOnInit(): void {
    this.restaurantForm = this.formBuilder.group({
      restaurantName: ['', Validators.required],
      restaurantLocation: ['', Validators.required],
    });
  }

  close(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.restaurantForm.markAllAsTouched();
    if (!this.restaurantForm.valid || this.deliveryZones.length == 0) {
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
      }
    }
    this.request.deliveryZones = this.deliveryZones.join(",");
    this.request.categories = this.categories.value;
    this.dialogRef.close(this.request);
  }

  add(event: MatChipInputEvent) {
    let input = event.input;
    let value = event.value;

    if ((value || '').trim()) {
      this.deliveryZones.push(value.trim());
    }

    if (input) {
      input.value = '';
    }
  }

  remove(deliveryZone: string) {

    let index = this.deliveryZones.indexOf(deliveryZone);

    if (index >= 0) {
      this.deliveryZones.splice(index, 1);
    }
  }
}