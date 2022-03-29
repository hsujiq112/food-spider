import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DisplayEmitter } from './api-model';
import { RestaurantsComponent } from './ui/pages/all-restaurants-page/all-restaurants-page.component';
import { LandingPageComponent } from './ui/pages/landing-page/landing-page.component';
import { OrdersPageComponent } from './ui/pages/orders-page/orders-page.component';
import { MyRestaurantComponent } from './ui/pages/restaurant-page/restaurant-page.component';

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: '/landing-page'},
  {path:'landing-page', component: LandingPageComponent},
  {path:'my-restaurant-page', component: MyRestaurantComponent, data: new DisplayEmitter()},
  {path:'all-restaurants-page', component: RestaurantsComponent},
  {path:'orders-page', component: OrdersPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
