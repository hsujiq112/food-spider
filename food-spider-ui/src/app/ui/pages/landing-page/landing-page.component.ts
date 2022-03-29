import { AfterViewInit, Component, OnInit } from '@angular/core';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { SharedService } from 'src/app/shared.service';
import { LoginPageComponent } from '../login-page/login-page.component'

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {

  constructor(
    private _bottomSheet: MatBottomSheet,
    private service: SharedService,
    ) {}

  get restaurants(): string {
    return !this.isAdmin ? "Browse Restaurants" : "My Restaurant";
  }

  get orders(): string {
    return !this.isAdmin ? "Get My Orders" : "Inspect Orders";
  }

  get restaurantRoute(): string {
    return !this.isAdmin ? "/all-restaurants-page" : "/my-restaurant-page";
  }

  get ordersRoute(): string {
    return "/orders-page";
  }
  
  get loggedIn(): boolean {
    return this.service.isLoggedIn;
  }

  get isAdmin(): boolean {
    if (!this.service.user) {
      return false;
    }
    if (!this.service.isAdmin) {
      return false;
    }
    return true;
  }

  openLoginPage(): void {
    this._bottomSheet.open(LoginPageComponent);
  }

  ngOnInit(): void {
    if (!this.service.user){
      setTimeout(() => this._bottomSheet.open(LoginPageComponent), 1000);
    }
  }

}
