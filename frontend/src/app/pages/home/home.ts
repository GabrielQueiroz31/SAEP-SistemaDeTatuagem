import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [RouterLink, RouterOutlet],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {

  nomeUsuario = 'Administrador';

  constructor(private router: Router) {
  }

  ngOnInit() {
    const usuario = JSON.parse(
      localStorage.getItem('usuarioLogado') || '{}'
    );

    this.nomeUsuario = usuario.nome || 'Administrador';
  }

  logout() {
    localStorage.removeItem('usuarioLogado');
    this.router.navigate(['/login']);
  }
}
