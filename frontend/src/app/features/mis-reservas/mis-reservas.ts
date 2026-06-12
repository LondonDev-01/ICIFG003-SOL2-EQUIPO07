import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { AuthService } from '../../core/services/auth.service';
import { Reserva } from '../../core/models/reserva.model';

@Component({
  selector: 'app-mis-reservas',
  standalone: true,
  imports: [CommonModule, RouterLink, DatePipe],
  templateUrl: './mis-reservas.html',
  styleUrl: './mis-reservas.css'
})
export class MisReservas implements OnInit {
  private readonly http = inject(HttpClient);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly currentUser = this.auth.currentUser;
  readonly reservas = signal<Reserva[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.http.get<Reserva[]>('http://localhost:8080/api/mis-reservas').subscribe({
      next: (data) => {
        this.reservas.set(data);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.error.set(err.error?.message || 'Error al cargar tus reservas');
      }
    });
  }

  cancelarReserva(id: number): void {
    if (!confirm('¿Cancelar esta reserva?')) return;
    this.http.delete(`http://localhost:8080/api/reservas/${id}`).subscribe({
      next: () => this.reservas.update((list) => list.filter((r) => r.id !== id)),
      error: () => alert('No se pudo cancelar la reserva')
    });
  }

  logout(): void {
    this.auth.logout();
  }
}