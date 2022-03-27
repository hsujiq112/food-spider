import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginResponse, ResponseBase } from './response-base';
import { UserModel } from './api-model';
import { LoginRequest, RegisterRequest } from './request-base';


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
    this._snackBar.open(message, action, {
      duration: 2000,
      verticalPosition: 'top',});
  }
    
  login(loginModel: LoginRequest) {
    return this.http.post<LoginResponse>(this.APIUrl+'/login', loginModel, {observe: 'response'});
  }

  register(registerModel: RegisterRequest) {
    return this.http.post<ResponseBase>(this.APIUrl+'/register', registerModel, {observe: 'response'});
  }
  
}
