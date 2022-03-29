import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, NumberValueAccessor, Validators } from '@angular/forms';
import {MatBottomSheet, MatBottomSheetRef} from '@angular/material/bottom-sheet';
import { MatButton } from '@angular/material/button';
import {MatChipsModule} from '@angular/material/chips';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SocialAuthService, GoogleLoginProvider, SocialUser, FacebookLoginProvider } from 'angularx-social-login';
import { MatDivider } from '@angular/material/divider';
import { SharedService } from 'src/app/shared.service';
import { MustMatch } from 'src/app/custom-validator';
import { Subscriber, Subscription } from 'rxjs';
import { initializeLinq, IEnumerable, from } from 'linq-to-typescript';
import { LoginRequest, RegisterRequest } from 'src/app/request-base';
import { UserModel } from 'src/app/api-model';

@Component({
  selector: 'login-info-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {

  constructor(
    private _bottomSheetRef: MatBottomSheetRef<LoginPageComponent>, 
    private formBuilder: FormBuilder,
    private service: SharedService
    ) {
      this.loginForm = this.formBuilder.group({
        username: ['', [Validators.required]],
        password: ['', [Validators.required]]
      });
      this.registerForm = this.formBuilder.group({
        firstName: ['', [Validators.required]],
        lastName: ['', [Validators.required]],
        email: ['', [Validators.email, Validators.required]],
        username: ['', [Validators.required]],
        password: ['', [Validators.required]],
        confirmPass: ['', [Validators.required]],
        isAdmin: ['']
      },
      {
        validator: MustMatch("password", "confirmPass")
      });
  }
  
  loginForm: FormGroup;
  registerForm: FormGroup;
  isRegisterToggled: boolean = false;
  toggleText: string = "Toggle Register";
  buttonText: string = "Login";
  loggedIn: boolean = false;

  localUser = {
    isLoggedIn : false,
    username : "Not connected",
    userType : "N/A",
    userAvatar : "https://img.favpng.com/25/13/19/samsung-galaxy-a8-a8-user-login-telephone-avatar-png-favpng-dqKEPfX7hPbc6SMVUCteANKwj.jpg",
    placedOrders: 0,
    pendingOrders: 0
  }

  get ordersCountText(): string {
    return (this.service.isAdmin ? "Resaurant orders count: " : "Placed orders: ") + this.localUser.placedOrders;
  }

  get ordersPendingText(): string {
    return (this.service.isAdmin ? "Pending orders to approve: " : "Undelivered orders: ") + this.localUser.pendingOrders;
  }

  ngOnInit(): void {
    initializeLinq();
    this.localUser = {
      isLoggedIn : false,
      username : "Not Connected",
      userType : "N/A",
      userAvatar : "https://img.favpng.com/25/13/19/samsung-galaxy-a8-a8-user-login-telephone-avatar-png-favpng-dqKEPfX7hPbc6SMVUCteANKwj.jpg",
      placedOrders : 0,
      pendingOrders: 0
    }
    if (!!this.service.user?.username) {
      this.service.getOrdersCountByUserID(this.service.user.id, this.service.isAdmin).subscribe(response => {
        if (!response.body) {
          this.service.openSnackBar("Oh no.. something terribly wrong happened", ":O");
          return;
        }
        this.service.ordersCount = response.body.placedOrders;
        this.service.ordersPendingCount = response.body.pendingOrders;
        this.loggedIn = true;
        this.localUser = {
          isLoggedIn: true,
          userType: this.service.isAdmin ? "Admin" : "Customer",
          username: this.service.user.username,
          userAvatar: this.service.isAdmin ? "http://cdn.onlinewebfonts.com/svg/img_239979.png" :
          "http://cdn.onlinewebfonts.com/svg/img_552555.png",
          placedOrders: this.service.ordersCount,
          pendingOrders:this.service.ordersPendingCount,
        }
      }, responeError => {
        if (responeError.error.isError && !!responeError.error.errorMessage) {
          this.service.openSnackBar(responeError.error.errorMessage, "Close");
          return;
        } else {
          this.service.openSnackBar("Catastrophic Failure", "Ok?");
          return;
        }
      });
    }
  }

  dismiss(event: MouseEvent): void {
    this._bottomSheetRef.dismiss();
    event.preventDefault();
  }
  
  clickDismiss(): void {
    this._bottomSheetRef.dismiss();
  }


  onSubmit() {
    if (this.isRegisterToggled){
      this.registerForm.markAllAsTouched();
      if(!this.registerForm.valid) {
        this.service.openSnackBar("ERROR: Please fix the issues before pressing the button", "OK");
        return;
      }
      var registerRequest = new RegisterRequest();
      for (const field in this.registerForm.controls) {
        const control = this.registerForm.get(field); 
        switch(field) {
          case "firstName":
            registerRequest.firstName = control?.value;
            break;
          case "lastName":
            registerRequest.lastName = control?.value;
            break;
          case "email":
            registerRequest.emailAddress = control?.value;
            break;
          case "username":
            registerRequest.username = control?.value;
            break;
          case "password":
            registerRequest.password = control?.value;
            break;
          case "isAdmin":
            registerRequest.isAdmin = control?.value == '' ? false : control?.value;
            break;
        }
      }
      this.service.register(registerRequest).subscribe(_ => {
          var loginModel = new LoginRequest();
          loginModel.username = registerRequest.username;
          loginModel.password = registerRequest.password;
          this.login(loginModel, true);
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
    else{
      this.loginForm.markAllAsTouched();
      if (!this.loginForm.valid) {
        this.service.openSnackBar("ERROR: Please fix the issues before pressing the button", "OK");
        return;
      }
      var loginModel = new LoginRequest();
      for (const field in this.loginForm.controls) {
        const control = this.loginForm.get(field); 
        switch(field) {
          case "username":
            loginModel.username = control?.value;
            break;
          case "password":
            loginModel.password = control?.value;
            break;
        }
      }
      this.login(loginModel, false);
    }
  }

  login(loginModel: LoginRequest, newlyCreated: boolean) {
    this.service.login(loginModel).subscribe(response => {
      if (!response.body) {
        this.service.openSnackBar("Fatal Failure", ":(");
        return;
      }
      this.service.user = response.body.userBase;
      this.service.isAdmin = response.body.isAdmin;
      if (newlyCreated) {
        this.localUser = {
          isLoggedIn : true,
          username : this.service.user.username,
          userType : this.service.isAdmin ? "Admin" : "Customer",
          userAvatar : this.service.isAdmin ? "../../../../assets/administrator.png" :
          "../../../../assets/customer.png",
          placedOrders : 0,
          pendingOrders : 0
        }
        return;
      }
      this.service.getOrdersCountByUserID(response.body.userBase.id, this.service.isAdmin).subscribe(response => {
        if (!response.body) {
          this.service.openSnackBar("Oh no.. something terribly wrong happened", ":O");
          return;
        }
        this.service.openSnackBar("Successfully logged in from DB!", "Close")
        this.loggedIn = true;
        this.service.isLoggedIn = true;
        this.service.ordersCount = response.body.placedOrders;
        this.service.ordersPendingCount = response.body.pendingOrders;
        this.localUser = {
          isLoggedIn : true,
          username : this.service.user.username,
          userType : this.service.isAdmin ? "Admin" : "Customer",
          userAvatar : this.service.isAdmin ? "../../../../assets/administrator.png" :
          "../../../../assets/customer.png",
          placedOrders : this.service.ordersCount,
          pendingOrders: this.service.ordersPendingCount,
        }
      }, responeError => {
          if (responeError.error.isError && !!responeError.error.errorMessage) {
            this.service.openSnackBar(responeError.error.errorMessage, "Close");
            return;
          } else {
            this.service.openSnackBar("Catastrophic Failure", "Ok?");
            return;
          }
      });
      }, responeError => {
        if (responeError.error.isError && !!responeError.error.errorMessage) {
          this.service.openSnackBar(responeError.error.errorMessage, "Close");
          return;
        } else {
          this.service.openSnackBar("Catastrophic Failure", "Ok?");
          return;
        }
    });
  }

  toggleRegister() {
    this.toggleText = this.isRegisterToggled ? "Toggle Register" : "Toggle Login";
    this.buttonText = this.isRegisterToggled ? "Login" : "Register"
    this.isRegisterToggled = !this.isRegisterToggled;
  }

  signOut() {
    this.localUser.isLoggedIn = false;
    this.loggedIn = false;
    this.localUser = {
      isLoggedIn : false,
      username : "Not connected",
      userType : "N/A",
      userAvatar : "https://img.favpng.com/25/13/19/samsung-galaxy-a8-a8-user-login-telephone-avatar-png-favpng-dqKEPfX7hPbc6SMVUCteANKwj.jpg",
      placedOrders: 0,
      pendingOrders: 0
    }
    this.service.user = new UserModel();
    this.service.isLoggedIn = false;
    this.service.isAdmin = false;
    if (!this.localUser.isLoggedIn) {
      this.service.openSnackBar("Successfully logged out!", "Close")
    }
    this.loginForm.reset();
    this.registerForm.reset();
  }
  
}
