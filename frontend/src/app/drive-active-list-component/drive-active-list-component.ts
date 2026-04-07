import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {map, Observable} from 'rxjs';
import {Drive} from '../interfaces/drive';
import {DriveService} from '../services/drive.service';
import {AsyncPipe} from '@angular/common';
import {DriveComponent} from '../drive-component/drive-component';
import {NavbarComponent} from '../navbar-component/navbar-component';
import {HttpErrorResponse} from '@angular/common/http';
import {CarService} from '../services/car.service';
import {DialogService} from 'primeng/dynamicdialog';
import {CompleteDrive} from '../complete-drive/complete-drive';
import {StartDriveComponent} from '../start-drive-component/start-drive-component';
import {NewPaymentComponent} from '../new-payment-component/new-payment-component';

@Component({
  selector: 'app-drive-active-list-component',
  imports: [
    AsyncPipe,
    DriveComponent,
    NavbarComponent
  ],
  providers: [DialogService],
  templateUrl: './drive-active-list-component.html',
  styleUrl: './drive-active-list-component.css',
})
export class DriveActiveListComponent implements OnInit{
  protected drives$?: Observable<Drive[]>;

  protected errorMessage = '';

  constructor(private driveService: DriveService, private dialogService: DialogService, private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadDrives();
  }

  protected loadDrives(){
    this.drives$ = this.driveService.getActiveDrives();
  }
  protected cancelDrive(id: number) {
    this.driveService.cancelDrive(id).subscribe({
      next: () => {
        if (this.drives$) {
          this.drives$ = this.drives$.pipe(
            map(drives => drives.filter(d => d.driveId !== id))
          );
        }
        this.cd.detectChanges();
      },
      error: (err: HttpErrorResponse) => {
        if(err.status == 400){
          this.errorMessage = "Вы не можете отменить поездку так-как она начата более 5 минут назад!"
          this.cd.detectChanges();
        }
      }
    });
  }

  protected completeDrive(drive: Drive) {
    const ref = this.dialogService.open(CompleteDrive, {
      width: '550px',
      data: {
        drive: drive
      },
      styleClass: 'complete-trip-dialog',
      closable: true,
      dismissableMask: true
    });
    ref?.onClose.subscribe({
      next: (result) => {
        if(result == 'submitted'){
          if (this.drives$) {
            this.drives$ = this.drives$.pipe(
              map(drives => drives.map(currDrive =>
                currDrive.driveId === drive.driveId
                  ? { ...currDrive, driveStatus: 'PENDING_PAYMENT'}
                  : currDrive
              ))
            );
          }
        }
      }
    })
  }

  protected startDrive() {
    const ref = this.dialogService.open(StartDriveComponent, {
      width: '550px',
      data: {

      },
      styleClass: 'complete-trip-dialog',
      closable: true,
      dismissableMask: true
    });
    ref?.onClose.subscribe({
      next: () => {
        this.loadDrives();
        this.cd.detectChanges();
      }
    })
  }

  protected paymentDrive(drive: Drive) {
    console.log(drive);
    const ref = this.dialogService.open(NewPaymentComponent, {
      width: '550px',
      data: {
        drive: drive
      },
      styleClass: 'complete-trip-dialog',
      closable: true,
      dismissableMask: true
    });
    ref?.onClose.subscribe({
      next: () => {
        this.loadDrives();
        this.cd.detectChanges();
      }
    })
  }
}
