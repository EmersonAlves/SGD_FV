package com.sgdfv.emerson.sgd_fv.model;

import java.io.Serializable;

/**
 * Created by Emerson on 06/04/2017.
 */

public class Produto implements Serializable {

    private Long idProduto;
    private String descricao;
    private double preco;
    private String unidade;

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

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String toString(){
        return descricao;
    }

}
