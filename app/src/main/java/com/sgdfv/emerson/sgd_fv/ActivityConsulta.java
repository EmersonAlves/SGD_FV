package com.sgdfv.emerson.sgd_fv;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sgdfv.emerson.sgd_fv.model.ItemOrcamento;
import com.sgdfv.emerson.sgd_fv.model.Orcamento;
import com.sgdfv.emerson.sgd_fv.model.Produto;
import com.sgdfv.emerson.sgd_fv.util.MascaraUtil;

import java.util.ArrayList;
import java.util.List;

public class ActivityConsulta extends AppCompatActivity {
    private ListView listaItens;
    private EditText etTotalItem;
    private Orcamento orcamento;
    private List<ItemOrcamento> itens;

    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        itens = new ArrayList<>();

        orcamento = (Orcamento) getIntent().getSerializableExtra("orcamento");
        etTotalItem = (EditText) findViewById(R.id.etTotal);
        TextView tvNome = (TextView) findViewById(R.id.tvNome);
        listaItens = (ListView) findViewById(R.id.itens);

        tvNome.setText(tvNome.getText().toString()+" "+orcamento.getUsuario().getNome().toUpperCase());
        itens.addAll(orcamento.getListaItens());
        itens();
        etTotalItem.setText(MascaraUtil.setValorCampoMoeda(orcamento.getValorTotalOrcamento()));
        listaItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogItem(position);
            }
        });
    }

    public void itens(){
        ArrayAdapter<ItemOrcamento> adapter = new ArrayAdapter<ItemOrcamento>(this, android.R.layout.simple_list_item_1, itens);
        listaItens.setAdapter(adapter);
    }

    private void dialogItem(final int position) {
        ItemOrcamento item = itens.get(position);
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(item.getProduto().getDescricao());
        //define a mensagem
        builder.setMessage("PREÇO: "+MascaraUtil.setValorCampoMoeda(item.getPrecoUnitario())+"\nQUANTIDADE: "+item.getQuantidade()+"\nVALOR TOTAL: "+MascaraUtil.setValorCampoMoeda(item.getValorTotalItem()));
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
