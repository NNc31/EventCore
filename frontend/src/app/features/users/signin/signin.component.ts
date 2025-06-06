import {Component, inject} from '@angular/core';
import {AuthService} from '../services/auth.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {SigninRequest} from "../auth.interface";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent {

  errorMessage = '';
  snackBar = inject(MatSnackBar)

  form = new FormGroup ({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  })

  constructor(private authService: AuthService, private router: Router) {
    const message = this.router.getCurrentNavigation()?.extras.state?.['message'];
    if (message) {
      this.snackBar.open(message, 'OK', { duration: 3000, panelClass: 'custom-snackbar' });
    }
  }

  onSubmit() {
    if (this.form.invalid) return;
    const data: SigninRequest = this.form.value as SigninRequest;

    this.authService.signin(data).subscribe({
      next: () => this.router.navigate(['/account']),
      error: err => {
        console.error('Sign-in error', err);
        this.errorMessage = err.error?.message || 'Unknown error'
      }

    });
  }
}
