import {ChangeDetectorRef, Component} from '@angular/core';
import {DatePipe} from "@angular/common";
import {NavbarComponent} from "../navbar-component/navbar-component";
import {User} from '../interfaces/user';
import {AuthService} from '../services/auth.service';
import {UserService} from '../services/user.service';
import {DynamicDialogConfig} from 'primeng/dynamicdialog';

@Component({
  selector: 'app-user-by-drive-component',
    imports: [
        DatePipe,
    ],
  templateUrl: './user-by-drive-component.html',
  styleUrl: './user-by-drive-component.css',
})
export class UserByDriveComponent {
  protected user?: User;
  constructor(private userService: UserService,public config: DynamicDialogConfig, private cd: ChangeDetectorRef) {
    const userId = this.config.data.userId;
    this.userService.getUserById(userId).subscribe({
      next: (response) => {
        this.user = response;
        //this.cd.detectChanges();
      }
    });
  }
}
