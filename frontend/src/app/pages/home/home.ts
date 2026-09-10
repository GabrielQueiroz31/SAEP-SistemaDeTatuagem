import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Api } from '../../service/api';

@Component({
  selector: 'app-home',
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  nomeUsuario = 'Administrador';

  constructor(
    private router: Router,
    private api: Api
  ) {}

  ngOnInit() {
    const usuario = JSON.parse(localStorage.getItem('usuarioLogado') || '{}');

    this.nomeUsuario = usuario.nome || 'Administrador';
  }

  logout() {
    this.api.logout().subscribe({
      next: () => {
        localStorage.removeItem('usuarioLogado');
        this.router.navigate(['/login']);
      },
      error: (erro) => {
        if (erro.status !== 401) {
          alert('Não foi possível encerrar a sessão. Tente novamente.');
        }
      }
    });
  }
}
