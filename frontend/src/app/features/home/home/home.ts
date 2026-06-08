import { NavbarComponent } from '../../../core/components/navbar/navbar';
import { Component } from '@angular/core';
import { FooterComponent } from "../../../core/components/footer/footer";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, FooterComponent],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {}
