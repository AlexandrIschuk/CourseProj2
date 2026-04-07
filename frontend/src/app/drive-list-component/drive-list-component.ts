import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NavbarComponent} from '../navbar-component/navbar-component';
import {AsyncPipe} from '@angular/common';
import {DriveComponent} from '../drive-component/drive-component';
import { map, Observable} from 'rxjs';
import {Drive} from '../interfaces/drive';
import {DriveService} from '../services/drive.service';

@Component({
  selector: 'app-drive-list-component',
  imports: [
    NavbarComponent,
    AsyncPipe,
    DriveComponent
  ],
  templateUrl: './drive-list-component.html',
  styleUrl: './drive-list-component.css',
})
export class DriveListComponent implements OnInit{
  public drives$?: Observable<Drive[]>;

  protected errorMessage = '';

  constructor(private driveService: DriveService, private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.drives$ = this.driveService.getDrives();
  }

  protected deleteDrive(id: number) {
    this.driveService.deleteDrive(id).subscribe({
      next: () => {
        if (this.drives$) {
          this.drives$ = this.drives$.pipe(
            map(drives => drives.filter(d => d.driveId !== id))
          );
        }
        this.cd.detectChanges();
      }
    })
  }
}
