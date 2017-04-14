package com.sgdfv.emerson.sgd_fv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sgdfv.emerson.sgd_fv.db.DBManager;
import com.sgdfv.emerson.sgd_fv.model.Orcamento;

import java.util.ArrayList;
import java.util.List;

public class ActivityHistorico extends AppCompatActivity {
    private ListView lvOrcamentos;
    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        dbManager = new DBManager(this);

        lvOrcamentos = (ListView) findViewById(R.id.lvOrcamentos);
        preecherLista();
    }
    public void preecherLista(){
        List<Orcamento> orcamentos = new ArrayList<>();
        orcamentos = dbManager.getListaOrcamentos();
        if(orcamentos.size() > 0){
            ArrayAdapter<Orcamento> adapter = new ArrayAdapter<Orcamento>(this, android.R.layout.simple_list_item_1, orcamentos);
            lvOrcamentos.setAdapter(adapter);
        }
    }
}
