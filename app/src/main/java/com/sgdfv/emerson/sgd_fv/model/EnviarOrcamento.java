package com.sgdfv.emerson.sgd_fv.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Emerson on 19/04/2017.
 */

public class EnviarOrcamento implements Serializable {
    private String url;
    private Orcamento orcamento;
    private List<Orcamento> lista;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Orcamento> getLista() {
        return lista;
    }

    public void setLista(List<Orcamento> lista) {
        this.lista = lista;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }
}
