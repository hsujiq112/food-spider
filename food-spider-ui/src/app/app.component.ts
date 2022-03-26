import { Component, HostBinding } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { fadeInAnimation, slideInOutAnimation } from './animations';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    fadeInAnimation,
    slideInOutAnimation
  ]
})
export class AppComponent{
  
  title = 'foodSpider-app-material';

  prepareRoute(outlet: RouterOutlet) {
    return outlet?.activatedRouteData?.['animation'];
  }
}
