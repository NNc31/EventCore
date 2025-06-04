import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SignupRequest {
  username: string;
  email: string;
  password: string;
}

export interface SigninRequest {
  username: string;
  password: string;
}

export interface JwtResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = '/api/users';

  constructor(private http: HttpClient) {}

  signup(request: SignupRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/signup`, request);
  }

  signin(request: SigninRequest): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.apiUrl}/signin`, request);
  }
}
