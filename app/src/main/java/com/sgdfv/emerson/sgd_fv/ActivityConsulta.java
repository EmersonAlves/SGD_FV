package com.sgdfv.emerson.sgd_fv;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sgdfv.emerson.sgd_fv.db.DBManager;
import com.sgdfv.emerson.sgd_fv.model.Endereco;
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

    private Button btnAlterar;
    private DBManager dbManager;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        dbManager = new DBManager(this);

        itens = new ArrayList<>();

        orcamento = (Orcamento) getIntent().getSerializableExtra("orcamento");
        etTotalItem = (EditText) findViewById(R.id.etTotal);
        btnAlterar = (Button) findViewById(R.id.btnAlterar);

        TextView tvNome = (TextView) findViewById(R.id.tvNome);
        TextView tvEndereco = (TextView) findViewById(R.id.tvEndereco);
        listaItens = (ListView) findViewById(R.id.itens);
        Endereco endereco = dbManager.getEndereco(orcamento.getUsuario().getIdUsuario());

        tvNome.setText(tvNome.getText().toString()+" "+orcamento.getUsuario().getNome().toUpperCase());
        String end = tvEndereco.getText().toString()+" "+endereco.getLogradouro()+" "+endereco.getEndereco()+" "+endereco.getNumero();
        tvEndereco.setText(end.toUpperCase());

        itens.addAll(orcamento.getListaItens());
        itens();
        etTotalItem.setText(MascaraUtil.setValorCampoMoeda(orcamento.getValorTotalOrcamento()));
        listaItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogItem(position);
            }
        });

        if(!orcamento.getStatus().equals("NAO ENVIADO")){
            btnAlterar.setVisibility(View.INVISIBLE);
        }

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityConsulta.this,ActivityAlteraItens.class);
                intent.putExtra("orcamento",orcamento);
                startActivity(intent);
                finish();
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
