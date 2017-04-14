package com.sgdfv.emerson.sgd_fv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        values.put("endereco",usuario.getEndereco());
        values.put("tipo",usuario.getTipo());
        db.insert("usuario",null,values);

    }
    public List<Usuario> getListaUsuarios(){
        String sql = "SELECT * FROM usuario";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        List<Usuario> usuarios = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()){
            while (cursor.moveToNext()){
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(cursor.getLong(0));
                usuario.setNome(cursor.getString(1));
                usuario.setEndereco(cursor.getString(2));
                usuario.setTipo(cursor.getString(3));
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
            usuario.setEndereco(cursor.getString(2));
            usuario.setTipo(cursor.getString(3));
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
    public List<Produto> getListaTodosProdutos(){
        String sql = "SELECT * FROM produto";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        List<Produto> produtos = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()){
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
    public void inserirOrcamento(Orcamento orcamento){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idusuario",orcamento.getUsuario().getIdUsuario());
        values.put("idvendedor",orcamento.getVendedor().getIdUsuario());
        values.put("valorOrcamento",orcamento.getValorTotalOrcamento());
        db.insert("orcamento",null,values);
        incluirItensOrcamento(orcamento.getListaItens());
    }
    public List<Orcamento> getListaOrcamentos(){
        String sql = "SELECT * FROM orcamento";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        List<Orcamento> orcamentos = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()){
            while (cursor.moveToNext()){
                Orcamento orcamento = new Orcamento();
                orcamento.setIdOrcamento(cursor.getLong(0));
                orcamento.setUsuario(getUsuario(cursor.getLong(1)));
                orcamento.setVendedor(getUsuario(cursor.getLong(2)));
                orcamento.setValorTotalOrcamento(cursor.getDouble(3));
                orcamento.setListaItens(getItensOrcamento(cursor.getLong(0)));
                orcamentos.add(orcamento);
            }
        }
        return orcamentos;
    }
    public void incluirItensOrcamento(List<ItemOrcamento> itens){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (ItemOrcamento item : itens){
            values.put("idorcamento",item.getOrcamento());
            values.put("idproduto",item.getProduto().getIdProduto());
            values.put("preco",item.getPrecoUnitario());
            values.put("quantidade",item.getQuantidade());
            values.put("valorTotal",item.getValorTotalItem());
            db.insert("itemorcamento",null,values);
        }
    }
    public List<ItemOrcamento> getItensOrcamento(Long id){
        String sql = "SELECT * FROM itemorcamento where idorcamento= '"+id+"'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        List<ItemOrcamento> itens = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()){
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
