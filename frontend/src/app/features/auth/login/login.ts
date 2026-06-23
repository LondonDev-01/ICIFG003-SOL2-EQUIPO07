import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly cargando = signal(false);
  readonly error = signal<string | null>(null);

  readonly form: FormGroup = this.fb.group({
    rut: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]]
  });

  ngOnInit(): void {
    // Si ya está logueado, redirigir a /mis-reservas
    if (this.auth.isAuthenticated()) {
      this.router.navigate(['/mis-reservas']);
    }
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.cargando.set(true);
    this.error.set(null);
    this.auth.login(this.form.value).subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigate(['/salas']);
      },
      error: (err) => {
        this.cargando.set(false);
        if (err.status === 401) this.error.set('Credenciales inválidas');
        else if (err.status === 429) this.error.set('Demasiados intentos. Intenta más tarde.');
        else this.error.set(err.error?.message || 'Error al iniciar sesión');
      }
    });
  }

  formatearRut(event: Event): void {

    const input = event.target as HTMLInputElement;

    let valor = input.value.replace(/\./g, '');
    valor = valor.replace(/-/g, '');

    if (valor.length <= 1) {
      input.value = valor;
      this.form.get('rut')?.setValue(valor, { emitEvent: false });
      return;
    }

    const dv = valor.slice(-1);
    let cuerpo = valor.slice(0, -1);

    cuerpo = cuerpo.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

    const rutFormateado = `${cuerpo}-${dv}`;

    input.value = rutFormateado;

    this.form.get('rut')?.setValue(rutFormateado, {
      emitEvent: false
    });
  }

}