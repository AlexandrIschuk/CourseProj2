import {Component, Input} from '@angular/core';
import {DatePipe} from "@angular/common";
import {Payment} from '../interfaces/payment';

@Component({
  selector: '[app-payment-component]',
    imports: [
        DatePipe
    ],
  templateUrl: './payment-component.html',
  styleUrl: './payment-component.css',
})
export class PaymentComponent {
  @Input() payment!: Payment;
}
