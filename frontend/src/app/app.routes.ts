import { Routes } from '@angular/router';

import { Home } from './features/home/home/home';
import { Salas } from './features/salas/salas/salas';
import { Contacto } from './features/contacto/contacto';
import { Ayuda } from './features/ayuda/ayuda';

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
  }

];