import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-reserva-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reserva-form.html',
  styleUrls: ['./reserva-form.css']
})
export class ReservaForm implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  reservaForm!: FormGroup;
  readonly cargando = signal(false);
  readonly error = signal<string | null>(null);
  readonly exito = signal(false);

  readonly carreras = signal<any[]>([]);
  readonly salas = signal<any[]>([]);
  readonly horarios = signal<any[]>([]);
  readonly estados = signal<any[]>([]);

  isEditMode = false;
  reservaId: number | null = null;
  salaSeleccionada: any = null;

  ngOnInit(): void {
    const salaIdParam = this.route.snapshot.queryParamMap.get('salaId');
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.isEditMode = true;
      this.reservaId = Number(idParam);
    }

    this.reservaForm = this.fb.group({
      idSala: [salaIdParam || '', [Validators.required]],
      fecha: ['', [Validators.required, this.fechaNoPasadaValidator]],
      idHorario: ['', [Validators.required]],
      idEstado: ['', [Validators.required]],
      observaciones: ['', [Validators.required, Validators.minLength(15)]]
    });

    this.cargarCatalogos();

    if (this.isEditMode) {
      this.cargarReserva();
    }
  }

  cargarReserva(): void {
    if (!this.reservaId) return;
    this.http.get<any>(`http://localhost:8080/api/reservas/${this.reservaId}`).subscribe({
      next: (reserva) => {
        const idS = this.salas().find(s => s.nombreSala === reserva.nombreSala)?.id || reserva.idSala;
        this.reservaForm.patchValue({
          idSala: idS || '',
          fecha: reserva.fechaReserva?.split('T')[0] || reserva.fechaReserva,
          idHorario: reserva.idHorario || '',
          idEstado: reserva.idEstado || '',
          observaciones: reserva.observacion
        });
      },
      error: () => this.error.set('Error al cargar la reserva a editar')
    });
  }

  cargarCatalogos(): void {
    this.http.get<any[]>('http://localhost:8080/api/carreras').subscribe((d) => this.carreras.set(d));
    this.http.get<any[]>('http://localhost:8080/api/salas').subscribe((d) => this.salas.set(d));
    this.http.get<any[]>('http://localhost:8080/api/horarios').subscribe((d) => this.horarios.set(d));
    this.http.get<any[]>('http://localhost:8080/api/estados-reserva').subscribe((d) => this.estados.set(d));
  }

  fechaNoPasadaValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const seleccionada = new Date(control.value);
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    return seleccionada < hoy ? { fechaPasada: true } : null;
  }

  onSubmit(): void {
    if (this.reservaForm.invalid) {
      this.reservaForm.markAllAsTouched();
      return;
    }
    this.cargando.set(true);
    this.error.set(null);
    this.exito.set(false);

    // El idEstudiante NO se envía: el backend lo extrae del JWT.
    // Solo se envía la información de la reserva (sala, horario, estado, fecha, observación).
    const body = {
      fechaReserva: this.reservaForm.value.fecha,
      observacion: this.reservaForm.value.observaciones,
      idSala: Number(this.reservaForm.value.idSala),
      idHorario: Number(this.reservaForm.value.idHorario),
      idEstado: Number(this.reservaForm.value.idEstado)
    };

    if (this.isEditMode && this.reservaId) {
      this.http.put(`http://localhost:8080/api/reservas/${this.reservaId}`, body).subscribe({
        next: () => {
          this.cargando.set(false);
          this.exito.set(true);
          setTimeout(() => this.router.navigate(['/mis-reservas']), 1500);
        },
        error: (err) => {
          this.cargando.set(false);
          this.error.set(err.error?.message || 'Error al editar la reserva');
        }
      });
    } else {
      this.http.post('http://localhost:8080/api/reservas', body).subscribe({
        next: () => {
          this.cargando.set(false);
          this.exito.set(true);
          this.reservaForm.reset();
          setTimeout(() => this.router.navigate(['/mis-reservas']), 1500);
        },
        error: (err) => {
          this.cargando.set(false);
          this.error.set(err.error?.message || 'Error al crear la reserva');
        }
      });
    }
  }
}