import {ChangeDetectorRef, Component} from '@angular/core';
import {NavbarComponent} from "../navbar-component/navbar-component";
import {FormBuilder, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AuthService} from '../services/auth.service';
import {RouterLink} from '@angular/router';
import {User} from '../interfaces/user';
import {DatePipe} from '@angular/common';
import { NgxMaskDirective } from 'ngx-mask';
import { UserService } from '../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-user-component',
  imports: [
    NavbarComponent,
    FormsModule,
    ReactiveFormsModule,
    DatePipe,
    RouterLink,
    NgxMaskDirective,
  ],
  templateUrl: './user-component.html',
  styleUrl: './user-component.css',
})
export class UserComponent {
  protected user?: User;
  isEdit = false;
  protected errorMessage = '';
  constructor(
    private authService: AuthService,
    private cd: ChangeDetectorRef,
    private userService: UserService
  ) {
    this.authService = authService;
    this.cd = cd;
    this.loadUserInfo();
  }
  loadUserInfo(){
    this.authService.profile().subscribe({
      next: (response) => {
        this.user = response;
        this.cd.detectChanges();
      },
    });
  }

  protected logout() {
    sessionStorage.clear();
  }

  protected onEdit() {
    this.isEdit = true;
  }

  protected onCancel() {
    this.isEdit = false;
  }

  protected onUpdate(user: User) {
    this.userService.updateUser(user).subscribe({
      next: () => {
        this.loadUserInfo();
        this.isEdit = false;
        this.cd.detectChanges();
      },
      error: (err: HttpErrorResponse) => {
        if(err.status == 409){
          this.errorMessage = "Пользователь уже существует!";
          this.cd.detectChanges();
        }
      }
    })
  }

  protected isRequired(item: any) {
    if(item == ''){
      return true;
    }else {
      return false;
    }
  }

  protected isValid(user: User) {
    if(this.isRequired(user.email) || this.isRequired(user.firstname) || this.isRequired(user.lastname) || this.isRequired(user.phoneNumber) || this.isRequired(user.drvLicenseNumber)){
      return true;
    } else {
      return false;
    }
  }
}
