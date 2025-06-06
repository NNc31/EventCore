import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import {HttpClientModule, withInterceptors, provideHttpClient} from '@angular/common/http';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { SignupComponent } from './features/users/signup/signup.component';
import { SigninComponent } from './features/users/signin/signin.component';
import { AccountComponent } from './features/users/account/account.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { authInterceptor } from './features/users/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    SigninComponent,
    AccountComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatSnackBarModule
  ],
  providers: [
    provideHttpClient(withInterceptors([authInterceptor]))
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
