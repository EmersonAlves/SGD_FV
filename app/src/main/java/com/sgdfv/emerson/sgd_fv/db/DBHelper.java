package com.sgdfv.emerson.sgd_fv.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Emerson on 10/04/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "SGD_FV";
    private static int DB_VERSION = 1;

    private static String TABLE_USUARIO =
            "CREATE TABLE usuario(" +
                    "idusuario INTEGER PRIMARY KEY," +
                    "nome TEXT," +
                    "tipo TEXT," +
                    "nomefantasia TEXT)";
    private static String TABLE_ENDERECO =
            "CREATE TABLE endereco(" +
                    "idendereco INTEGER PRIMARY KEY," +
                    "logradouro TEXT," +
                    "endereco TEXT," +
                    "numero TEXT," +
                    "idusuario INTEGER)";
    private static String TABLE_CIDADE =
            "CREATE TABLE cidade(" +
                    "idcidade INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "codcidade TEXT," +
                    "codigoestado TEXT," +
                    "nome TEXT)";
    private static String TABLE_PRODUTO =
            "CREATE TABLE produto(" +
                    "idproduto INTEGER PRIMARY KEY," +
                    "descricao TEXT," +
                    "preco FLOAT," +
                    "unidade TEXT)";

    private static String TABLE_ORCAMENTO =
            "CREATE TABLE orcamento(" +
                    "idorcamento INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idusuario INTEGER," +
                    "idvendedor INTEGER," +
                    "valorOrcamento FLOAT," +
                    "status TEXT)";
    private static String TABLE_ITEMORCAMENTO = "" +
            "CREATE TABLE itemorcamento(" +
            "iditem INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idorcamento INTEGER," +
            "idproduto INTEGER," +
            "preco FLOAT," +
            "quantidade FLOAT," +
            "valorTotal FLOAT)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USUARIO);
        db.execSQL(TABLE_ENDERECO);
        db.execSQL(TABLE_CIDADE);
        db.execSQL(TABLE_PRODUTO);
        db.execSQL(TABLE_ORCAMENTO);
        db.execSQL(TABLE_ITEMORCAMENTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
