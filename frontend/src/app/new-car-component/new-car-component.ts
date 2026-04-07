import { Component } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Car} from '../interfaces/car';
import {CarService} from '../services/car.service';
import {DynamicDialogRef} from 'primeng/dynamicdialog';
import {NgxMaskDirective} from 'ngx-mask';

@Component({
  selector: 'app-new-car-component',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgxMaskDirective
  ],
  templateUrl: './new-car-component.html',
  styleUrl: './new-car-component.css',
})
export class NewCarComponent {
  protected newCarForm!: FormGroup;
  errorMessage = '';
  public customPatterns = {
    'X': { pattern: new RegExp('[АВЕКМНОРСТУХ]') },
    '0': { pattern: new RegExp('[d{0-9}]') },

  };

  constructor(private fb: FormBuilder, private carService: CarService, public ref: DynamicDialogRef,) {
    this.newCarForm = this.fb.group({
      carBrand: ['', [
        Validators.required
      ]],
      carModel: ['', [
        Validators.required
      ]],
      carRegistrationNumber: ['', [
        Validators.required
      ]],
      year: ['', [
        Validators.required
      ]],
    })
  }

  protected onSubmit() {
    if(this.newCarForm.valid){
      const newCar: Car = {
        ...this.newCarForm.value,
        carStatus: "FREE"
      }
      this.carService.addCar(newCar).subscribe({
        next: () => {
          this.ref.close();
        }
      })
    }
  }

  get carBrand() {return this.newCarForm.get('carBrand');}
  get carModel() {return this.newCarForm.get('carModel');}
  get carRegistrationNumber() {return this.newCarForm.get('carRegistrationNumber');}
  get year() {return this.newCarForm.get('year');}

}
