import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {PaymentService} from '../services/PaymentService';
import {Payment} from '../interfaces/payment';

@Component({
  selector: 'app-new-payment-component',
    imports: [
        ReactiveFormsModule
    ],
  templateUrl: './new-payment-component.html',
  styleUrl: './new-payment-component.css',
})
export class NewPaymentComponent {
  protected newPaymentForm: FormGroup;

  methods = [
    { value: 'CARD', label: 'Картой' },
    { value: 'CASH', label: 'Наличными' }
  ];

  constructor(private fb: FormBuilder, private paymentService: PaymentService,public config: DynamicDialogConfig, public ref: DynamicDialogRef) {
    this.newPaymentForm = this.fb.group({
      paymentMethod: ['', [
        Validators.required
      ]]
    })
  }
  protected onSubmit() {
    const drive = this.config.data.drive;
    if(this.newPaymentForm.valid){
      const newPayment: Payment = {
            drive: drive,
        ...this.newPaymentForm.value
      }
      this.paymentService.newPayment(newPayment).subscribe({
        next: () => this.ref.close()
      });
    }
  }
  get paymentMethod() {return this.newPaymentForm.get('paymentMethod')}
}
