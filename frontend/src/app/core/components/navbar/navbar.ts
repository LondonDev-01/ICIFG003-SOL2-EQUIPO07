import { Component, inject, HostListener } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class NavbarComponent {
  mostrarMenu = false;
  
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly isAuthenticated = this.auth.isAuthenticated;
  readonly currentUser = this.auth.currentUser;

  toggleMenu(): void {
    this.mostrarMenu = !this.mostrarMenu;
  }

  @HostListener('document:click', ['$event'])
  onClickFuera(event: MouseEvent): void {

    const elemento = event.target as HTMLElement;

    if (!elemento.closest('.user-menu')) {
      this.mostrarMenu = false;
    }
  }

  logout(): void {
    this.auth.logout();
  }
}