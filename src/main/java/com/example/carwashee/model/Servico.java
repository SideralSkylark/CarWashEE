package com.example.carwashee.model;

import java.math.BigDecimal;

public class Servico {
    private int id;
    private String descricao;
    private TipoServico tipoServico;
    private Plano plano;
    private BigDecimal preco;

    public Servico() {}

    public Servico(int id, String descricao, String tipoServico, String plano, BigDecimal preco) {
        this.id = id;
        this.descricao = descricao;
        this.tipoServico = TipoServico.valueOf(tipoServico);
        this.plano = Plano.valueOf(plano);
        this.preco = preco;
    }

    // Getters and Setters

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
