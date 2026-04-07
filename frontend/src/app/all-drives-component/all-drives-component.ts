import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {AsyncPipe, DatePipe} from "@angular/common";
import {DriveComponent} from '../drive-component/drive-component';
import {NavbarComponent} from '../navbar-component/navbar-component';
import {map, Observable} from 'rxjs';
import {Drive} from '../interfaces/drive';
import {DriveService} from '../services/drive.service';
import {UserService} from '../services/user.service';
import {DialogService} from 'primeng/dynamicdialog';
import {CompleteDrive} from '../complete-drive/complete-drive';
import {UserByDriveComponent} from '../user-by-drive-component/user-by-drive-component';

@Component({
  selector: 'app-all-drives-component',
  imports: [
    AsyncPipe,
    DriveComponent,
    NavbarComponent
  ],
  providers: [DialogService],
  templateUrl: './all-drives-component.html',
  styleUrl: './all-drives-component.css',
})
export class AllDrivesComponent implements OnInit{
  public drives$?: Observable<Drive[]>;

  protected errorMessage = '';

  constructor(private driveService: DriveService, private userService: UserService, private cd: ChangeDetectorRef, private dialogService: DialogService) { }

  ngOnInit(): void {
    this.drives$ = this.driveService.getAllDrives();
  }


  protected getUserInfo(userId: number) {
    const ref = this.dialogService.open(UserByDriveComponent, {
      width: '550px',
      data: {
        userId: userId
      },
      styleClass: 'complete-trip-dialog',
      closable: true,
      dismissableMask: true
    });
  }
}
