import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { environment } from '../../environments/environment';
import {Observable} from 'rxjs';
import {Car} from '../interfaces/car';

@Injectable({providedIn: 'root'})
export class CarService{
  constructor(private _httpClient: HttpClient) {
  }

  public getFreeCar(): Observable<Car[]>{
    return this._httpClient.get<Car[]>(`${environment.apiUrl}/cars/free`);
  }

  public getAllCars(): Observable<Car[]>{
    return this._httpClient.get<Car[]>(`${environment.apiUrl}/cars`);
  }

  public deleteCar(id: number){
    return this._httpClient.delete(`${environment.apiUrl}/cars/${id}`);
  }

  public addCar(car: Car){
    return this._httpClient.post(`${environment.apiUrl}/cars`, car);
  }

  public updateCar(car: Car){
    return this._httpClient.put(`${environment.apiUrl}/cars/${car.carId}`, car);
  }

}
