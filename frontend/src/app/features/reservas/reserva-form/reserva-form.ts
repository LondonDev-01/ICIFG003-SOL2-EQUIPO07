import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';

@Component({
  selector: 'app-reserva-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reserva-form.html',
  styleUrls: ['./reserva-form.css']
})
export class ReservaForm implements OnInit {
  @Input() salaSeleccionada: any = null;

  reservaForm!: FormGroup;
  estudiantesFiltrados: any[] = [];
  
  // Mock data para estudiantes, en un caso real se llamaría al backend
  estudiantesMock = [
    { id: 1, rut: '12345678-9', nombre: 'Juan Pérez', correo: 'juan.perez@universidad.cl' },
    { id: 2, rut: '98765432-1', nombre: 'María González', correo: 'maria.g@universidad.cl' }
  ];

  horariosDisponibles = [
    '08:00 - 09:30',
    '09:40 - 11:10',
    '11:20 - 12:50',
    '14:00 - 15:30',
    '15:40 - 17:10'
  ];

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.reservaForm = this.fb.group({
      estudiante: ['', [Validators.required]],
      correo: ['', [Validators.required, Validators.email]],
      fecha: ['', [Validators.required, this.fechaNoPasadaValidator]],
      horario: ['', [Validators.required]],
      observaciones: ['', [Validators.required, Validators.minLength(15)]]
    });

    // Filtro simulado al escribir
    this.reservaForm.get('estudiante')?.valueChanges.subscribe(value => {
      if (typeof value === 'string' && value.length > 2) {
        this.estudiantesFiltrados = this.estudiantesMock.filter(e => 
          e.nombre.toLowerCase().includes(value.toLowerCase()) || 
          e.rut.includes(value)
        );
      } else {
        this.estudiantesFiltrados = [];
      }
    });
  }

  fechaNoPasadaValidator(control: AbstractControl): ValidationErrors | null {
    const seleccionada = new Date(control.value);
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0); // Solo comparar fecha, no hora
    // se le suma el offset para evitar problemas de zona horaria si aplica
    seleccionada.setMinutes(seleccionada.getMinutes() + seleccionada.getTimezoneOffset());
    return seleccionada < hoy ? { fechaPasada: true } : null;
  }

  seleccionarEstudiante(estudiante: any) {
    this.reservaForm.patchValue({
      estudiante: estudiante.nombre,
      correo: estudiante.correo
    });
    this.estudiantesFiltrados = [];
  }

  onSubmit() {
    if (this.reservaForm.valid) {
      console.log('Reserva creada:', this.reservaForm.value);
      // Aquí se debería llamar al servicio de reserva conectándose al backend (Control superposición de horario)
      alert("Solicitud de reserva enviada correctamente.");
    } else {
      this.reservaForm.markAllAsTouched();
    }
  }
}
