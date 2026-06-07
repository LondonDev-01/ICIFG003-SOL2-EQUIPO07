import { NavbarComponent } from '../../../core/components/navbar/navbar';
import { FooterComponent } from '../../../core/components/footer/footer';
import { Component } from '@angular/core';

@Component({
  selector: 'app-salas',
  standalone: true,
  imports: [
    NavbarComponent,
    FooterComponent
  ],
  templateUrl: './salas.html',
  styleUrl: './salas.css',
})
export class Salas {}