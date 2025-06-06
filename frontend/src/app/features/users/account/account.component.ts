import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { User } from '../auth.interface';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
})
export class AccountComponent implements OnInit {
  user?: User;

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.getAccount().subscribe({
      next: (data) => {
        this.user = data;
      },
      error: (err) => console.error('Unauthorized or error:', err)
    });
  }
}
