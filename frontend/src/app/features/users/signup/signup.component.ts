import { Component } from '@angular/core';
import { AuthService, SignupRequest } from '../services/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html'
})
export class SignupComponent {

  form: SignupRequest = {
    username: '',
    email: '',
    password: ''
  };

  constructor(private authService: AuthService) {}

  onSubmit() {
    this.authService.signup(this.form).subscribe({
      next: () => alert('Sign up successful'),
      error: err => alert('Sign up failed: ' + err.error?.message ?? err.statusText)
    });
  }
}
