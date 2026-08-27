import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { Api } from '../../service/api';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  credencial = {
    usuario: '',
    senha: ''
  };

  erro = '';

  constructor(
    private api: Api,
    private router: Router
  ) {
  }

  entrar() {
    this.erro = '';

    this.api.login(this.credencial).subscribe({
      next: (usuario) => {
        localStorage.setItem(
          'usuarioLogado',
          JSON.stringify(usuario)
        );

        this.router.navigate(['/home']);
      },
      error: (erro) => {
        this.erro = erro.error?.message
          || 'Usuário ou senha inválidos.';
      }
    });
  }
}
