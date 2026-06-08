import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-mensaje',
  standalone: true,
  imports: [],
  templateUrl: './mensaje.html',
  styleUrl: './mensaje.css',
})
export class Mensaje {

  @Input() texto = '';
}

