package com.example.carwashee.model;

import jakarta.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProdutoGasto {
    private int id_Servico;
    private List<Produto> produtosUsados;

    public ProdutoGasto () {
        produtosUsados = new ArrayList<Produto>();
    }

    public ProdutoGasto(int id_Servico) {
        this.id_Servico = id_Servico;
        produtosUsados = new ArrayList<Produto>();
    }

    public int getId_Servico() {
        return id_Servico;
    }

    public void setId_Servico(int id_Servico) {
        this.id_Servico = id_Servico;
    }

    private List<Produto> obterQuantidadeGasta() {
        if (id_Servico == 1) {
            Produto produto = new Produto();
            produto.setTipoProduto(TipoProduto.valueOf("DETERGENTE"));
            produto.setQuantia(2);
            produtosUsados.add(produto);
        } else if (id_Servico == 2) {
            Produto produto = new Produto();
            produto.setTipoProduto(TipoProduto.valueOf("DETERGENTE"));
            produto.setQuantia(2);
            Produto spray = new Produto();
            produto.setTipoProduto(TipoProduto.valueOf("SPRAY"));
            produto.setQuantia(2);
            produtosUsados.add(produto);
            produtosUsados.add(spray);
        } else if (id_Servico == 3) {
            Produto cera = new Produto();
            cera.setTipoProduto(TipoProduto.valueOf("CERA"));
            cera.setQuantia(5);
            produtosUsados.add(cera);
        } else if (id_Servico == 4) {
            Produto produto = new Produto();
            produto.setTipoProduto(TipoProduto.valueOf("DETERGENTE"));
            produto.setQuantia(6);
            Produto spary = new Produto();
            produto.setTipoProduto(TipoProduto.valueOf("SPRAY"));
            produto.setQuantia(6);
            Produto cera = new Produto();
            cera.setTipoProduto(TipoProduto.valueOf("CERA"));
            cera.setQuantia(5);
            produtosUsados.add(produto);
            produtosUsados.add(spary);
            produtosUsados.add(cera);
        } else if (id_Servico == 5) {
            Produto produto = new Produto();
            produto.setTipoProduto(TipoProduto.valueOf("DETERGENTE"));
            produto.setQuantia(2);
            Produto spary = new Produto();
            produto.setTipoProduto(TipoProduto.valueOf("SPRAY"));
            produto.setQuantia(2);
            Produto cera = new Produto();
            cera.setTipoProduto(TipoProduto.valueOf("CERA"));
            cera.setQuantia(1);
            produtosUsados.add(produto);
            produtosUsados.add(spary);
            produtosUsados.add(cera);
        }
        return produtosUsados;
    }

    //por no marcar como concluido

    public void concluirAgendamento() {
    ProdutoGasto produtoGasto = new ProdutoGasto(this.id_Servico);
    List<Produto> produtosUsados = produtoGasto.obterQuantidadeGasta();

    // Para cada produto usado, envie uma requisição de atualização
    for (Produto produto : produtosUsados) {
        try {
            System.out.println("Tipo prod: " + produto.getTipoProduto() + ", Quantia: " + produto.getQuantia());
            // Cria a URL do servlet
            URL url = new URL("http://localhost:8080/CarwashEE_war_exploded/atualizarProduto");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);

            // Prepara os dados do produto no formato JSON
            String jsonInputString = "{ \"tipoProduto\": \"" + produto.getTipoProduto() +
                                      "\", \"valor\": " + (-produto.getQuantia()) + " }";

            // Envia o JSON ao servlet
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Verifica a resposta do servidor
            int responseCode = con.getResponseCode();
            if (responseCode == HttpServletResponse.SC_OK) {
                System.out.println("Produto atualizado com sucesso: " + produto.getTipoProduto());
            } else {
                System.out.println("Erro ao atualizar produto: " + produto.getTipoProduto());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

}
