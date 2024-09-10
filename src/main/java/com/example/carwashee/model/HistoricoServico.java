package com.example.carwashee.model;

import java.util.Date;

public class HistoricoServico {
    private int id;
    private Usuario usuario;
    private Servico servico;
    private Date data;
    private String detalhes;

    public HistoricoServico(int id, Usuario usuario, Servico servico, Date data, String detalhes) {
        this.id = id;
        this.usuario = usuario;
        this.servico = servico;
        this.data = data;
        this.detalhes = detalhes;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }
}
