package com.example.carwashee.model;

public class Produto {
    private int id;
    private TipoProduto tipoProduto;
    private int quantia;

    public Produto() {}

    public Produto(int id, TipoProduto tipoProduto, int quantia) {
        this.id = id;
        this.tipoProduto = tipoProduto;
        this.quantia = quantia;
    }

    public Produto(TipoProduto tipoProduto, int quantia) {
        this.tipoProduto = tipoProduto;
        this.quantia = quantia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public int getQuantia() {
        return quantia;
    }

    public void setQuantia(int quantia) {
        this.quantia = quantia;
    }
}
