import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Tariff} from '../interfaces/tariff';
import {FormsModule, ReactiveFormsModule } from "@angular/forms";

@Component({
  selector: '[app-tariff-component]',
  imports: [FormsModule, ReactiveFormsModule],
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

  protected onCancel(){
    this.isEdit = false;
  }
}
