import { Component } from '@angular/core';
import {NgxMaskDirective} from "ngx-mask";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {DynamicDialogRef} from 'primeng/dynamicdialog';
import {Tariff} from '../interfaces/tariff';
import {TariffService} from '../services/tariff.service';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-new-tariff-component',
  imports: [
    ReactiveFormsModule,
    AsyncPipe
  ],
  templateUrl: './new-tariff-component.html',
  styleUrl: './new-tariff-component.css',
})
export class NewTariffComponent {
  protected newTariffForm!: FormGroup;
  errorMessage = '';

  methods = [
    { value: 'MINUTE', label: 'По минутам' },
    { value: 'KILOMETER', label: 'По километражу' }
  ];


  constructor(private fb: FormBuilder, private tariffService: TariffService, public ref: DynamicDialogRef,) {
    this.newTariffForm = this.fb.group({
      name: ['', [
        Validators.required
      ]],
      price: ['', [
        Validators.required,Validators.pattern('^[0-9]*$')
      ]],
      tariffType: ['', [
        Validators.required
      ]]
    })
  }

  protected onSubmit() {
    if(this.newTariffForm.valid){
      const newTariff: Tariff = {
        ...this.newTariffForm.value,

      }
      this.tariffService.newTariff(newTariff).subscribe({
        next: () => {
          this.ref.close();
        }
      })
    }
  }

  get name() {return this.newTariffForm.get('name')}
  get price() {return this.newTariffForm.get('price')}
  get tariffType() {return this.newTariffForm.get('tariffType')}
}
