import {ChangeDetectorRef, Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";

import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Drive} from '../interfaces/drive';
import {DriveService} from '../services/drive.service';


@Component({
  selector: 'app-complete-drive',
    imports: [
        ReactiveFormsModule
    ],
  templateUrl: './complete-drive.html',
  styleUrl: './complete-drive.css',
})
export class CompleteDrive {
  drive!: Drive;
  protected completeForm: FormGroup;
  errorMessage = '';
  successMessage = '';


  constructor(public ref: DynamicDialogRef, private cd: ChangeDetectorRef,private driveService: DriveService, public config: DynamicDialogConfig, private fb: FormBuilder) {
    this.drive  = this.config.data.drive;
    if(this.drive.tariff.tariffType == 'MINUTE'){
      const drive: Drive = {
        ...this.drive,
        driveStatus: 'PENDING_PAYMENT'
      };
      this.driveService.completeDrive(this.drive.driveId, drive).subscribe({
        next: () => {
          this.successMessage = 'Завершено успешно. Возврат на предыдущую страницу...';
          this.cd.detectChanges();
          setTimeout(() => {
            this.submitDialog();
            this.ref.close();
          }, 1000);

        }
      })
    }
    this.completeForm = this.fb.group({
      totalDistance: ['', [
        Validators.required
      ]]
    });
  }

  submitDialog() {
    this.ref.close('submitted'); // Передаем признак подтверждения
  }

  get totalDistance() {
    return this.completeForm.get('totalDistance');
  }

  protected onSubmit() {
    if (this.completeForm.valid) {
      const drive: Drive = {
        ...this.drive,
        ...this.completeForm.value,
        driveStatus: 'PENDING_PAYMENT'
      };
      this.driveService.completeDrive(this.drive.driveId, drive).subscribe({
        next: () => {
          this.submitDialog();
          this.ref.close();
        }
      })
    }
  }
}
