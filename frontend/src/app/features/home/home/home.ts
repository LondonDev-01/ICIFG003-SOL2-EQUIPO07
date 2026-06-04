import { NavbarComponent } from '../../../core/components/navbar/navbar';
import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {}
