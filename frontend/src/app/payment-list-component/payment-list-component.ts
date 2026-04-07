import {Component, OnInit} from '@angular/core';
import {AsyncPipe} from "@angular/common";
import {NavbarComponent} from "../navbar-component/navbar-component";
import {Observable} from 'rxjs';
import {Payment} from '../interfaces/payment';
import {DialogService} from 'primeng/dynamicdialog';
import {PaymentService} from '../services/PaymentService';
import {PaymentComponent} from '../payment-component/payment-component';

@Component({
  selector: 'app-payment-list-component',
  imports: [
    AsyncPipe,
    NavbarComponent,
    PaymentComponent
  ],
  providers: [DialogService],
  templateUrl: './payment-list-component.html',
  styleUrl: './payment-list-component.css',
})
export class PaymentListComponent implements OnInit{
  protected payments$?: Observable<Payment[]>;

  constructor(private paymentService: PaymentService) { }

  ngOnInit(): void {
    this.loadPayments();
  }
  protected loadPayments(){
    this.payments$ = this.paymentService.getAllPayments();
  }
}
