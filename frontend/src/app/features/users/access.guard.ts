import {inject} from "@angular/core";
import {AuthService} from "./services/auth.service";
import {Router} from '@angular/router'

export const canActivateAuth = () => {
  const isSignedIn = inject(AuthService).isAuthenticated()
  const router = inject(Router)

  if (isSignedIn) {
    return true
  }

  return router.createUrlTree(['/signin']);
}
