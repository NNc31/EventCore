import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import {JwtResponse, SigninRequest, SignupRequest, User} from '../auth.interface';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = '/api/users';
  private accessToken$ = new BehaviorSubject<string | null>(null);

  constructor(private http: HttpClient) {
    const savedToken = localStorage.getItem('accessToken');
    this.accessToken$.next(savedToken);
  }
  get token(): string | null {
    return this.accessToken$.value;
  }

  private set token(token: string | null) {
    this.accessToken$.next(token);
    if (token) {
      localStorage.setItem('accessToken', token);
    } else {
      localStorage.removeItem('accessToken');
    }
  }

  signup(request: SignupRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/signup`, request);
  }

  signin(request: SigninRequest): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.apiUrl}/signin`, request, {
      withCredentials: true
    }).pipe(
      tap(response => {
        this.token = response.token;
      })
    );
  }

  getAccount(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/account`);
  }

  refreshToken(): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.apiUrl}/refresh`, {}, {
      withCredentials: true
    }).pipe(
      tap(response => this.token = response.token)
    );
  }

  logout(): void {
    this.token = null;
  }

  isAuthenticated(): boolean {
    return !!this.token;
  }
}
