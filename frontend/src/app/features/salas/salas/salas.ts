import { Component, OnInit, AfterViewChecked } from '@angular/core';

import { NavbarComponent } from '../../../core/components/navbar/navbar';
import { FooterComponent } from '../../../core/components/footer/footer';
import { SalaCard } from '../../../shared/components/sala-card/sala-card';
import { CommonModule } from '@angular/common';
import { SalaService } from './services/sala.service';
import { Sala } from './models/sala.model';
import { ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-salas',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NavbarComponent,
    FooterComponent,
    SalaCard
  ],
  templateUrl: './salas.html',
  styleUrl: './salas.css',
})
export class Salas implements OnInit {

  salas: Sala[] = [];

  salasFiltradas: Sala[] = [];
  textoBusqueda: string = '';
  capacidadSeleccionada: string = '';
  fechaSeleccionada: string = '';
  ordenSeleccionado: string = 'nombre';

  constructor(
    private salaService: SalaService,
    private cdr: ChangeDetectorRef
  ) {
    
  }

  ngOnInit(): void {
    
    this.salaService.obtenerSalas().subscribe({
      next: (data) => {

      this.salas = data;
      this.salasFiltradas = data;

      this.cdr.detectChanges();

  },

  

  error: (error) => {
    console.error('ERROR:', error);
  }
});
  }

  filtrarSalas(): void {

    const texto = this.textoBusqueda.toLowerCase();

    this.salasFiltradas = this.salas.filter(sala => {

      const coincideTexto =
        sala.codigoSala.toLowerCase().includes(texto) ||
        sala.nombreSala.toLowerCase().includes(texto);

      let coincideCapacidad = true;

      if (this.capacidadSeleccionada === '4') {
        coincideCapacidad = sala.capacidad <= 4;
      }

      if (this.capacidadSeleccionada === '8') {
        coincideCapacidad = sala.capacidad <= 8;
      }

      if (this.capacidadSeleccionada === '9') {
        coincideCapacidad = sala.capacidad > 8;
      }

      return coincideTexto && coincideCapacidad;
    });

    if (this.ordenSeleccionado === 'nombre') {

      this.salasFiltradas.sort((a, b) =>
        a.nombreSala.localeCompare(b.nombreSala)
      );

    }

    if (this.ordenSeleccionado === 'capacidad') {

      this.salasFiltradas.sort((a, b) =>
        a.capacidad - b.capacidad
      );

    }

  }

  obtenerImagen(codigoSala: string): string {

    switch (codigoSala) {
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

  ngDoCheck(): void {
  console.log('RENDER ->', this.salas.length);
}
}
