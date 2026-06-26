import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { AuthService } from '../services/auth.service';

/**
 * Mensaje genérico que se muestra cuando no se pudo establecer conexión
 * con el backend (servidor caído, sin red, o bloqueado por CORS) o cuando
 * un gateway/proxy intermedio reporta que el servicio no está disponible.
 */
export const MENSAJE_SERVICIO_NO_DISPONIBLE =
  'En estos momentos el servicio no se encuentra disponible. Por favor, intenta nuevamente en unos minutos.';

/**
 * status === 0  -> el navegador no logró conectarse en absoluto
 *                  (backend detenido, sin internet, CORS, DNS, etc.)
 * 502/503/504    -> errores típicos de un proxy/gateway cuando el
 *                  servicio de backend no está respondiendo.
 */
function esErrorDeConexion(error: HttpErrorResponse): boolean {
  return error.status === 0 || [502, 503, 504].includes(error.status);
}

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getToken();

  // No agregar Authorization en endpoints públicos de auth
  const isAuthEndpoint = req.url.includes('/api/auth/');
  const authReq = token && !isAuthEndpoint
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !isAuthEndpoint) {
        authService.logout();
        router.navigate(['/login']);
      }

      if (esErrorDeConexion(error)) {
        // Reemplazamos el error "crudo" (status 0, sin body legible) por uno
        // con un mensaje amigable en error.error.message. Así, todo el código
        // existente que ya hace `err.error?.message || 'fallback'` muestra
        // automáticamente este mensaje en vez del código HTTP.
        const errorAmigable = new HttpErrorResponse({
          error: { message: MENSAJE_SERVICIO_NO_DISPONIBLE },
          status: error.status,
          statusText: error.statusText,
          url: error.url ?? undefined
        });
        return throwError(() => errorAmigable);
      }

      return throwError(() => error);
    })
  );
};