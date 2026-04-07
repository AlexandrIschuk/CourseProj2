import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private authRoleService: AuthService, private router: Router) {}

  canActivate(route: any): boolean {
    const requiredRole: string | null = route.data['role'];
    const hasAccess = this.authRoleService.hasRole(requiredRole);

    if (!hasAccess) {
      this.router.navigate(['/access-denied']);
      return false;
    }
    return true;
  }
}
