package com.example.carwashee.model;

import java.math.BigDecimal;

public class Servico {
    private int id;
    private String descricao;
    private String tipoServico;
    private String plano;
    private BigDecimal preco;

    public Servico() {}

    public Servico(int id, String descricao, String  tipoServico, String plano, BigDecimal preco) {
        this.id = id;
        this.descricao = descricao;
        this.tipoServico = tipoServico;
        this.plano = plano;
        this.preco = preco;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String  getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String  tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Servico{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", tipoServico=" + tipoServico +
                ", plano=" + plano +
                ", preco=" + preco +
                '}';
    }
}