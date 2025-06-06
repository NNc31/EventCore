import {inject} from '@angular/core';
import {HttpInterceptorFn, HttpErrorResponse} from '@angular/common/http';
import {catchError, switchMap, throwError} from 'rxjs';
import {AuthService} from './services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);

  const accessToken = auth.token;
  let authReq = req;

  if (!req.url.includes('/signin') && !req.url.includes('/refresh') && accessToken) {
    authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${accessToken}` }
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        return auth.refreshToken().pipe(
          switchMap(res => {
            const newReq = req.clone({
              setHeaders: { Authorization: `Bearer ${res.token}` }
            });
            return next(newReq);
          }),
          catchError(refreshError => {
            auth.logout();
            return throwError(() => refreshError);
          })
        );
      }
      return throwError(() => error);
    })
  );
};
