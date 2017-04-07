package com.sgdfv.emerson.sgd_fv.model;

/**
 * Created by Emerson on 06/04/2017.
 */

public class Produto {

    private Long idProduto;
    private String descricao;
    private double preco;

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String toString(){
        return descricao;
    }

}
