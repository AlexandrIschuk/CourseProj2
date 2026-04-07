import {ChangeDetectorRef, Component} from '@angular/core';
import {Button} from 'primeng/button';
import {Password} from 'primeng/password';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Card} from 'primeng/card';
import {Router, RouterLink} from '@angular/router';
import {User} from '../interfaces/user';
import {HttpErrorResponse} from '@angular/common/http';
import {AuthService} from '../services/auth.service';
@Component({
  selector: 'app-login-component',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
})
export class LoginComponent {
  protected loginForm: FormGroup;
  errorMessage = '';

  constructor(private fb: FormBuilder,private cd: ChangeDetectorRef,private authService: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['',[
        Validators.required
      ]],
      password: ['',[
        Validators.required
      ]],
    });
  }

  protected onSubmit() {
    if (this.loginForm.valid) {
      this.errorMessage = '';
      const user : User = this.loginForm.value;
      this.authService.login(user).subscribe({
          next: (response) => {
            sessionStorage.setItem('auth_token', response.token);
            sessionStorage.setItem('refresh_token', response.refreshToken);
            const role = this.authService.getRoleFromToken()[0].authority;
            sessionStorage.setItem("user_role", role);
            if (role == 'ROLE_USER')
            this.router.navigate(['/drives']);
            else this.router.navigate(['/drives/all']);
          },
          error: (error: HttpErrorResponse) => {
            if(error.status == 403){
              this.errorMessage = "Неправильный логин или пароль!"
              this.cd.detectChanges();
            }
            this.loginForm.patchValue({ password: '' });
          }
        }
      );
    }
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }
}
