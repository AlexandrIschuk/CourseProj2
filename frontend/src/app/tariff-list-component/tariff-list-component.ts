import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {AsyncPipe} from '@angular/common';
import {CarComponent} from '../car-component/car-component';
import {NavbarComponent} from '../navbar-component/navbar-component';
import {map, Observable} from 'rxjs';
import {Tariff} from '../interfaces/tariff';
import {TariffService} from '../services/tariff.service';
import {TariffComponent} from '../tariff-component/tariff-component';
import {DialogService} from 'primeng/dynamicdialog';
import {NewTariffComponent} from '../new-tariff-component/new-tariff-component';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-tariff-list-component',
  imports: [
    AsyncPipe,
    NavbarComponent,
    TariffComponent
  ],
  providers: [DialogService],
  templateUrl: './tariff-list-component.html',
  styleUrl: './tariff-list-component.css',
})
export class TariffListComponent implements OnInit{
  protected tariffs$?: Observable<Tariff[]>;
  protected errorMessage = '';

  constructor(private dialogService: DialogService, private tariffService: TariffService, private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadTariffs();
  }

  protected deleteTariff(id: number) {
    this.tariffService.deleteTariff(id).subscribe({
      next: () => {
        if (this.tariffs$) {
          this.tariffs$ = this.tariffs$.pipe(
            map(tariffs => tariffs.filter(t => t.tariffId !== id))
          );
        }
        this.cd.detectChanges();
      }
    })
  }

  private loadTariffs() {
    this.tariffs$ = this.tariffService.getAllTariff();
  }

  protected newTariff() {
    const ref = this.dialogService.open(NewTariffComponent, {
      width: '550px',
      data: {

      },
      styleClass: 'complete-trip-dialog',
      closable: true,
      dismissableMask: true
    });
    ref?.onClose.subscribe({
      next: () => {
        this.loadTariffs();
        this.cd.detectChanges();
      }
    })
  }

  protected updateTariff(tariff: Tariff) {
    this.tariffService.updateTariff(tariff).subscribe({
      next: () => {
        this.loadTariffs();
        this.cd.detectChanges();
      },
      error: (err: HttpErrorResponse) => {
        if(err.status == 409){
          this.errorMessage = "Тариф с таким именем уже существует!"
          this.cd.detectChanges();
        }
      }
    })
  }
}
