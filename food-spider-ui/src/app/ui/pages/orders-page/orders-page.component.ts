import { Component, Injectable, OnInit } from '@angular/core';
import { SharedService } from 'src/app/shared.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddRestaurantRequest, ChangeStatusToOrderRequest } from 'src/app/request-base';
import { NewRestaurantDialogComponent } from '../new-restaurant-dialog/new-restaurant-dialog.component';
import { ActivatedRoute, Router } from '@angular/router';
import { initializeLinq } from 'linq-to-typescript';
import { NarrowedFoodItem, NarrowedOrder, OrderStatusEnum } from 'src/app/api-model';
import { NewOrderDialogComponent } from '../new-order-dialog/new-order-dialog.component';

@Component({
  selector: 'orders-page',
  templateUrl: './orders-page.component.html',
  styleUrls: ['./orders-page.component.css']
})

export class OrdersPageComponent implements OnInit {

  constructor(
    private service: SharedService,
    private dialog: MatDialog,
    private router: Router,
  ) {}

  tableSource: Array<NarrowedOrder>;
  displayedColumns: string[] = ['client', 'price', 'status', 'action'];
  selected: number;

  ngOnInit(): void {
    initializeLinq();
    if (!this.service.user) {
      if (!this.service.user || !this.service.isAdmin) {
        this.service.openSnackBar("You shouldn't be here!", "Sorry");
        setTimeout(() => this.router.navigate(['/landing-page']), 1000);
        return;
      }
    }
    if (this.service.isAdmin) {
      this.displayedColumns = ['client', 'price', 'status', 'seeFood', 'changeStatus'];
    } else {
      this.displayedColumns = ['restaurant', 'price', 'status', 'seeFood']
    }
    this.getOrders();
  }

  getOrders() {
    this.service.getOrdersByUserID(this.service.user.id, this.service.isAdmin).subscribe(response => {
      if (!response.body) {
        this.service.openSnackBar("Oh no!", "Ikr?");
        return;
      }
      this.tableSource = this.resolveParameters(response.body.orders);
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

  getOrdersFiltered() {
    this.service.getOrdersByUserIDFiltered(this.service.user.id, this.service.isAdmin, this.selected).subscribe(response => {
      if (!response.body) {
        this.service.openSnackBar("Oh no!", "Ikr?");
        return;
      }
      this.tableSource = this.resolveParameters(response.body.orders);
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

  getDisplayProcessStatus(status: OrderStatusEnum): string {
    switch(status) {
      case OrderStatusEnum.PENDING:
        return "pending";
      case OrderStatusEnum.ACCEPTED:
        return "accepted";
      case OrderStatusEnum.IN_DELIVERY:
        return "in-delivery";
      case OrderStatusEnum.DELIVERED:
        return "delivered";
      case OrderStatusEnum.DECLINED:
        return "declined";
      default:
        return "-";
    }
  }

  getDisplayProcessStatusText(status: OrderStatusEnum): string {
    switch(status) {
      case OrderStatusEnum.PENDING:
        return "Pending";
      case OrderStatusEnum.ACCEPTED:
        return "Accepted";
      case OrderStatusEnum.IN_DELIVERY:
        return "In Delivery";
      case OrderStatusEnum.DELIVERED:
        return "Delivered";
      case OrderStatusEnum.DECLINED:
        return "Declined";
      default:
        return "-";
    }
  }

  get getAllDisplayStatuses(): string[] {
    return ["Pending", "Accepted", "In Delivery", "Delivered", "Declined"];
  }

  convertStatusToNumber(status: string): number {
    switch(status) {
      case OrderStatusEnum.PENDING:
        return 0;
      case OrderStatusEnum.ACCEPTED:
        return 1;
      case OrderStatusEnum.IN_DELIVERY:
        return 2;
      case OrderStatusEnum.DELIVERED:
        return 3;
      case OrderStatusEnum.DECLINED:
        return 4;
      default:
        return 0;
    }
  }

  isPending(status: OrderStatusEnum) {
    return status == OrderStatusEnum.PENDING;
  }

  isDeclined(status: OrderStatusEnum) {
    return status == OrderStatusEnum.DECLINED;
  }

  isDelivered(status: OrderStatusEnum) {
    return status == OrderStatusEnum.DELIVERED;
  }

  onChange() {
    if (this.selected == undefined) {
      this.getOrders();
    } else {
      this.getOrdersFiltered();
    }
  }

  changeStatus(element: NarrowedOrder, status: string) {
    var request = new ChangeStatusToOrderRequest();
    request.orderID = element.id;
    if (status != '') {
      request.status = this.convertStatusToNumber(status);
    } else {
      request.status = this.convertStatusToNumber(element.status) + 1;
    }
    this.service.changeStatusToOrder(request).subscribe(response => {
      if (response.status != 204) {
        this.service.openSnackBar("Something wrong happened", ":(");
        return;
      } 
      this.service.openSnackBar("Successfully updated status", "Close");
      this.getOrders();
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

  showFoodItems(element: NarrowedOrder) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.minWidth = "50vw";
    dialogConfig.maxWidth = "70vw";
    dialogConfig.maxHeight = '80vh';
    var foodItems = new Array<NarrowedFoodItem>();
    foodItems = element.foodItems;
    dialogConfig.data = {
      foodItems: foodItems,
      isForView: true
    };
    this.dialog.open(NewOrderDialogComponent, dialogConfig);
  }

  resolveParameters(orders: Array<NarrowedOrder>): Array<NarrowedOrder> {
    orders.forEach(i => {
      i.$fullName = i.clientFirstName + " " + i.clientLastName;
      i.$price = i.foodItems.reduce(function(acc, i) {return acc + i.price}, 0);
      i.$displayProcessStatus = this.getDisplayProcessStatus(i.status);
      i.$displayProcessStatusText = this.getDisplayProcessStatusText(i.status);
    })
    return orders;
  }

}
