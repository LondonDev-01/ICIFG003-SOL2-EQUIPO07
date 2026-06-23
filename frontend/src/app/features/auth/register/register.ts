import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { AuthService } from '../../../core/services/auth.service';

interface Carrera {
  id: number;
  nombreCarrera: string;
  facultad: string;
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly http = inject(HttpClient);

  readonly carreras = signal<Carrera[]>([]);
  readonly cargando = signal(false);
  readonly error = signal<string | null>(null);

  readonly form: FormGroup = this.fb.group({
    rut: ['', [Validators.required, Validators.pattern(/^\d{1,2}\.\d{3}\.\d{3}-[\dkK]$/)]],
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    apellido: ['', [Validators.required, Validators.maxLength(100)]],
    correo: ['', [Validators.required, Validators.email]],
    telefono: ['', [Validators.maxLength(20)]],
    idCarrera: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]]
  });

  ngOnInit(): void {
    if (this.auth.isAuthenticated()) {
      this.router.navigate(['/mis-reservas']);
      return;
    }
    this.http.get<Carrera[]>('http://localhost:8080/api/carreras').subscribe({
      next: (data) => this.carreras.set(data),
      error: () => this.carreras.set([])
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.cargando.set(true);
    this.error.set(null);
    this.auth.register(this.form.value).subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigate(['/salas']);
      },
      error: (err) => {
        this.cargando.set(false);
        if (err.status === 400) this.error.set(err.error?.message || 'Datos inválidos o duplicados');
        else if (err.status === 404) this.error.set(err.error?.message || 'Carrera no encontrada');
        else this.error.set('Error al registrar usuario');
      }
    });
  }

  formatearRut(event: Event): void {

    const input = event.target as HTMLInputElement;

    let valor = input.value.replace(/\./g, '');
    valor = valor.replace(/-/g, '');

    if (valor.length <= 1) {
      input.value = valor;
      this.form.get('rut')?.setValue(valor);
      this.form.get('rut')?.updateValueAndValidity();
      return;
    }

    const dv = valor.slice(-1);
    let cuerpo = valor.slice(0, -1);

    cuerpo = cuerpo.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

    const rutFormateado = `${cuerpo}-${dv}`;

    input.value = rutFormateado;

    this.form.get('rut')?.setValue(rutFormateado);
    this.form.get('rut')?.updateValueAndValidity();
  }
}