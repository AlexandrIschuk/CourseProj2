import {ChangeDetectorRef, Component} from '@angular/core';
import {NavbarComponent} from "../navbar-component/navbar-component";
import {FormBuilder, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AuthService} from '../services/auth.service';
import {RouterLink} from '@angular/router';
import {User} from '../interfaces/user';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-user-component',
  imports: [
    NavbarComponent,
    FormsModule,
    ReactiveFormsModule,
    DatePipe,
    RouterLink
  ],
  templateUrl: './user-component.html',
  styleUrl: './user-component.css',
})
export class UserComponent {
  protected user?: User;
  constructor(private authService: AuthService, private cd: ChangeDetectorRef) {
    this.authService = authService;
    this.cd = cd;
    this.authService.profile().subscribe({
      next: (response) => {
        this.user = response;
        this.cd.detectChanges();
      }
    });
  }

  protected logout() {
    sessionStorage.clear();
  }
}
