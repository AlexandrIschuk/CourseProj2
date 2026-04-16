import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {User} from '../interfaces/user';
import { environment } from '../../environments/environment';
import {Observable} from 'rxjs';
import {AuthResponse} from '../interfaces/auth.response';
import {Role} from '../interfaces/role';

export type UserRole =  'ROLE_ADMIN' | 'ROLE_USER' ;

@Injectable({providedIn: 'root'})
export class AuthService{
  private currentRole!: UserRole;
  constructor(private _httpClient: HttpClient) {
    this.getRoleFromToken();
  }
  public login(user: User):Observable<AuthResponse>{
    return this._httpClient.post<AuthResponse>(`${environment.apiUrl}/auth/login`, user);
  }

  public refreshToken(refreshToken: String | null):Observable<AuthResponse>{
    return this._httpClient.post<AuthResponse>(`${environment.apiUrl}/auth/refresh`,refreshToken, {withCredentials: true});
  }

  public profile(): Observable<User>{
    return this._httpClient.get<User>(`${environment.apiUrl}/auth/me`);
  }

  getDecodedToken(): any | null {
    const token = sessionStorage.getItem("auth_token");
    if(token){
      const payload = token.split('.')[0];
      const decodedPayload = atob(payload); // base64 decode
      return JSON.parse(decodedPayload);
    }
  }

  public getRoleFromToken(): Role[] {
    const decoded = this.getDecodedToken();
    this.currentRole = decoded?.roles[0].authority;
    return decoded?.roles || null;
  }

  get role() {return this.currentRole}
}
