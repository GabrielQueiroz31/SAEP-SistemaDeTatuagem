import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Api, Cliente } from '../../service/api';

@Component({
  selector: 'app-clientes',
  imports: [FormsModule],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css'
})
export class Clientes implements OnInit {
  clientes: Cliente[] = [];
  busca = '';
  erro = '';
  cliente: Cliente = this.novoCliente();

  constructor(
    private api: Api,
    private changeDetectorRef: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.listar();
  }

  novoCliente(): Cliente {
    return {
      nome: '',
      documento: '',
      telefone: '',
      email: ''
    };
  }

  listar() {
    this.api.getClientes(this.busca).subscribe({
      next: (dados) => {
        this.clientes = dados;
        this.changeDetectorRef.markForCheck();
      },
      error: () => {
        this.erro = 'Não foi possível carregar os clientes.';
        this.changeDetectorRef.markForCheck();
      }
    });
  }

  salvar() {
    this.erro = '';
    this.changeDetectorRef.markForCheck();

    this.api.salvarCliente(this.cliente).subscribe({
      next: () => {
        this.cancelar();
        this.changeDetectorRef.markForCheck();
        this.listar();
      },
      error: (erro) => {
        this.erro = erro.error?.message || 'Não foi possível salvar o cliente.';
        this.changeDetectorRef.markForCheck();
      }
    });
  }

  editar(cliente: Cliente) {
    this.cliente = { ...cliente };
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }

  excluir(cliente: Cliente) {
    if (!cliente.id || !confirm('Excluir ' + cliente.nome + '?')) {
      return;
    }

    this.api.excluirCliente(cliente.id).subscribe({
      next: () => this.listar(),
      error: (erro) => {
        this.erro = erro.error?.message || 'Não foi possível excluir o cliente.';
        this.changeDetectorRef.markForCheck();
      }
    });
  }

  cancelar() {
    this.cliente = this.novoCliente();
  }
}
