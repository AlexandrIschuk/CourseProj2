import { Routes } from '@angular/router';
import {LoginComponent} from './login-component/login-component';
import {RegistrationComponent} from './registration-component/registration-component';
import {DriveActiveListComponent} from './drive-active-list-component/drive-active-list-component';
import {DriveListComponent} from './drive-list-component/drive-list-component';
import {UserComponent} from './user-component/user-component';
import {CarListComponent} from './car-list-component/car-list-component';
import {RoleGuard} from './services/guard';
import {TariffListComponent} from './tariff-list-component/tariff-list-component';
import {AccessDeniedComponent} from './access-denied-component/access-denied-component';
import {PaymentListComponent} from './payment-list-component/payment-list-component';
import {AllDrivesComponent} from './all-drives-component/all-drives-component';

export const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'drives', component: DriveActiveListComponent},
  {path: 'drives/history', component: DriveListComponent},
  {path: 'me', component: UserComponent},
  {path: 'payment-history', component: PaymentListComponent},
  {path: 'access-denied', component: AccessDeniedComponent},
  {path: 'cars', component: CarListComponent, canActivate: [RoleGuard], data: {role: 'ROLE_ADMIN'}},
  {path: 'tariffs', component: TariffListComponent, canActivate: [RoleGuard], data: {role: 'ROLE_ADMIN'}},
  {path: 'drives/all', component: AllDrivesComponent, canActivate: [RoleGuard], data: {role: 'ROLE_ADMIN'}}

];
