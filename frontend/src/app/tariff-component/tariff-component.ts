import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Tariff} from '../interfaces/tariff';
import {FormsModule, ReactiveFormsModule } from "@angular/forms";
import { NgxMaskDirective } from 'ngx-mask';

@Component({
  selector: '[app-tariff-component]',
  imports: [FormsModule, ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './tariff-component.html',
  styleUrl: './tariff-component.css',
})
export class TariffComponent {
  @Input() tariff!: Tariff;
  @Output() delete = new EventEmitter<number>();
  @Output() update = new EventEmitter<Tariff>();
  isEdit: boolean = false;

  methods = [
    { value: 'MINUTE', label: 'По минутам' },
    { value: 'KILOMETER', label: 'По километражу' },
  ];

  protected onDelete() {
    this.delete.emit(this.tariff.tariffId);
  }

  onEdit() {
    this.isEdit = true;
  }

  protected onUpdate() {
    this.update.emit(this.tariff);
  }

  protected onCancel() {
    this.isEdit = false;
  }

  protected isRequired(item: any) {
    if (item == '') {
      return true;
    } else {
      return false;
    }
  }

  protected isValid(tariff: Tariff) {
    if(this.isRequired(tariff.name) || this.isRequired(tariff.price)){
      return true;
    } else{
      return false;
    }

  }
}
