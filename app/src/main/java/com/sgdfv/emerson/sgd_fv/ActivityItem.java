package com.sgdfv.emerson.sgd_fv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.sgdfv.emerson.sgd_fv.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ActivityItem extends AppCompatActivity {
    private List<String> listaProdutos;
    private AutoCompleteTextView atDescricao;
    private ListView listaItens;
    private Button btnItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        listaProdutos = new ArrayList<>();

        atDescricao = (AutoCompleteTextView) findViewById(R.id.atDescricao);
        listaItens = (ListView) findViewById(R.id.itens);
        btnItem = (Button) findViewById(R.id.btnItem);

        popularProduto();
        actionButton();
    }

    public void popularProduto(){
        List<Produto> produtos = new ArrayList<>();

        Produto produto1 = new Produto();
        produto1.setIdProduto(1l);
        produto1.setDescricao("Sacola 1");
        produto1.setPreco(2.4);

        Produto produto2 = new Produto();
        produto2.setIdProduto(2l);
        produto2.setDescricao("Sacola 2");
        produto2.setPreco(3.4);

        produtos.add(produto1);
        produtos.add(produto2);
        ArrayAdapter<Produto>  adapter = new ArrayAdapter<Produto>(this,android.R.layout.simple_dropdown_item_1line,produtos);
        atDescricao.setAdapter(adapter);
    }

    public void actionButton(){
        btnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descricao = atDescricao.getText().toString();
                listaProdutos.add(descricao);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listaProdutos);
                listaItens.setAdapter(adapter);
            }
        });
    }

}
