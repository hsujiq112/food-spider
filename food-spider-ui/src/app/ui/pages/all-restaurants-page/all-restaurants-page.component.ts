import { Component, EventEmitter, OnInit, Output, ViewChild, ViewContainerRef } from '@angular/core';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { SharedService } from 'src/app/shared.service';
import { MatDialog } from '@angular/material/dialog';
import { CategoryEnum, DisplayEmitter, DisplayTypeEnum, NarrowedResaurant } from 'src/app/api-model';
import { ActivatedRoute, Router } from '@angular/router';
import { initializeLinq } from 'linq-to-typescript';

@Component({
  selector: 'all-restaurants-page',
  templateUrl: './all-restaurants-page.component.html',
  styleUrls: ['./all-restaurants-page.component.css']
})
export class RestaurantsComponent implements OnInit {

  constructor(
    private _bottomSheet: MatBottomSheet,
    private service: SharedService,
    private dialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  tableSource: Array<NarrowedResaurant>;
  displayedColumns: string[] = ['name', 'location', 'deliveryZones', 'categories', 'action'];
  toDisplay: boolean = false;
  restaurantID: string;
  restaurantDisplayType: DisplayEmitter = new DisplayEmitter();

  ngOnInit(): void {
    initializeLinq();
    if (!this.service.user || this.service.isAdmin) {
      this.service.openSnackBar("You shouldn't be here!", "Sorry");
      setTimeout(() => this.router.navigate(['/landing-page']), 1000);
      return;
    }
    this.service.getRestaurants().subscribe(response => {
      if (!response.body) {
        this.service.openSnackBar("Uhm... where restaurants", "???");
        return;
      }
      this.tableSource = response.body.restaurants;
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

  categoryText(categories: Array<number>): string {
    var categoriesText: Array<string> = [];
    categories.forEach(i => {
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
    return categoriesText.join(', ');
  }

  view(element: NarrowedResaurant) {
    this.restaurantDisplayType.restaurantID = element.id;
    this.restaurantDisplayType.displayType = DisplayTypeEnum.VIEW;
    this.toDisplay = true;
  }

  order(element: NarrowedResaurant) {
    this.restaurantDisplayType.restaurantID = element.id;
    this.restaurantDisplayType.displayType = DisplayTypeEnum.ORDER;
    this.toDisplay = true;
  }

  destroyChild() {
    this.toDisplay = false;
  }

}
