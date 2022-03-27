import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// services and modules
import { MaterialModule } from './material-module';
import { SharedService } from './shared.service';
import { HttpClientModule } from '@angular/common/http';
import { LandingPageComponent } from './ui/pages/landing-page/landing-page.component';
import { LoginPageComponent } from './ui/pages/login-page/login-page.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule, MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDialogModule } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu'
import { MatCheckboxModule } from '@angular/material/checkbox'
import { MyRestaurantComponent } from './ui/pages/my-restaurant-page/my-restaurant-page.component';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { NewRestaurantDialogComponent } from './ui/pages/new-restaurant-dialog/new-restaurant-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    LoginPageComponent,
    MyRestaurantComponent,
    NewRestaurantDialogComponent
  ],
  imports: [
    MaterialModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    FlexLayoutModule,
    MatBadgeModule,
    MatDialogModule,
    MatMenuModule,
    MatCheckboxModule,
    BrowserAnimationsModule
  ],
  providers: [
    SharedService,
    {provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: {appearance: 'fill'}}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

platformBrowserDynamic().bootstrapModule(AppModule);
