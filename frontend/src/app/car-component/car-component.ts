import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import {Car} from '../interfaces/car';
import {
  FormBuilder,
  FormGroup,
  FormsModule, NgModel,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NgxMaskDirective } from 'ngx-mask';
import { CarService } from '../services/car.service';
import { disabled } from '@angular/forms/signals';

@Component({
  selector: '[app-car-component]',
  templateUrl: './car-component.html',
  styleUrl: './car-component.css',
  imports: [FormsModule, ReactiveFormsModule, NgxMaskDirective],
})
export class CarComponent {
  @Input() car!: Car;
  @Output() delete = new EventEmitter<number>();
  @Output() update = new EventEmitter<Car>();
  isEdit: boolean = false;
  originalCar!: Car;

  public customPatterns = {
    X: { pattern: new RegExp('[АВЕКМНОРСТУХ]') },
    '0': { pattern: new RegExp('[d{0-9}]') },
  };

  protected onDelete() {
    this.delete.emit(this.car.carId);
  }

  protected onEdit() {
    this.originalCar = {...this.car};
    this.isEdit = true;
  }

  protected onCancel() {
    this.isEdit = false;
    this.car = {...this.originalCar};

  }

  protected onUpdate() {
    this.update.emit(this.car);
  }

  isRequired(item: any) {
    if (item == '') {
      return true;
    } else {
      return false;
    }
  }

  isRegNumber(item: any) {
    if (item.length == 8 || item.length == 9) {
      return false;
    } else {
      return true;
    }
  }

  isYear(item: any) {
    if (item.toString().length != 4) {
      if(item < 2016){
        return "Машина не должна быть старше 10 лет!"
      }else{
        return "Введите корректный год!";
      }
    } else {
      return "";
    }
  }

  isValid(car: Car){
    if(this.isRequired(car.carBrand) || this.isRequired(car.carModel) || this.isRegNumber(car.carRegistrationNumber) || car.year < 2016 || car.year.toString().length != 4){
      return true;
    }else{
      return false;
    }
  }

}
