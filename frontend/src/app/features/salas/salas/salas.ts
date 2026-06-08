import { NavbarComponent } from '../../../core/components/navbar/navbar';
import { FooterComponent } from '../../../core/components/footer/footer';
import { SalaCard } from '../../../shared/components/sala-card/sala-card';
import { Component } from '@angular/core';

@Component({
  selector: 'app-salas',
  standalone: true,
  imports: [
    NavbarComponent,
    FooterComponent,
    SalaCard
  ],
  templateUrl: './salas.html',
  styleUrl: './salas.css',
})
export class Salas {}