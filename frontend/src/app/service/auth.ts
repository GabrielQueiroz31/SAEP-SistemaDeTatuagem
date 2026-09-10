import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const authGuard: CanActivateFn = () => {
  return localStorage.getItem('usuarioLogado') !== null || inject(Router).createUrlTree(['/login']);
};

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const router = inject(Router);
  if (!request.url.startsWith('http://localhost:8081/api/')) {
    return next(request);
  }

  return next(request.clone({ withCredentials: true })).pipe(
    catchError((erro) => {
      if (erro.status === 401 && !request.url.endsWith('/auth/login')) {
        localStorage.removeItem('usuarioLogado');
        router.navigate(['/login']);
      }
      return throwError(() => erro);
    })
  );
};
