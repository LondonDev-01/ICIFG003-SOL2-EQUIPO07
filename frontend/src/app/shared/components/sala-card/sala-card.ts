import { Component, Input } from '@angular/core';

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
}
