import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

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
  private readonly authService = inject(AuthService);

  reservaForm!: FormGroup;
  readonly cargando = signal(false);
  readonly error = signal<string | null>(null);
  readonly exito = signal(false);

  readonly carreras = signal<any[]>([]);
  readonly salas = signal<any[]>([]);
  readonly horarios = signal<any[]>([]);
  readonly estados = signal<any[]>([]);
  readonly reservasExistentes = signal<any[]>([]);

  isEditMode = false;
  reservaId: number | null = null;
  salaSeleccionada: any = null;
  nombreUsuario = '';

  ngOnInit(): void {

    const usuario = this.authService.currentUser();

    if (usuario) {
      this.nombreUsuario = usuario.nombre;
    }

    const salaIdParam = this.route.snapshot.queryParamMap.get('salaId');
    const idParam = this.route.snapshot.paramMap.get('id');
    const fechaParam = this.route.snapshot.queryParamMap.get('fecha');
    

    if (idParam) {
      this.isEditMode = true;
      this.reservaId = Number(idParam);
    }

    this.reservaForm = this.fb.group({
      idSala: [salaIdParam || '', [Validators.required]],
      fecha: [fechaParam || '', [Validators.required, this.fechaNoPasadaValidator]],
      idHorario: ['', [Validators.required]],
      idEstado: ['', [Validators.required]],
      observaciones: ['', [Validators.required, Validators.minLength(15)]]
    });

    if (!fechaParam) {
      const hoy = new Date().toISOString().split('T')[0];

      this.reservaForm.patchValue({
        fecha: hoy
      });
    }

    this.reservaForm.patchValue({
      idEstado: 2
    });

    this.cargarCatalogos();

    if (this.isEditMode) {
      this.cargarReserva();
    }

    this.cargarReservas();

    this.reservaForm.get('fecha')?.valueChanges.subscribe(() => {
      this.cargarReservas();
    });
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
    this.http.get<any[]>('http://localhost:8080/api/salas').subscribe((d) => {
      this.salas.set(d);

      const salaId = this.route.snapshot.queryParamMap.get('salaId');

      if (salaId) {
        this.salaSeleccionada = d.find(
          s => s.id === Number(salaId)
        );
      }
    });
    this.http.get<any[]>('http://localhost:8080/api/horarios').subscribe((d) => this.horarios.set(d));
    this.http.get<any[]>('http://localhost:8080/api/estados-reserva').subscribe((d) => this.estados.set(d));
  }

  obtenerImagenSala(): string {

  switch (this.salaSeleccionada?.codigoSala) {

    case 'A101':
      return '/salas/sala101.png';

    case 'B202':
      return '/salas/sala202.png';

    case 'C303':
      return '/salas/sala303.png';

    case 'D404':
      return '/salas/sala404.png';

    case 'E505':
      return '/salas/sala505.png';

    case 'F606':
      return '/salas/sala606.png';

    default:
      return '/salas/sala101.png';
  }
}

cargarReservas(): void {

  const salaId = this.route.snapshot.queryParamMap.get('salaId');
  const fechaSeleccionada = this.reservaForm?.get('fecha')?.value;

  if (!salaId) return;

  this.http.get<any[]>('http://localhost:8080/api/reservas')
    .subscribe({
      next: (reservas) => {

        const filtradas = reservas.filter(
          r =>
            r.idSala === Number(salaId) &&
            (!fechaSeleccionada || r.fechaReserva === fechaSeleccionada)
        );

        this.reservasExistentes.set(filtradas);

        const horariosActualizados = this.horarios().map(h => ({
          ...h,
          reservado: filtradas.some(r => r.idHorario === h.id)
        }));

        this.horarios.set(horariosActualizados);

      }
    });

}

 fechaNoPasadaValidator(
    control: AbstractControl
  ): ValidationErrors | null {

    if (!control.value) {
      return null;
    }

    const partes = control.value.split('-');

    const fechaSeleccionada = new Date(
      Number(partes[0]),
      Number(partes[1]) - 1,
      Number(partes[2])
    );

    const hoy = new Date();

    hoy.setHours(0, 0, 0, 0);

    if (fechaSeleccionada < hoy) {
      return { fechaPasada: true };
    }

    return null;
  }

  onSubmit(): void {

    console.log('BOTON PRESIONADO');
    console.log(this.reservaForm.value);
    console.log('VALIDO:', this.reservaForm.valid);

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
          this.error.set(err.error?.message || 'Ya tienes una reserva activa para esta fecha. Solo puedes reservar una sala por día');
        }
      });
    }
  }
}