import { Component, OnInit } from '@angular/core';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { SharedService } from 'src/app/shared.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddFoodItemRequest, AddRestaurantRequest } from 'src/app/request-base';
import { NewRestaurantDialogComponent } from '../new-restaurant-dialog/new-restaurant-dialog.component';
import { CategoryEnum, NarrowedFoodItem } from 'src/app/api-model';
import { NewFoodItemDialogComponent } from '../new-food-item-dialog/new-food-item-dialog.component';
import { Router } from '@angular/router';
import { initializeLinq, IEnumerable, from } from 'linq-to-typescript';

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
    private router: Router
  ) {}

  tableSource: Array<NarrowedFoodItem>;
  categoriesToShow: Array<number>;
  get categoriesToShowText(): Array<string> {
    var categoriesText: Array<string> = [];
    this.categoriesToShow?.forEach(i => {
      switch(i) {
        case CategoryEnum.BREAKFAST:
            categoriesText = categoriesText.concat("Breakfast");
            break;
        case CategoryEnum.LUNCH:
          categoriesText = categoriesText.concat("Lunch");
          break;
        case CategoryEnum.DINNER:
          categoriesText = categoriesText.concat("Dinner");
          break;
        case CategoryEnum.DESSERT:
          categoriesText = categoriesText.concat("Dessert");
          break;
        case CategoryEnum.BEVERAGES:
          categoriesText = categoriesText.concat("Beverages");
          break;
        case CategoryEnum.TRENDING:
          categoriesText = categoriesText.concat("Trending");
          break;
        default:
          categoriesText = categoriesText.concat("-");
          break;
      }
    })
    return categoriesText;
  }

  tableSourceByCategory(category: number) {
    return from(this.tableSource).where(i => i.category.toString() == CategoryEnum[this.categoriesToShow[category]]).toArray();
  }

  ngOnInit(): void {
    initializeLinq();
    if (!this.service.user || !this.service.isAdmin) {
      this.service.openSnackBar("You shouldn't be here!", "Sorry");
      setTimeout(() => this.router.navigate(['/landing-page']), 1000);
      return;
    }
    this.service.getRestaurantByAdminID(this.service.user.id).subscribe(response => {
      if (response.status == 204) {
        this.showAddRestaurantPopup();
      } else {
        if (!response.body) {
          return;
        }
        this.service.user.restaurant = response.body?.narrowedRestaurant;
        this.getRestaurantMenu(this.service.user.restaurant.id);
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
      if (!response.body) {
        this.service.openSnackBar("Catastrophic Failure", "Ok?");
        return;
      }
      this.categoriesToShow = response.body.categories;
      this.tableSource = response.body.foodItems;
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

  addFoodItem(category: number) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.width = "500px";
    var request = new AddFoodItemRequest();
    dialogConfig.data = request;
    const dialogRef = this.dialog.open(NewFoodItemDialogComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((request: AddFoodItemRequest) => {
      if (request == new AddFoodItemRequest() || !request) {
        return;
      }
      request.restaurantID = this.service.user.restaurant.id;
      request.categoryEnum = this.categoriesToShow[category];
      this.service.addFoodItemToRestaurant(request).subscribe(_ => {
        this.service.openSnackBar("Food Item was successfully added", "Close");
        this.getRestaurantMenu(this.service.user.restaurant.id);
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

  isInCategory(foodItem: NarrowedFoodItem, category: number) {
    return CategoryEnum[this.categoriesToShow[category]] == foodItem.category.toString();
  }

  redirectPageTo(category: string) {
    document.getElementById(category)?.scrollIntoView();
  }


}
