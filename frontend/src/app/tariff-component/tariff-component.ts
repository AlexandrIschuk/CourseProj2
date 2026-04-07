import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Tariff} from '../interfaces/tariff';

@Component({
  selector: '[app-tariff-component]',
  imports: [],
  templateUrl: './tariff-component.html',
  styleUrl: './tariff-component.css',
})
export class TariffComponent {
  @Input() tariff!: Tariff;
  @Output() delete = new EventEmitter<number>

  protected onDelete() {
    this.delete.emit(this.tariff.tariffId);
  }
}
