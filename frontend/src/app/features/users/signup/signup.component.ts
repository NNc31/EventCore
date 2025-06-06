import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

import {AuthService} from '../services/auth.service';
import {SignupRequest} from "../auth.interface";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {

  isLoading = false;
  errorMessage = '';

  form = new FormGroup ({
    username: new FormControl('', Validators.required),
    email: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  })

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (this.form.invalid) return;
    const data: SignupRequest = this.form.value as SignupRequest;

    this.authService.signup(data).subscribe({
      next: () => {
        this.router.navigate(['/signin'], {
          state: { message: 'Sign up successful' }
        });
      },
      error: err => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Unknown error';
      }
    });
  }
}
