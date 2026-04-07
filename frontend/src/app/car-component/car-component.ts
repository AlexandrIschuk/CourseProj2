import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Car} from '../interfaces/car';

@Component({
  selector: '[app-car-component]',
  templateUrl: './car-component.html',
  styleUrl: './car-component.css',
})
export class CarComponent {
  @Input() car!: Car;
  @Output() delete = new EventEmitter<number>

  protected onDelete() {
    this.delete.emit(this.car.carId);
  }
}
