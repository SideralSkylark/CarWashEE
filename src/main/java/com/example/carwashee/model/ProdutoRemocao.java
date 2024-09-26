package com.example.carwashee.model;

public class ProdutoRemocao {
    private String tipoProduto;

    // Construtor
    public ProdutoRemocao(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    // Getters e Setters
    public String getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }
}
