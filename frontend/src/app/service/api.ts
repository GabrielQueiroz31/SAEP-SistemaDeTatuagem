import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface Cliente {
  id?: number;
  nome: string;
  documento: string;
  telefone: string;
  email?: string;
}

export interface Maca {
  id: number;
  nome: string;
}

export interface Tatuador {
  id: number;
  nome: string;
  especialidade: string;
  ativo: boolean;
}

export interface Agendamento {
  id?: number;
  cliente: Cliente;
  maca?: Maca;
  tatuador: Tatuador;
  data: string;
  hora: string;
  servico: string;
  observacao?: string;
}

@Injectable({
  providedIn: 'root'
})
export class Api {

  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {
  }

  login(dados: { usuario: string; senha: string }) {
    return this.http.post<{ nome: string }>(
      this.baseUrl + '/auth/login',
      dados
    );
  }

  getClientes(busca = '') {
    return this.http.get<Cliente[]>(this.baseUrl + '/clientes', {
      params: busca ? { busca } : {}
    });
  }

  salvarCliente(cliente: Cliente) {
    if (cliente.id) {
      return this.http.put<Cliente>(
        this.baseUrl + '/clientes/' + cliente.id,
        cliente
      );
    }

    return this.http.post<Cliente>(this.baseUrl + '/clientes', cliente);
  }

  excluirCliente(id: number) {
    return this.http.delete(this.baseUrl + '/clientes/' + id);
  }

  getMacas() {
    return this.http.get<Maca[]>(this.baseUrl + '/macas');
  }

  getTatuadores() {
    return this.http.get<Tatuador[]>(this.baseUrl + '/tatuadores');
  }

  getAgendamentos() {
    return this.http.get<Agendamento[]>(this.baseUrl + '/agendamentos');
  }

  salvarAgendamento(agendamento: Agendamento) {
    if (agendamento.id) {
      return this.http.put<Agendamento>(
        this.baseUrl + '/agendamentos/' + agendamento.id,
        agendamento
      );
    }

    return this.http.post<Agendamento>(
      this.baseUrl + '/agendamentos',
      agendamento
    );
  }

  excluirAgendamento(id: number) {
    return this.http.delete(this.baseUrl + '/agendamentos/' + id);
  }
}
