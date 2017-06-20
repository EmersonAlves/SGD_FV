package com.sgdfv.emerson.sgd_fv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sgdfv.emerson.sgd_fv.model.Cidade;
import com.sgdfv.emerson.sgd_fv.model.Endereco;
import com.sgdfv.emerson.sgd_fv.model.Ip;
import com.sgdfv.emerson.sgd_fv.model.ItemOrcamento;
import com.sgdfv.emerson.sgd_fv.model.Orcamento;
import com.sgdfv.emerson.sgd_fv.model.Produto;
import com.sgdfv.emerson.sgd_fv.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emerson on 10/04/2017.
 */

public class DBManager {
    private static DBHelper dbHelper = null;

    public DBManager(Context context){
        if(dbHelper == null){
            dbHelper = new DBHelper(context);
        }
    }

    public void inserirUsuario(Usuario usuario){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idusuario",usuario.getIdUsuario());
        values.put("nome",usuario.getNome());
        values.put("tipo",usuario.getTipo());
        values.put("nomefantasia",usuario.getNomeFantasia());
        db.insert("usuario",null,values);
    }
    public void inserirCidade(Cidade cidade){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codcidade",cidade.getIdCidade());
        values.put("codigoestado",cidade.getCodigoestado());
        values.put("nome",cidade.getNome());
        db.insert("cidade",null,values);
    }
    public void inserirEndereco(Endereco endereco){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idendereco",endereco.getIdEndereco());
        values.put("logradouro",endereco.getLogradouro());
        values.put("endereco",endereco.getEndereco());
        values.put("numero",endereco.getNumero());
        values.put("idusuario",endereco.getIdusuario());
        db.insert("endereco",null,values);
    }
    public List<Usuario> getListaUsuarios(){
        String sql = "SELECT * FROM usuario ORDER BY nome";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        List<Usuario> usuarios = new ArrayList<>();

        if(cursor != null){
            while (cursor.moveToNext()){
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(cursor.getLong(0));
                usuario.setNome(cursor.getString(1));
                usuario.setTipo(cursor.getString(2));
                usuario.setNomeFantasia(cursor.getString(3));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
    public Usuario getUsuario(Long id){
        String sql = "SELECT * FROM usuario WHERE idusuario = '"+id+"'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        Usuario usuario = new Usuario();
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            usuario.setIdUsuario(cursor.getLong(0));
            usuario.setNome(cursor.getString(1));
            usuario.setTipo(cursor.getString(2));
        }
        return usuario;
    }
    public Usuario getUltimoUsuario(){
        String sql = "SELECT * FROM usuario ORDER BY idusuario DESC LIMIT 1";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        Usuario usuario = new Usuario();
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            usuario.setIdUsuario(cursor.getLong(0));
            usuario.setNome(cursor.getString(1));
            usuario.setTipo(cursor.getString(2));
        }
        return usuario;
    }

    public void inserirProduto(Produto produto){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idproduto",produto.getIdProduto());
        values.put("descricao",produto.getDescricao());
        values.put("preco",produto.getPreco());
        values.put("unidade",produto.getUnidade());
        db.insert("produto",null,values);
    }
    public void inserirIp(Ip ip){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idip",ip.getIdIp());
        values.put("ip",ip.getIp());
        db.insert("tip",null,values);
    }

    public List<Produto> getListaTodosProdutos(){
        String sql = "SELECT * FROM produto ORDER BY descricao";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        List<Produto> produtos = new ArrayList<>();

        if(cursor != null){
            while (cursor.moveToNext()){
                Produto produto = new Produto();
                produto.setIdProduto(cursor.getLong(0));
                produto.setDescricao(cursor.getString(1));
                produto.setPreco(cursor.getDouble(2));
                produto.setUnidade(cursor.getString(3));
                produtos.add(produto);
            }
        }
        return produtos;
    }

    public List<Cidade> getListaTodosCidades(){
        String sql = "SELECT * FROM cidade ORDER BY nome";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        List<Cidade> cidades = new ArrayList<>();

        if(cursor != null){
            while (cursor.moveToNext()){
                Cidade cidade = new Cidade();
                cidade.setIdCidade(cursor.getString(1));
                cidade.setCodigoestado(cursor.getString(2));
                cidade.setNome(cursor.getString(3));
                cidades.add(cidade);
            }
        }
        return cidades;
    }
    public List<Ip> getListaIp(){
        String sql = "SELECT * FROM tip ORDER BY idip";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        List<Ip> ips = new ArrayList<>();

        if(cursor != null){
            while (cursor.moveToNext()){
                Ip ip = new Ip();

                ip.setIdIp(cursor.getLong(0));
                ip.setIp(cursor.getString(1));
                ips.add(ip);
            }
        }
        return ips;
    }

    public void deleta(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.rawQuery("DELETE FROM usuario",null);
        db.rawQuery("DELETE FROM produto",null);
        db.rawQuery("DELETE FROM endereco",null);
    }

    public Produto getProduto(Long id){
        String sql = "SELECT * FROM produto where idproduto = '"+id+"'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        Produto produto = new Produto();

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            produto.setIdProduto(cursor.getLong(0));
            produto.setDescricao(cursor.getString(1));
            produto.setPreco(cursor.getDouble(2));
            produto.setUnidade(cursor.getString(3));
        }
        return produto;
    }
    public Produto getUlitmoProduto(){
        String sql = "SELECT * FROM produto ORDER BY idproduto DESC LIMIT 1";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        Produto produto = new Produto();

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            produto.setIdProduto(cursor.getLong(0));
            produto.setDescricao(cursor.getString(1));
            produto.setPreco(cursor.getDouble(2));
            produto.setUnidade(cursor.getString(3));
        }
        return produto;
    }
    public Endereco getUlitmoEndereco(){
        String sql = "SELECT * FROM endereco ORDER BY idendereco DESC LIMIT 1";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        Endereco endereco = new Endereco();

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            endereco.setIdEndereco(cursor.getLong(0));
            endereco.setLogradouro(cursor.getString(1));
            endereco.setEndereco(cursor.getString(2));
            endereco.setNumero(cursor.getString(3));
            endereco.setIdusuario(cursor.getLong(4));
        }
        return endereco;
    }
    public Orcamento getOrcamento(){
        String sql = "SELECT * FROM orcamento ORDER BY idorcamento DESC LIMIT 1";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        Orcamento orcamento = new Orcamento();
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            orcamento.setListaItens(getItensOrcamento(cursor.getLong(0)));
            orcamento.setIdOrcamento(cursor.getLong(0));
            orcamento.setUsuario(getUsuario(cursor.getLong(1)));
            orcamento.setVendedor(getUsuario(cursor.getLong(2)));
            orcamento.setValorTotalOrcamento(cursor.getDouble(3));
            orcamento.setStatus(cursor.getString(4));
            orcamento.setListaItens(getItensOrcamento(cursor.getLong(0)));
        }
        return orcamento;
    }
    public int getUlitmoOrcamento(){
        String sql = "SELECT * FROM orcamento ORDER BY idorcamento DESC LIMIT 1";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        int id = 0;

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }
        return id;
    }
    public void inserirOrcamento(Orcamento orcamento){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idusuario",orcamento.getUsuario().getIdUsuario());
        values.put("idvendedor",orcamento.getVendedor().getIdUsuario());
        values.put("valorOrcamento",orcamento.getValorTotalOrcamento());
        values.put("status",orcamento.getStatus());
        db.insert("orcamento",null,values);
        incluirItensOrcamento(orcamento.getListaItens());
    }
    public void atualizarOrcamento(Orcamento orcamento){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idusuario",orcamento.getUsuario().getIdUsuario());
        values.put("idvendedor",orcamento.getVendedor().getIdUsuario());
        values.put("valorOrcamento",orcamento.getValorTotalOrcamento());
        values.put("status",orcamento.getStatus());
        db.update("orcamento",values,"idorcamento="+orcamento.getIdOrcamento(),null);
        deleteItem(orcamento);
    }
    public void atualizarIp(Ip ip){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ip",ip.getIp());
        db.update("tip",values,"idip="+ip.getIdIp(),null);
    }
    public List<Orcamento> getListaOrcamentos(){
        String sql = "SELECT * FROM orcamento ORDER BY idorcamento DESC LIMIT 30";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        List<Orcamento> orcamentos = new ArrayList<>();

        if(cursor != null){
            while (cursor.moveToNext()){
                Orcamento orcamento = new Orcamento();
                orcamento.setListaItens(getItensOrcamento(cursor.getLong(0)));
                orcamento.setIdOrcamento(cursor.getLong(0));
                orcamento.setUsuario(getUsuario(cursor.getLong(1)));
                orcamento.setVendedor(getUsuario(cursor.getLong(2)));
                orcamento.setValorTotalOrcamento(cursor.getDouble(3));
                orcamento.setStatus(cursor.getString(4));
                orcamento.setListaItens(getItensOrcamento(cursor.getLong(0)));
                orcamentos.add(orcamento);
            }
        }
        return orcamentos;
    }
    public void deleteItem(Orcamento orcamento){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("itemorcamento","idorcamento="+orcamento.getIdOrcamento(),null);
        atualizaItensOrcamento(orcamento.getListaItens(),orcamento);
    }
    public void atualizaItensOrcamento(List<ItemOrcamento> itens,Orcamento orcamento){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (ItemOrcamento item : itens){
            values.put("idorcamento",orcamento.getIdOrcamento());
            values.put("idproduto",item.getProduto().getIdProduto());
            values.put("preco",item.getPrecoUnitario());
            values.put("quantidade",item.getQuantidade());
            values.put("valorTotal",item.getValorTotalItem());
            db.insert("itemorcamento",null,values);
        }
    }

    public void incluirItensOrcamento(List<ItemOrcamento> itens){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (ItemOrcamento item : itens){
            values.put("idorcamento",getUlitmoOrcamento());
            values.put("idproduto",item.getProduto().getIdProduto());
            values.put("preco",item.getPrecoUnitario());
            values.put("quantidade",item.getQuantidade());
            values.put("valorTotal",item.getValorTotalItem());
            db.insert("itemorcamento",null,values);
        }
    }
    public Endereco getEndereco(Long id){
        String sql = "SELECT * FROM endereco WHERE idusuario= '"+id+"'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        Endereco endereco = new Endereco();
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            endereco.setIdEndereco(cursor.getLong(0));
            endereco.setLogradouro(cursor.getString(1));
            endereco.setEndereco(cursor.getString(2));
            endereco.setNumero(cursor.getString(3));
            endereco.setIdusuario(cursor.getLong(4));
        }
        return endereco;
    }
    public List<ItemOrcamento> getItensOrcamento(Long id){
        String sql = "SELECT * FROM itemorcamento where idorcamento= '"+id+"'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        List<ItemOrcamento> itens = new ArrayList<>();

        if(cursor != null){
            while (cursor.moveToNext()){
                ItemOrcamento item = new ItemOrcamento();
                item.setIdItemOrcamento(cursor.getLong(0));
                item.setOrcamento(cursor.getLong(1));
                item.setProduto(getProduto(cursor.getLong(2)));
                item.setPrecoUnitario(cursor.getDouble(3));
                item.setQuantidade(cursor.getDouble(4));
                item.setValorTotalItem(cursor.getDouble(5));
                itens.add(item);
            }
        }
        return itens;
    }
}
