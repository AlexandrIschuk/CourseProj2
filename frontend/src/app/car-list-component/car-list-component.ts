import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {AsyncPipe} from '@angular/common';
import {NavbarComponent} from '../navbar-component/navbar-component';
import {map, Observable} from 'rxjs';
import {Car} from '../interfaces/car';
import {CarComponent} from '../car-component/car-component';
import {DialogService} from 'primeng/dynamicdialog';
import {CarService} from '../services/car.service';
import {NewCarComponent} from '../new-car-component/new-car-component';

@Component({
  selector: 'app-car-list-component',
  imports: [AsyncPipe, NavbarComponent, CarComponent],
  providers: [DialogService],
  templateUrl: './car-list-component.html',
  styleUrl: './car-list-component.css',
})
export class CarListComponent implements OnInit {
  protected cars$?: Observable<Car[]>;

  constructor(
    private carService: CarService,
    private dialogService: DialogService,
    private cd: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadCars();
  }

  protected deleteCar(id: number) {
    this.carService.deleteCar(id).subscribe({
      next: () => {
        if (this.cars$) {
          this.cars$ = this.cars$.pipe(map((cars) => cars.filter((c) => c.carId !== id)));
        }
        this.cd.detectChanges();
      },
    });
  }

  private loadCars() {
    this.cars$ = this.carService.getAllCars();
  }

  protected newCar() {
    const ref = this.dialogService.open(NewCarComponent, {
      width: '550px',
      data: {},
      styleClass: 'complete-trip-dialog',
      closable: true,
      dismissableMask: true,
    });
    ref?.onClose.subscribe({
      next: () => {
        this.loadCars();
        this.cd.detectChanges();
      },
    });
  }

  protected updateCar(car: Car) {
    this.carService.updateCar(car).subscribe({
      next: () => {
        this.loadCars();
        this.cd.detectChanges();
      },
    });
  }
}
