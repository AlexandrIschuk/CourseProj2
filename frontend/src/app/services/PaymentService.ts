import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Payment} from '../interfaces/payment';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({providedIn: 'root'})
export class PaymentService {
  constructor(private _htttClient: HttpClient) {
  }
  public getAllPayments(): Observable<Payment[]>{
    return this._htttClient.get<Payment[]>(`${environment.apiUrl}/payments`);
  }
  public newPayment(payment:Payment){
    return this._htttClient.post(`${environment.apiUrl}/payments`, payment);
  }
}
