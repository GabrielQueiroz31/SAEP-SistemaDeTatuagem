import { Routes } from '@angular/router';

import { Agendamentos } from './pages/agendamentos/agendamentos';
import { Clientes } from './pages/clientes/clientes';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';

export const routes: Routes = [
  {
    path: 'login',
    component: Login
  },
  {
    path: 'home',
    component: Home,
    children: [
      {
        path: 'clientes',
        component: Clientes
      },
      {
        path: 'agendamentos',
        component: Agendamentos
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'agendamentos'
      }
    ]
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login'
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
