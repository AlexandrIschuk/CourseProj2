import {ChangeDetectorRef, Component} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import { Location } from '@angular/common';
import {User} from '../interfaces/user';
import {UserService} from '../services/user.service';
import {HttpErrorResponse} from '@angular/common/http'
import {NgxMaskDirective} from 'ngx-mask';



@Component({
  selector: 'app-registration-component',
  imports: [
    ReactiveFormsModule,
    NgxMaskDirective,
    FormsModule
  ],
  templateUrl: './registration-component.html',
  styleUrl: './registration-component.css',
})
export class RegistrationComponent {
  protected registerForm: FormGroup;
  errorMessage = '';
  successMessage = '';


  constructor(private fb: FormBuilder, private location: Location, private cd: ChangeDetectorRef, private userService: UserService) {
    this.registerForm = this.fb.group({
      email: ['',[
        Validators.required
      ]],
      password: ['',[
        Validators.required
      ]],
      phoneNumber: ['',[
        Validators.required
      ]],
      firstname: ['',[
        Validators.required
      ]],
      lastname: ['',[
        Validators.required
      ]],
      drvLicenseNumber: ['',[
        Validators.required
      ]],
      confirmPassword: ['',[
        Validators.required
      ]]
    });
  }
  protected onCancel() {
    this.location.back();
  }

  protected onSubmit() {
    if (this.registerForm.valid) {
      if (this.password?.value == this.confirmPassword?.value) {
        this.errorMessage = '';
        const user: User = this.registerForm.value;
        console.log(user)
        this.userService.registration(user).subscribe({
            next: () => {
              this.successMessage = 'Регистрация прошла успешно! Перенаправление на страницу входа...';
              this.cd.detectChanges();
              setTimeout(() => {
                this.onCancel();
              }, 2000);
            },
            error: (error: HttpErrorResponse) => {
              if (error.status == 409) {
                this.errorMessage = "Пользователь уже существует!"
                this.cd.detectChanges();
              }
              this.registerForm.patchValue({email: ''});
              this.registerForm.patchValue({password: ''});
            }
          }
        );
      } else {
        this.errorMessage = "Пароли должны совпадать"
        this.cd.detectChanges();
      }
    }
  }

  get email() { return this.registerForm.get('email'); }
  get password() { return this.registerForm.get('password'); }
  get phoneNumber() { return this.registerForm.get('phoneNumber'); }
  get firstname() { return this.registerForm.get('firstname'); }
  get lastname() { return this.registerForm.get('lastname'); }
  get drvLicenseNumber() { return this.registerForm.get('drvLicenseNumber'); }
  get confirmPassword() { return this.registerForm.get('confirmPassword'); }
}
