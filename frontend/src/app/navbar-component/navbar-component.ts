import { Component, EventEmitter, Output} from '@angular/core';
import {provideRouter, Router, RouterLink, RouterOutlet} from "@angular/router";
import {DialogService} from 'primeng/dynamicdialog';
import {routes} from '../app.routes';

@Component({
  selector: '[app-navbar-component]',
  imports: [
    RouterOutlet,
    RouterLink
  ],
  providers:[DialogService],
  templateUrl: './navbar-component.html',
  styleUrl: './navbar-component.css',
})
export class NavbarComponent {
  @Output() startDrive = new EventEmitter;
  @Output() newCar = new EventEmitter;
  @Output() newTariff = new EventEmitter;




  role = sessionStorage.getItem('user_role');

  isAdmin(){
    return this.role == 'ROLE_ADMIN'
  }

  constructor(protected router: Router) {
  }

  protected onStartDrive() {
    this.startDrive.emit()
  }
  protected onNewCar() {
    this.newCar.emit()
  }
  protected onNewTariff() {
    this.newTariff.emit()
  }

  protected readonly routes = routes;
  protected readonly provideRouter = provideRouter;
}
