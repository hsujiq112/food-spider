import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './ui/pages/landing-page/landing-page.component';
import { MyRestaurantComponent } from './ui/pages/my-restaurant-page/my-restaurant-page.component';

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: '/landing-page'},
  {path:'landing-page', component:LandingPageComponent},
  {path:'my-restaurant-page', component:MyRestaurantComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
