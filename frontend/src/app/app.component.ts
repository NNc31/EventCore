import { Component } from '@angular/core';
import {AuthService} from "./features/users/services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private auth: AuthService) {}

  ngOnInit() {
    if (!this.auth.token) {
      this.auth.refreshToken().subscribe({
        next: () => {},
        error: () => this.auth.logout()
      });
    }
  }
}
