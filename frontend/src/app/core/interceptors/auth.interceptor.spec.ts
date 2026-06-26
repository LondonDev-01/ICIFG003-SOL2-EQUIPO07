import { TestBed } from '@angular/core/testing';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { authInterceptor, MENSAJE_SERVICIO_NO_DISPONIBLE } from './auth.interceptor';

describe('authInterceptor - mensajes de error amigables', () => {
  let httpClient: HttpClient;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    });

    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('convierte un error de conexión (backend caído, status 0) en el mensaje amigable', () => {
    let mensajeRecibido: string | undefined;
    let llegoRespuestaExitosa = false;

    httpClient.get('http://localhost:8080/api/salas').subscribe({
      next: () => { llegoRespuestaExitosa = true; },
      error: (err) => { mensajeRecibido = err.error?.message; }
    });

    const req = httpMock.expectOne('http://localhost:8080/api/salas');

    // Esto es exactamente lo que el navegador reporta cuando el backend
    // está detenido / no hay nadie escuchando en ese puerto: la conexión
    // nunca se establece y HttpClient entrega status 0.
    req.error(new ProgressEvent('error'), { status: 0, statusText: 'Unknown Error' });

    expect(llegoRespuestaExitosa).toBe(false);
    expect(mensajeRecibido).toBe(MENSAJE_SERVICIO_NO_DISPONIBLE);
  });

  it('convierte un 503 (gateway/servicio no disponible) en el mismo mensaje amigable', () => {
    let mensajeRecibido: string | undefined;

    httpClient.get('http://localhost:8080/api/salas').subscribe({
      next: () => {},
      error: (err) => { mensajeRecibido = err.error?.message; }
    });

    const req = httpMock.expectOne('http://localhost:8080/api/salas');
    req.flush('Service Unavailable', { status: 503, statusText: 'Service Unavailable' });

    expect(mensajeRecibido).toBe(MENSAJE_SERVICIO_NO_DISPONIBLE);
  });

  it('NO modifica errores normales del backend (ej. 404 con mensaje propio)', () => {
    let statusRecibido: number | undefined;
    let mensajeRecibido: string | undefined;

    httpClient.get('http://localhost:8080/api/salas/999').subscribe({
      next: () => {},
      error: (err) => {
        statusRecibido = err.status;
        mensajeRecibido = err.error?.message;
      }
    });

    const req = httpMock.expectOne('http://localhost:8080/api/salas/999');
    req.flush({ message: 'Sala no encontrada' }, { status: 404, statusText: 'Not Found' });

    expect(statusRecibido).toBe(404);
    expect(mensajeRecibido).toBe('Sala no encontrada');
  });
});
