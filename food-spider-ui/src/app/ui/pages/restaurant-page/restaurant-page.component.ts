import { AfterViewInit, Component, EventEmitter, Inject, Injectable, Input, OnChanges, OnInit, Output, ViewContainerRef } from '@angular/core';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { SharedService } from 'src/app/shared.service';
import { MatDialog, MatDialogConfig, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AddFoodItemRequest, AddOrderRequest, AddRestaurantRequest } from 'src/app/request-base';
import { NewRestaurantDialogComponent } from '../new-restaurant-dialog/new-restaurant-dialog.component';
import { CategoryEnum, DisplayEmitter, DisplayTypeEnum, NarrowedFoodItem } from 'src/app/api-model';
import { NewFoodItemDialogComponent } from '../new-food-item-dialog/new-food-item-dialog.component';
import { ActivatedRoute, Router } from '@angular/router';
import { initializeLinq, IEnumerable, from } from 'linq-to-typescript';
import { NewOrderDialogComponent } from '../new-order-dialog/new-order-dialog.component';
import { YesNoDialogComponent } from '../yes-no-dialog/yes-no-dialog.component';

@Component({
  selector: 'restaurant-page',
  templateUrl: './restaurant-page.component.html',
  styleUrls: ['./restaurant-page.component.css']
})
@Injectable()
export class MyRestaurantComponent implements OnInit, OnChanges {

  constructor(
    private service: SharedService,
    private dialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  tableSource: Array<NarrowedFoodItem>;
  categoriesToShow: Array<number>;
  restaurantID: string;
  potentialOrderFoodItems: Array<NarrowedFoodItem> = new Array<NarrowedFoodItem>();
  @Input() displayType: DisplayEmitter;
  @Output() buttonPressed = new EventEmitter<MouseEvent>();

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
    if (!!this.route.snapshot.data && this.route.snapshot.data.displayType == DisplayTypeEnum.ADMIN) {
      this.initAdmin();
    }
  }

  ngOnChanges(): void {
    if (!this.displayType) {
      return;
    }
    switch(this.displayType.displayType) {
      case DisplayTypeEnum.ADMIN:
        this.initAdmin();
        break;
      case DisplayTypeEnum.VIEW:
        this.initView();
        break;
      case DisplayTypeEnum.ORDER:
        this.initOrder();
        break;
    }
  }

  initAdmin() {
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
        this.restaurantID = this.service.user.restaurant.id;
        this.getRestaurantMenu();
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

  initView() {
    if (!this.service.user || this.service.isAdmin) {
      this.service.openSnackBar("You shouldn't be here!", "Sorry");
      setTimeout(() => this.router.navigate(['/landing-page']), 1000);
      return;
    }
    this.restaurantID = this.displayType.restaurantID;
    this.getRestaurantMenu();
  }

  initOrder() {
    if (!this.service.user || this.service.isAdmin) {
      this.service.openSnackBar("You shouldn't be here!", "Sorry");
      setTimeout(() => this.router.navigate(['/landing-page']), 1000);
      return;
    }
    this.restaurantID = this.displayType.restaurantID;
    this.getRestaurantMenu();
  }

  getRestaurantMenu() {
    if (!this.restaurantID) {
      this.service.openSnackBar("Oh no, something bad happened", ":(");
      return;
    }
    this.service.getMenuByRestaurantID(this.restaurantID).subscribe(response => {
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
    dialogConfig.minWidth = "50vw";
    dialogConfig.maxWidth = "70vw";
    dialogConfig.maxHeight = '80vh';
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
        this.service.user.restaurant = response.body.restaurant;
        this.restaurantID = response.body.restaurant.id;
        this.service.openSnackBar("Yay! Now you have a restaurant", "Super!");
        this.getRestaurantMenu();
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
    dialogConfig.minWidth = "50vw";
    dialogConfig.maxWidth = "70vw";
    dialogConfig.maxHeight = '80vh';
    var request = new AddFoodItemRequest();
    dialogConfig.data = request;
    const dialogRef = this.dialog.open(NewFoodItemDialogComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((request: AddFoodItemRequest) => {
      if (request == new AddFoodItemRequest() || !request) {
        return;
      }
      request.categoryEnum = this.categoriesToShow[category];
      request.restaurantID = this.service.user.restaurant.id;
      this.service.addFoodItemToRestaurant(request).subscribe(response => {
        this.service.openSnackBar("Food Item was successfully added", "Close");
        this.getRestaurantMenu();
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

  displayTypeAsNumber() {
    if (!this.displayType) {
      return 0;
    }
    return this.displayType?.displayType.valueOf();
  }

  destroyChild($event: MouseEvent) {
    if (this.displayType?.displayType === DisplayTypeEnum.ORDER && this.potentialOrderFoodItems.length != 0) {
      const dialogConfig = new MatDialogConfig();
      dialogConfig.autoFocus = true;
      dialogConfig.minWidth = "50vw";
      dialogConfig.maxWidth = "70vw";
      dialogConfig.maxHeight = '80vh';
      dialogConfig.data = "Are you sure you want to close this restaurant? Your order will be lost!";
      const dialogRef = this.dialog.open(YesNoDialogComponent, dialogConfig);
      dialogRef.afterClosed().subscribe((response : boolean) => {
        if (!response) {
          return;
        }
        this.buttonPressed.emit($event);
      });
    } else {
      this.buttonPressed.emit($event);
    }
  }
  
  addItemForPotentialOrder(element: NarrowedFoodItem) {
    this.potentialOrderFoodItems = this.potentialOrderFoodItems.concat(element);
  }

  showOrderPopup() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.minWidth = "50vw";
    dialogConfig.maxWidth = "70vw";
    dialogConfig.maxHeight = '80vh';
    var foodItems = new Array<NarrowedFoodItem>();
    foodItems = this.potentialOrderFoodItems;
    dialogConfig.data = foodItems;
    const dialogRef = this.dialog.open(NewOrderDialogComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((request: AddOrderRequest) => {
      if (request == new AddOrderRequest() || !request) {
        return;
      }
      request.userId = this.service.user.id;
      this.service.placeOrder(request).subscribe(response => {
        if (response.status == 204) {
          this.service.openSnackBar("Successfully placed order!", "Yay");
          return;
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
    });
  }

}
