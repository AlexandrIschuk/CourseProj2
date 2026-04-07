import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { environment } from '../../environments/environment';
import {Observable} from 'rxjs';
import {Drive} from '../interfaces/drive';

@Injectable({providedIn: 'root'})
export class DriveService{
  constructor(private _httpClient: HttpClient ) {
  }
  public getActiveDrives(): Observable<Drive[]>{
    return this._httpClient.get<Drive[]>(`${environment.apiUrl}/drives/active`);
  }

  public getDrives(): Observable<Drive[]>{
    return this._httpClient.get<Drive[]>(`${environment.apiUrl}/drives`);
  }
  public getAllDrives(): Observable<Drive[]>{
    return this._httpClient.get<Drive[]>(`${environment.apiUrl}/drives/all`);
  }

  public cancelDrive(id: number){
    return this._httpClient.put(`${environment.apiUrl}/drives/cancel/${id}`, null);
  }

  public completeDrive(id: number,drive: Drive){
    return this._httpClient.put(`${environment.apiUrl}/drives/${id}`, drive);
  }

  public deleteDrive(id: number){
    return this._httpClient.delete(`${environment.apiUrl}/drives/${id}`);
  }

  public startDrive(drive: Drive) {
    return this._httpClient.post(`${environment.apiUrl}/drives`, drive);
  }
}
