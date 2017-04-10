package com.sgdfv.emerson.sgd_fv.model;

import java.io.Serializable;

/**
 * Created by Emerson on 07/04/2017.
 */

public class Usuario implements Serializable {
    private Long idUsuario;
    private String nome;
    private String Endereco;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public String toString(){
        return nome;
    }
}
