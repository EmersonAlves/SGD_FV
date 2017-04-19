package com.sgdfv.emerson.sgd_fv.model;

/**
 * Created by Emerson on 18/04/2017.
 */
public class ItemListView {
    private int id;
    private String texto;
    private int iconeRid;

    public ItemListView() {}

    public ItemListView(int id,String texto, int iconeRid) {
        this.texto = texto;
        this.iconeRid = iconeRid;
        this.id = id;
    }

    public int getIconeRid() {
        return iconeRid;
    }

    public void setIconeRid(int iconeRid) {
        this.iconeRid = iconeRid;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}