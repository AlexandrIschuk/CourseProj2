import {ChangeDetectorRef, Component} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Observable} from 'rxjs';
import {Car} from '../interfaces/car';
import {Tariff} from '../interfaces/tariff';
import {CarService} from '../services/car.service';
import {ActivatedRoute} from '@angular/router';
import {TariffService} from '../services/tariff.service';
import {AsyncPipe} from '@angular/common';
import {Drive} from '../interfaces/drive';
import {DriveService} from '../services/drive.service';
import {DynamicDialogRef} from 'primeng/dynamicdialog';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-start-drive-component',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    AsyncPipe
  ],
  templateUrl: './start-drive-component.html',
  styleUrl: './start-drive-component.css',
})
export class StartDriveComponent {
  startDriveForm: FormGroup;
  cars$!: Observable<Car[]>;
  tariffs$!: Observable<Tariff[]>;
  drive!: Drive;
  errorMessage = '';

  constructor(private cd: ChangeDetectorRef,public ref: DynamicDialogRef,private driveService: DriveService,private tariffService: TariffService,private carService: CarService,private fb: FormBuilder) {
    this.startDriveForm = this.fb.group({
      car: ['',[
        Validators.required
      ]],
      tariff: ['',[
        Validators.required
      ]]
    });
    this.cars$ = carService.getFreeCar();
    this.tariffs$ = tariffService.getAllTariff();
  }

  get car() {return this.startDriveForm.get('car');}
  get tariff() {return this.startDriveForm.get('tariff');}

  protected onSubmit() {
    if(this.startDriveForm.valid){
      const newDrive: Drive = {
        ...this.drive,
        ...this.startDriveForm.value,
        driveStatus: 'ACTIVE'
      };
      this.driveService.startDrive(newDrive).subscribe({
        next:() => {
          this.ref.close();
        },
        error: (err:HttpErrorResponse) => {
          if(err.status == 400){
            this.errorMessage = "Нельзя начать больше 1 поездки!";
            this.cd.detectChanges();
          }
        }
      });
    }
  }
}
