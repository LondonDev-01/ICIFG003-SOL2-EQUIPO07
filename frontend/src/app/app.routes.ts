import { Routes } from '@angular/router';

import { Home } from './features/home/home/home';
import { Salas } from './features/salas/salas/salas';

export const routes: Routes = [

  {
    path: '',
    component: Home
  },

  {
    path: 'salas',
    component: Salas
  }

];