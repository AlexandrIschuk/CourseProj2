import {User} from '../interfaces/user';
import {HttpClient} from '@angular/common/http';
import { environment } from '../../environments/environment';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class UserService{
  constructor(private _httpClient: HttpClient) {
  }
  public registration(user: User){
    return this._httpClient.post(`${environment.apiUrl}/users/register`, user);
  }

  public getUserById(userId: number): Observable<User>{
    return this._httpClient.get<User>(`${environment.apiUrl}/users/${userId}`);
  }

}
