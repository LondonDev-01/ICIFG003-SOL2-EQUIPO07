# API de Autenticación - Guía para Frontend

Este documento describe el contrato de los endpoints de autenticación y cómo consumirlos desde Angular (o cualquier frontend SPA).

## 1. Registro de Estudiante

**Endpoint**: `POST /api/auth/register`
**Auth requerida**: NO (público)
**Content-Type**: `application/json`

**Request body**:
```json
{
  "rut": "12.345.678-9",
  "nombre": "Juan",
  "apellido": "Pérez",
  "correo": "juan@correo.cl",
  "telefono": "+56912345678",
  "idCarrera": 1,
  "password": "1234"
}
```

**Response 201 Created**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "rut": "12345678-9",
  "nombre": "Juan Pérez",
  "expiresIn": 3600
}
```

**Errores**:
- `400` — Validación fallida (rut/correo duplicado, password no es 4 dígitos, etc.)
- `404` — Carrera con `idCarrera` no existe

## 2. Login

**Endpoint**: `POST /api/auth/login`
**Auth requerida**: NO (público)
**Content-Type**: `application/json`

**Request body**:
```json
{
  "rut": "12.345.678-9",
  "password": "1234"
}
```

**Response 200 OK**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "rut": "12345678-9",
  "nombre": "Juan Pérez",
  "expiresIn": 3600
}
```

**Errores**:
- `400` — Faltan campos o password no es 4 dígitos
- `401` — Credenciales inválidas
- `429` — Demasiados intentos (rate limit: 5 por minuto, lockout 15 min tras 5 fallos)

## 3. Obtener Mis Reservas

**Endpoint**: `GET /api/mis-reservas`
**Auth requerida**: SÍ (JWT en header)
**Header**: `Authorization: Bearer <token>`

**Response 200 OK**:
```json
[
  {
    "id": 1,
    "fechaReserva": "2026-06-15",
    "observacion": "Estudio para examen final",
    "fechaCreacion": "2026-06-10T10:30:00",
    "idEstudiante": 5,
    "nombreEstudiante": "Juan",
    "idSala": 1,
    "nombreSala": "Sala 101",
    "idHorario": 1,
    "bloqueHorario": "08:00",
    "idEstado": 1,
    "nombreEstado": "Confirmada"
  }
]
```

**Errores**:
- `401` — Token ausente, inválido o expirado

## 4. Endpoints Protegidos

A partir de esta versión, los siguientes verbos requieren JWT:
- `POST /api/**` (crear recursos)
- `PUT /api/**` (actualizar)
- `DELETE /api/**` (eliminar)
- `GET /api/mis-reservas` (nuevo)

Los `GET` regulares (listar/consultar) siguen siendo públicos:
- `GET /api/carreras`, `/api/edificios`, `/api/salas`, etc.

## 5. Headers en Requests Protegidos

```http
GET /api/mis-reservas HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

## 6. Formato de RUT Aceptado

El backend normaliza el RUT eliminando puntos, guiones y el dígito verificador.
Formatos aceptados (todos equivalentes):
- `12345678-9` (con guión y DV)
- `12.345.678-9` (con puntos y guión)
- `123456789` (solo números)
- `12.345.678` (con puntos, sin guión)

## 7. Expiración del Token

- `expiresIn: 3600` (1 hora) por defecto
- Configurable vía `jwt.expiration-hours` en `application.properties`
- Cuando el token expira, los endpoints protegidos devuelven `401`
- El usuario debe volver a hacer login (no hay refresh token por ahora)

## 8. Ejemplo de AuthService en Angular

```typescript
import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

interface AuthResponse {
  token: string;
  rut: string;
  nombre: string;
  expiresIn: number;
}

interface LoginRequest {
  rut: string;
  password: string;
}

interface RegisterRequest extends LoginRequest {
  nombre: string;
  apellido: string;
  correo: string;
  telefono?: string;
  idCarrera: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'auth_user';
  private readonly API_URL = 'http://localhost:8080/api/auth';

  // Signals para estado reactivo (Angular 17+)
  private readonly tokenSignal = signal<string | null>(this.getStoredToken());
  private readonly userSignal = signal<{ rut: string; nombre: string } | null>(this.getStoredUser());

  readonly isAuthenticated = computed(() => this.tokenSignal() !== null);
  readonly currentUser = this.userSignal.asReadonly();

  constructor(private http: HttpClient, private router: Router) {}

  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, data).pipe(
      tap(response => this.setSession(response))
    );
  }

  login(data: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, data).pipe(
      tap(response => this.setSession(response))
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.tokenSignal.set(null);
    this.userSignal.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this.tokenSignal();
  }

  private setSession(response: AuthResponse): void {
    localStorage.setItem(this.TOKEN_KEY, response.token);
    localStorage.setItem(this.USER_KEY, JSON.stringify({ rut: response.rut, nombre: response.nombre }));
    this.tokenSignal.set(response.token);
    this.userSignal.set({ rut: response.rut, nombre: response.nombre });
  }

  private getStoredToken(): string | null {
    return typeof localStorage !== 'undefined' ? localStorage.getItem(this.TOKEN_KEY) : null;
  }

  private getStoredUser(): { rut: string; nombre: string } | null {
    if (typeof localStorage === 'undefined') return null;
    const raw = localStorage.getItem(this.USER_KEY);
    return raw ? JSON.parse(raw) : null;
  }
}
```

## 9. Ejemplo de HTTP Interceptor (inyectar JWT automáticamente)

```typescript
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getToken();

  // Solo agregar Authorization si hay token y NO es endpoint público de auth
  if (token && !req.url.includes('/api/auth/')) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(req).pipe(
    catchError((error) => {
      if (error.status === 401 && !req.url.includes('/api/auth/login')) {
        authService.logout();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
```

**Registro del interceptor** (en `app.config.ts` o `app.module.ts`):

```typescript
import { ApplicationConfig } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './auth/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor])),
    // ... otros providers
  ]
};
```

## 10. Ejemplo de Guard para Rutas Protegidas

```typescript
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }
  router.navigate(['/login']);
  return false;
};
```

Uso en rutas:
```typescript
import { Routes } from '@angular/router';
import { authGuard } from './auth/auth.guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./login/login.component') },
  { path: 'mis-reservas', loadComponent: () => import('./mis-reservas/mis-reservas.component'), canActivate: [authGuard] },
  // ...
];
```

## 11. Notas Importantes

1. **El campo `password` se elimina automáticamente de las respuestas GET de `/api/estudiantes`** (es interno, nunca debe exponerse al cliente).
2. **El token NO se puede revocar** antes de su expiración (no hay blacklist). Si el usuario cambia la contraseña, los tokens anteriores siguen siendo válidos hasta expirar.
3. **El frontend debe manejar 401** redirigiendo al login.
4. **CORS está habilitado** para `http://localhost:4200` en desarrollo.

## 12. Diagrama de Flujo

```
┌──────────┐                  ┌──────────┐                ┌──────────┐
│ Frontend │ POST /register   │ Backend  │  save + hash  │  Postgres │
│          │ ──────────────►  │          │ ────────────► │          │
│          │ ◄──────────────  │          │  return JWT   │          │
│          │ 201 + token      │          │                │          │
└──────────┘                  └──────────┘                └──────────┘
     │
     │ save token en localStorage
     ▼
┌──────────┐                  ┌──────────┐
│ Frontend │ GET /mis-reservas│ Backend  │
│          │ Bearer <token>   │          │
│          │ ──────────────►  │          │
│          │ ◄──────────────  │          │
│          │ 200 + reservas   │          │
└──────────┘                  └──────────┘
```
