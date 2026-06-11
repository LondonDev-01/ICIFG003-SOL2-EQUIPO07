import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-sala-card',
  standalone: true,
  imports: [],
  templateUrl: './sala-card.html',
  styleUrl: './sala-card.css',
})
export class SalaCard {

  @Input() nombre = '';

  @Input() capacidad = 0;

  @Input() ubicacion = '';

  @Input() horario = '';

  @Input() imagen = '';

  @Output() verReservas = new EventEmitter<void>();

  @Output() reservar = new EventEmitter<void>();

  onVerReservas() {
    this.verReservas.emit();
  }

  onReservar() {
    this.reservar.emit();
  }
}

