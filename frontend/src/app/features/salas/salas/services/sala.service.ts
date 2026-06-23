import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Sala } from '../models/sala.model';

@Injectable({
  providedIn: 'root'
})
export class SalaService {

  private apiUrl = 'http://localhost:8080/api/salas';

  constructor(private http: HttpClient) {}

  obtenerSalas(): Observable<Sala[]> {
    return this.http.get<Sala[]>(this.apiUrl);
  }

  obtenerHorarios() {
    return this.http.get<any[]>(
      'http://localhost:8080/api/horarios'
    );
  }

  obtenerReservas() {
    return this.http.get<any[]>(
      'http://localhost:8080/api/reservas'
    );
  }
}