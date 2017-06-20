package com.sgdfv.emerson.sgd_fv.model;

/**
 * Created by Emerson on 14/06/2017.
 */

public class Fantasia extends Usuario {
    private String nomeFantasia;
    private Long idUsuario;
    private String nome;
    private String tipo;
    private String cpfcnpj;
    private  String fone;
    private Endereco endereco;
    private String url;
    private Long idServidor;

    public Fantasia(Usuario usuario) {
        super();
        idUsuario = usuario.getIdUsuario();
        nome = usuario.getNome();
        nomeFantasia = usuario.getNomeFantasia();
        tipo = usuario.getTipo();
        cpfcnpj = usuario.getCpfcnpj();
        fone = usuario.getFone();
        endereco = usuario.getEndereco();
    }

    @Override
    public String getNomeFantasia() {
        return nomeFantasia;
    }

    @Override
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    @Override
    public Long getIdUsuario() {
        return idUsuario;
    }

    @Override
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getTipo() {
        return tipo;
    }

    @Override
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String getCpfcnpj() {
        return cpfcnpj;
    }

    @Override
    public void setCpfcnpj(String cpfcnpj) {
        this.cpfcnpj = cpfcnpj;
    }

    @Override
    public String getFone() {
        return fone;
    }

    @Override
    public void setFone(String fone) {
        this.fone = fone;
    }

    @Override
    public Endereco getEndereco() {
        return endereco;
    }

    @Override
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Long getIdServidor() {
        return idServidor;
    }

    @Override
    public void setIdServidor(Long idServidor) {
        this.idServidor = idServidor;
    }

    public String toString(){
        return getNomeFantasia();
    }
}
