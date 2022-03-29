import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AddRestaurantResponse, GetRestaurantByAdminIDResponse, GetRestaurantsResponse, LoginResponse, MenuItemsResponse, ResponseBase } from './response-base';
import { NarrowedResaurant, UserModel } from './api-model';
import { AddFoodItemRequest, AddOrderRequest, AddRestaurantRequest, LoginRequest, RegisterRequest } from './request-base';


@Injectable({
  providedIn: 'root'
})

export class SharedService {
  readonly APIUrl = "http://localhost:8080/api/v1";
  
  isLoggedIn: boolean = false;
  user: UserModel;
  isAdmin: boolean = false;
  
  constructor(private http:HttpClient, private _snackBar: MatSnackBar) {

  }
  
  openSnackBar(message: string, action: string) {
    var snack = this._snackBar.open(message, action, {
      duration: 2000,
      verticalPosition: 'top',});
    snack.onAction().subscribe(() => {
      snack.dismiss();
    });
  }
    
  login(loginModel: LoginRequest) {
    return this.http.post<LoginResponse>(this.APIUrl + '/login', loginModel, {observe: 'response'});
  }

  register(registerModel: RegisterRequest) {
    return this.http.post<ResponseBase>(this.APIUrl + '/register', registerModel, {observe: 'response'});
  }

  getMenuByRestaurantID(restaurantID: string) {
    return this.http.get<MenuItemsResponse>(this.APIUrl + '/getMenuByRestaurantID/' + restaurantID, {observe: 'response'});
  }

  addRestaurantToAdmin(request: AddRestaurantRequest) {
    return this.http.post<AddRestaurantResponse>(this.APIUrl + '/addRestaurantToAdmin', request, {observe: 'response'});
  }

  addFoodItemToRestaurant(request: AddFoodItemRequest) {
    return this.http.post<ResponseBase>(this.APIUrl + '/addFoodToCategory', request, {observe: 'response'});
  }

  deleteFoodItemById(id: String) {
    return this.http.delete<ResponseBase>(this.APIUrl + '/foodItem/' + id, {observe: 'response'});
  }

  placeOrder(request: AddOrderRequest) {
    return this.http.post<ResponseBase>(this.APIUrl + '/placeOrder', request, {observe: 'response'});
  }

  getRestaurantByAdminID(adminID: string) {
    return this.http.get<GetRestaurantByAdminIDResponse>(this.APIUrl + '/getRestaurantByAdminID/' + adminID, {observe: 'response'});
  }

  getRestaurants() {
    return this.http.get<GetRestaurantsResponse>(this.APIUrl + '/restaurants', {observe: 'response'});
  }
  
}
