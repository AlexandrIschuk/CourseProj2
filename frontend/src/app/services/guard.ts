import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor( private router: Router) {}

  canActivate(route: any): boolean {
    const requiredRole: string | null = route.data['role'];

    if (requiredRole != sessionStorage.getItem('user_role')) {
      this.router.navigate(['/access-denied']);
      return false;
    }
    return true;
  }
}
