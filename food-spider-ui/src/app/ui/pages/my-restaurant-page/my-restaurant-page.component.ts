import { Component, OnInit } from '@angular/core';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { SharedService } from 'src/app/shared.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddRestaurantRequest } from 'src/app/request-base';
import { NewRestaurantDialogComponent } from '../new-restaurant-dialog/new-restaurant-dialog.component';

@Component({
  selector: 'my-restaurant-page',
  templateUrl: './my-restaurant-page.component.html',
  styleUrls: ['./my-restaurant-page.component.css']
})
export class MyRestaurantComponent implements OnInit {

  constructor(
    private _bottomSheet: MatBottomSheet,
    private service: SharedService,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    if (!this.service.user || !this.service.isAdmin) {
      this.service.openSnackBar("You shouldn't be here!", "Sorry");
      return;
    }
    this.service.getRestaurantByAdminID(this.service.user.id).subscribe(response => {
      if (response.status == 204) {
        this.showAddRestaurantPopup();
      } else {
        this.getRestaurantMenu(!!response.body?.narrowedRestaurant ? response.body?.narrowedRestaurant.id : '');
      }
    }, responseError => {
      if (responseError.error.isError && !!responseError.error.errorMessage) {
        this.service.openSnackBar(responseError.error.errorMessage, "Close");
        return;
      } else {
        this.service.openSnackBar("Catastrophic Failure", "Ok?");
        return;
      }
    });
  }

  getRestaurantMenu(restaurantID: string) {
    if (restaurantID == '') {
      this.service.openSnackBar("Oh no, something bad happened", ":(");
      return;
    }
    this.service.getMenuByRestaurantID(restaurantID).subscribe(response => {
      //console.log(response.foodItems);
      // LIST ALL THE CATEGORIES AND THEIR RESPECTIVE FOODS!!!!!
    })
  }

  showAddRestaurantPopup() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.width = "500px";
    var request = new AddRestaurantRequest();
    dialogConfig.data = request;
    const dialogRef = this.dialog.open(NewRestaurantDialogComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((request: AddRestaurantRequest) => {
      if (request == new AddRestaurantRequest() || !request) {
        this.showAddRestaurantPopup();
        return;
      }
      request.adminID = this.service.user.id;
      this.service.addRestaurantToAdmin(request).subscribe(response => {
        if (!response.body?.restaurant) {
          this.service.openSnackBar("Something bad just happened", "Oh no!");
          return;
        } 
        this.service.user.restaurant = response.body?.restaurant;
        this.service.openSnackBar("Yay! Now you have a restaurant", "Super!");
        this.getRestaurantMenu(response.body.restaurant.id);
      }, responseError => {
        if (responseError.error.isError && !!responseError.error.errorMessage) {
          this.service.openSnackBar(responseError.error.errorMessage, "Close");
          return;
        } else {
          this.service.openSnackBar("Catastrophic Failure", "Ok?");
          return;
        }
      });
    });
  }

}
