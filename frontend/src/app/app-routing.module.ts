import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {canActivateAuth} from "./features/users/access.guard";
import {SignupComponent} from './features/users/signup/signup.component';
import {SigninComponent} from "./features/users/signin/signin.component";
import {AccountComponent} from "./features/users/account/account.component";

const routes: Routes = [
  { path: 'signup', component: SignupComponent },
  { path: 'signin', component: SigninComponent },
  { path: 'account', component: AccountComponent, canActivate: [canActivateAuth] },
  { path: '', redirectTo: '/signup', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
