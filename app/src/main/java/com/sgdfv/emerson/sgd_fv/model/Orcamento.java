package com.sgdfv.emerson.sgd_fv.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Emerson on 09/04/2017.
 */

public class Orcamento implements Serializable{

    private Long idOrcamento;
    private Usuario usuario;
    private Usuario vendedor;
    private Date dtEmissao;
    private double valorTotalOrcamento;
    private List<ItemOrcamento> listaItens;

    public Long getIdOrcamento() {
        return idOrcamento;
    }

    public void setIdOrcamento(Long idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public double getValorTotalOrcamento() {
        return valorTotalOrcamento;
    }

    public void setValorTotalOrcamento(double valorTotalOrcamento) {
        this.valorTotalOrcamento = valorTotalOrcamento;
    }

    public List<ItemOrcamento> getListaItens() {
        return listaItens;
    }

    public void setListaItens(List<ItemOrcamento> listaItens) {
        this.listaItens = listaItens;
    }
}
