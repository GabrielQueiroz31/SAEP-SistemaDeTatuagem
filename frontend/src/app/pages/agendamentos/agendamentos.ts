import { DatePipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';

import { Agendamento, Api, Cliente, Maca, Tatuador } from '../../service/api';

type FormAgendamento = {
  id?: number;
  clienteId: number | null;
  macaId: number | null;
  tatuadorId: number | null;
  data: string;
  hora: string;
  servico: string;
  observacao: string;
};

@Component({
  selector: 'app-agendamentos',
  imports: [FormsModule, DatePipe],
  templateUrl: './agendamentos.html',
  styleUrl: './agendamentos.css'
})
export class Agendamentos implements OnInit {
  clientes: Cliente[] = [];
  macas: Maca[] = [];
  tatuadores: Tatuador[] = [];
  agendamentos: Agendamento[] = [];
  erro = '';
  sucesso = '';
  salvando = false;
  formulario: FormAgendamento = this.novoFormulario();

  constructor(
    private api: Api,
    private changeDetectorRef: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.carregar();
  }

  novoFormulario(): FormAgendamento {
    return {
      clienteId: null,
      macaId: null,
      tatuadorId: null,
      data: '',
      hora: '',
      servico: '',
      observacao: ''
    };
  }

  carregar() {
    this.api.getClientes().subscribe({
      next: (dados) => {
        this.clientes = dados;
        this.changeDetectorRef.markForCheck();
      },
      error: () => this.mostrarErro('Não foi possível carregar os clientes.')
    });

    this.api.getMacas().subscribe({
      next: (dados) => {
        this.macas = dados;
        this.changeDetectorRef.markForCheck();
      },
      error: () => this.mostrarErro('Não foi possível carregar as macas.')
    });

    this.api.getTatuadores().subscribe({
      next: (dados) => {
        this.tatuadores = dados;
        this.changeDetectorRef.markForCheck();
      },
      error: () => this.mostrarErro('Não foi possível carregar os tatuadores.')
    });

    this.listar();
  }

  listar() {
    this.api.getAgendamentos().subscribe({
      next: (dados) => {
        this.agendamentos = dados;
        this.changeDetectorRef.markForCheck();
      },
      error: () => this.mostrarErro('Não foi possível carregar os agendamentos.')
    });
  }

  salvar(form: NgForm) {
    if (this.salvando) {
      return;
    }

    this.erro = '';
    this.sucesso = '';
    form.control.markAllAsTouched();
    if (this.horarioOcupado()) {
      this.mostrarErro(
        'Horário ocupado: já existe um agendamento para este tatuador ou esta maca nesta data e hora. Escolha outro horário ou recurso.'
      );
      return;
    }
    if (form.invalid) {
      this.mostrarErro('Preencha os campos obrigatórios indicados abaixo antes de salvar.');
      return;
    }

    if (
      this.formulario.clienteId === null ||
      this.formulario.macaId === null ||
      this.formulario.tatuadorId === null
    ) {
      this.mostrarErro('Selecione o cliente, a maca e o tatuador.');
      return;
    }

    const cliente = this.clientes.find((item) => item.id === this.formulario.clienteId);
    const maca = this.macas.find((item) => item.id === this.formulario.macaId);
    const tatuador = this.tatuadores.find((item) => item.id === this.formulario.tatuadorId);

    if (!cliente || !maca || !tatuador) {
      this.mostrarErro('Cliente, maca ou tatuador inválido.');
      return;
    }

    const dados: Agendamento = {
      id: this.formulario.id,
      cliente,
      maca,
      tatuador,
      data: this.formulario.data,
      hora: this.formulario.hora,
      servico: this.formulario.servico,
      observacao: this.formulario.observacao
    };

    this.salvando = true;
    this.api.salvarAgendamento(dados).subscribe({
      next: () => {
        this.salvando = false;
        this.cancelar();
        form.resetForm(this.formulario);
        this.sucesso = 'Agendamento salvo com sucesso.';
        this.listar();
        this.changeDetectorRef.markForCheck();
      },
      error: (erro) => {
        this.salvando = false;
        this.mostrarErro(erro.error?.message || 'Não foi possível salvar o agendamento.');
      }
    });
  }

  editar(item: Agendamento) {
    this.erro = '';
    this.sucesso = '';
    this.formulario = {
      id: item.id,
      clienteId: item.cliente.id || null,
      macaId: item.maca?.id || null,
      tatuadorId: item.tatuador.id,
      data: item.data,
      hora: item.hora.substring(0, 5),
      servico: item.servico,
      observacao: item.observacao || ''
    };

    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  excluir(item: Agendamento) {
    if (!item.id || !confirm('Excluir este agendamento?')) {
      return;
    }

    this.api.excluirAgendamento(item.id).subscribe({
      next: () => this.listar(),
      error: (erro) => {
        this.salvando = false;
        this.mostrarErro(erro.error?.message || 'Não foi possível excluir o agendamento.');
      }
    });
  }

  cancelar() {
    this.erro = '';
    this.sucesso = '';
    this.formulario = this.novoFormulario();
  }

  private mostrarErro(mensagem: string) {
    this.erro = mensagem;
    window.scrollTo({ top: 0, behavior: 'smooth' });
    this.changeDetectorRef.markForCheck();
  }

  private horarioOcupado(): boolean {
    const { id, data, hora, tatuadorId, macaId } = this.formulario;

    return this.agendamentos.some((item) => {
      const mesmoHorario = item.data === data && item.hora.substring(0, 5) === hora.substring(0, 5);
      const mesmoTatuador = tatuadorId !== null && item.tatuador.id === tatuadorId;
      const mesmaMaca = macaId !== null && item.maca?.id === macaId;

      return item.id !== id && mesmoHorario && (mesmoTatuador || mesmaMaca);
    });
  }
}
