package com.sgdfv.emerson.sgd_fv.model;

/**
 * Created by Emerson on 20/04/2017.
 */

public class Cidade {

    private String idCidade;
    private String codigoestado;
    private String nome;

    public String getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(String idCidade) {
        this.idCidade = idCidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoestado() {
        return codigoestado;
    }

    public void setCodigoestado(String codigoestado) {
        this.codigoestado = codigoestado;
    }

    public String toString(){
        return nome;
    }
}
