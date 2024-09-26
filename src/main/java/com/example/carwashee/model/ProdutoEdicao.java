package com.example.carwashee.model;

public class ProdutoEdicao {
    private String tipoProduto;
    private int novaQuantia;

    // Construtor
    public ProdutoEdicao(String tipoProduto, int novaQuantia) {
        this.tipoProduto = tipoProduto;
        this.novaQuantia = novaQuantia;
    }

    // Getters e Setters
    public String getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public int getNovaQuantia() {
        return novaQuantia;
    }

    public void setNovaQuantia(int novaQuantia) {
        this.novaQuantia = novaQuantia;
    }
}
