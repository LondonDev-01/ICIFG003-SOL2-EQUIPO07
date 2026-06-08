import { NavbarComponent } from '../../../core/components/navbar/navbar';
import { Component } from '@angular/core';
import { FooterComponent } from "../../../core/components/footer/footer";
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, FooterComponent, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {}
