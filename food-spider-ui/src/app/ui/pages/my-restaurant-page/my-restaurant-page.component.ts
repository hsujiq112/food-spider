import { Component, OnInit } from '@angular/core';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { MatGridListModule } from '@angular/material/grid-list';
import { SharedService } from 'src/app/shared.service';
import { LoginPageComponent } from '../login-page/login-page.component'
import { SocialAuthService, GoogleLoginProvider, SocialUser, FacebookLoginProvider } from 'angularx-social-login';
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
    if (!this.service.user.restaurant) {
      this.showAddRestaurantPopup();
      return;
    }
    this.getRestaurantMenu(this.service.user.id);
  }

  getRestaurantMenu(adminID: string) {
    this.service.getMenuByRestaurantID(adminID).subscribe(response => {
      console.log(response.foodItems);
    })
  }

  showAddRestaurantPopup() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.width = "500px";
    var request = new AddRestaurantRequest();
    dialogConfig.data = request;
    const dialogRef = this.dialog.open(NewRestaurantDialogComponent, dialogConfig);
  }

}
