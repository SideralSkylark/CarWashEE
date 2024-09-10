package com.example.carwashee.model;

import java.time.LocalDate;

public class Agendamento {
    private int id;
    private int usuarioId;
    private int servicoId;
    private LocalDate data;
    private String descricao;
    private TipoServico tipoServico;
    private Plano plano;
    private StatusAgendamento status;
    private Servico servico; // Opcional: para obter detalhes do serviço

    public Agendamento() {}

    public Agendamento(int id, int usuarioId, int servicoId, LocalDate data, String descricao, TipoServico tipoServico, Plano plano, String status) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.servicoId = servicoId;
        this.data = data;
        this.descricao = descricao;
        this.tipoServico = tipoServico;
        this.plano = plano;
        this.status = StatusAgendamento.valueOf(status);
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getServicoId() {
        return servicoId;
    }

    public void setServicoId(int servicoId) {
        this.servicoId = servicoId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    @Override
    public String toString() {
        return "Agendamento{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", servicoId=" + servicoId +
                ", data=" + data +
                ", descricao='" + descricao + '\'' +
                ", tipoServico=" + tipoServico +
                ", plano=" + plano +
                ", status=" + status +
                ", servico=" + servico +
                '}';
    }
}
