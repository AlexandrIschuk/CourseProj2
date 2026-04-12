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
  @ViewChild('brand') brandModel!: NgModel;
  @ViewChild('model') modelModel!: NgModel;
  @ViewChild('regNumber') regNumberModel!: NgModel;
  @ViewChild('year') yearModel!: NgModel;

  isBrandInvalid = false;
  isModelInvalid = false;
  isRegNumberInvalid = false;
  isYearInvalid = false;

  checkBrand() {
    this.isBrandInvalid = this.brandModel?.invalid || false;
  }
  checkModel() {
    this.isModelInvalid = this.modelModel?.invalid || false;
  }
  checkRegNumber() {
    this.isRegNumberInvalid = this.regNumberModel?.invalid || false;
  }
  checkYear() {
    this.isYearInvalid = this.yearModel?.invalid || false;
  }

  constructor(private carService: CarService) {}

  protected onDelete() {
    this.delete.emit(this.car.carId);
  }

  protected onEdit() {
    this.isEdit = true;
  }

  protected onCancel() {
    this.isEdit = false;
  }

  protected onSubmit() {}

  protected onUpdate() {
    this.update.emit(this.car);
  }

  protected readonly disabled = disabled;
}
