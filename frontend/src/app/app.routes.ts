import { Routes } from '@angular/router';

import { Home } from './features/home/home/home';
import { Salas } from './features/salas/salas/salas';
import { Contacto } from './features/contacto/contacto';
import { Ayuda } from './features/ayuda/ayuda';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { MisReservas } from './features/mis-reservas/mis-reservas';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    component: Home
  },
  {
    path: 'salas',
    component: Salas
  },
  {
    path: 'contacto',
    component: Contacto
  },
  {
    path: 'ayuda',
    component: Ayuda
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'register',
    component: Register
  },
  {
    path: 'mis-reservas',
    component: MisReservas,
    canActivate: [authGuard]
  }
];