package com.sgdfv.emerson.sgd_fv.model;

/**
 * Created by Emerson on 09/04/2017.
 */

public class ItemOrcamento {

    private Long idItemOrcamento;
    private Long orcamento;
    private Produto produto;

    private double precoUnitario;
    private double quantidade;
    private double valorTotalItem;

    public Long getIdItemOrcamento() {
        return idItemOrcamento;
    }

    public void setIdItemOrcamento(Long idItemOrcamento) {
        this.idItemOrcamento = idItemOrcamento;
    }

    public Long getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Long orcamento) {
        this.orcamento = orcamento;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorTotalItem() {
        return valorTotalItem;
    }

    public void setValorTotalItem(double valorTotalItem) {
        this.valorTotalItem = valorTotalItem;
    }

    public String toString(){
        return produto.getDescricao();
    }
}
