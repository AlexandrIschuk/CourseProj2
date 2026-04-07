import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Tariff} from '../interfaces/tariff';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({providedIn: 'root'})
export class TariffService{
  constructor(private _httpClient: HttpClient) {
  }

  public getAllTariff(): Observable<Tariff[]> {
    return this._httpClient.get<Tariff[]>(`${environment.apiUrl}/tariffs`);
  }

  public deleteTariff(id:number) {
    return this._httpClient.delete(`${environment.apiUrl}/tariffs/${id}`);
  }

  public newTariff(tariff:Tariff) {
    return this._httpClient.post(`${environment.apiUrl}/tariffs`, tariff);
  }
}
