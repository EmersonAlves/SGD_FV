package com.sgdfv.emerson.sgd_fv;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sgdfv.emerson.sgd_fv.model.ItemOrcamento;
import com.sgdfv.emerson.sgd_fv.model.Orcamento;
import com.sgdfv.emerson.sgd_fv.model.Produto;
import com.sgdfv.emerson.sgd_fv.util.MascaraUtil;
import com.sgdfv.emerson.sgd_fv.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class ActivityItem extends AppCompatActivity {
    private AutoCompleteTextView atDescricao;
    private ListView listaItens;
    private Button btnItem;
    private Button btnFinalizar;
    private EditText etPreco;
    private EditText etUnidade;
    private EditText etTotal;
    private EditText etQuantidade;
    private EditText etTotalItem;

    private AlertDialog alerta;

    private List<ItemOrcamento> itens;
    private Orcamento orcamento;
    private Produto produtoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        orcamento = (Orcamento) getIntent().getSerializableExtra("orcamento");
        itens = new ArrayList<>();

        atDescricao = (AutoCompleteTextView) findViewById(R.id.atDescricao);
        listaItens = (ListView) findViewById(R.id.itens);
        btnItem = (Button) findViewById(R.id.btnItem);
        btnFinalizar = (Button) findViewById(R.id.btnFinalizarOrcamento);
        etPreco = (EditText) findViewById(R.id.etPreco);
        etUnidade = (EditText) findViewById(R.id.etUnidade);
        etTotal = (EditText) findViewById(R.id.etTotal);
        etQuantidade = (EditText) findViewById(R.id.etQuantidade);
        etTotalItem = (EditText) findViewById(R.id.etTotalItem);

        TextView tvNome = (TextView) findViewById(R.id.tvNome);
        tvNome.setText(tvNome.getText().toString()+" "+orcamento.getUsuario().getNome().toUpperCase());

        etPreco.setText("0.00");
        etTotal.setText("0.00");
        etTotalItem.setText("0.00");
        etUnidade.setText("");

        popularProduto();
        atDescricao.requestFocus();
        actionEvent();
    }

    public void popularProduto() {
        List<Produto> produtos = new ArrayList<>();

        Produto produto1 = new Produto();
        produto1.setIdProduto(1l);
        produto1.setDescricao("Sacola 1");
        produto1.setPreco(2.4);
        produto1.setUnidade("UN");

        Produto produto2 = new Produto();
        produto2.setIdProduto(2l);
        produto2.setDescricao("Sacola 2");
        produto2.setPreco(3.4);
        produto2.setUnidade("UN");

        produtos.add(produto1);
        produtos.add(produto2);
        ArrayAdapter<Produto> adapter = new ArrayAdapter<Produto>(this, android.R.layout.simple_dropdown_item_1line, produtos);
        atDescricao.setAdapter(adapter);
        atDescricao.setThreshold(1);

    }

    public void actionEvent() {
        btnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(produtoSelecionado == null){
                    Toast.makeText(getApplicationContext(),"Selecione o Produto",Toast.LENGTH_LONG).show();
                    atDescricao.requestFocus();
                }else if(MascaraUtil.recuperarValorCampoMoeda(etPreco.getText().toString()) <= 0) {
                    Toast.makeText(getApplicationContext(),"Informe o Preço do Produto",Toast.LENGTH_LONG).show();
                    etPreco.requestFocus();
                }else if(etQuantidade.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Informe a Quantidade do Produto",Toast.LENGTH_LONG).show();
                    etQuantidade.requestFocus();
                }else {
                    if(itens.size() > 0) {
                        for (ItemOrcamento item : itens) {
                            if(item.getProduto().equals(produtoSelecionado)){
                                item.setIdItemOrcamento(produtoSelecionado.getIdProduto());
                                item.setPrecoUnitario(MascaraUtil.recuperarValorCampoMoeda(etPreco.getText().toString()));
                                item.setQuantidade(Double.parseDouble(etQuantidade.getText().toString()));
                                item.setValorTotalItem((item.getPrecoUnitario() * item.getQuantidade()));
                                item.setProduto(produtoSelecionado);
                                atualizarItens();
                                return;
                            }
                        }
                    }
                    ItemOrcamento item = new ItemOrcamento();
                    item.setIdItemOrcamento(produtoSelecionado.getIdProduto());
                    item.setPrecoUnitario(MascaraUtil.recuperarValorCampoMoeda(etPreco.getText().toString()));
                    item.setQuantidade(Double.parseDouble(etQuantidade.getText().toString()));
                    item.setValorTotalItem((item.getPrecoUnitario() * item.getQuantidade()));
                    item.setProduto(produtoSelecionado);
                    itens.add(item);
                    atualizarItens();
                    if(listaItens.getCount() > 1){
                        Utility.setListViewHeightBasedOnChildren(listaItens);
                    }
                }
            }
        });
        listaItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogItem(position);
            }
        });
        atDescricao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                produtoSelecionado = (Produto) parent.getItemAtPosition(position);
                etUnidade.setText(produtoSelecionado.getUnidade());
                etPreco.setText(MascaraUtil.setValorCampoMoeda(produtoSelecionado.getPreco()));
                etPreco.requestFocus();
            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(etQuantidade.getText().toString().isEmpty()) && MascaraUtil.recuperarValorCampoMoeda(etPreco.getText().toString()) > 0){
                    try{
                        double total = Double.parseDouble(etQuantidade.getText().toString()) * MascaraUtil.recuperarValorCampoMoeda(etPreco.getText().toString());
                        etTotalItem.setText(MascaraUtil.setValorCampoMoeda(total));
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Valor de Quantidade invalido",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etPreco.addTextChangedListener(textWatcher);
        etQuantidade.addTextChangedListener(textWatcher);

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"ORCAMENTO FINALIZADO",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void limparCampos() {
        atDescricao.setText("");
        etQuantidade.setText("");
        etPreco.setText("0.00");
        etTotalItem.setText("0.00");
        etUnidade.setText("");
        produtoSelecionado = null;
    }

    public void atualizarItens(){
        ArrayAdapter<ItemOrcamento> adapter = new ArrayAdapter<ItemOrcamento>(this, android.R.layout.simple_list_item_1, itens);
        listaItens.setAdapter(adapter);
        if(itens.size() > 0) {
            btnFinalizar.setVisibility(View.VISIBLE);
        }else{
            btnFinalizar.setVisibility(View.INVISIBLE);
        }double total = 0.0;
        for (ItemOrcamento itemOrc : itens) {
            total += itemOrc.getValorTotalItem();
        }
        etTotal.setText(MascaraUtil.setValorCampoMoeda(total));
        limparCampos();
        atDescricao.requestFocus();
    }

    private void dialogItem(final int position) {
        ItemOrcamento item = itens.get(position);
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(item.getProduto().getDescricao());
        //define a mensagem
        builder.setMessage("PREÇO: "+MascaraUtil.setValorCampoMoeda(item.getPrecoUnitario())+"\nQUANTIDADE: "+item.getQuantidade()+"\nVALOR TOTAL: "+MascaraUtil.setValorCampoMoeda(item.getValorTotalItem()));
        //define um botão como positivo
        builder.setPositiveButton("ALTERAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                alerta.dismiss();
                mostrarProduto(itens.get(position));
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("REMOVER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                itens.remove(position);
                alerta.dismiss();
                atualizarItens();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    public void mostrarProduto(ItemOrcamento item) {
        atDescricao.setText(item.getProduto().getDescricao());
        etQuantidade.setText(String.valueOf(item.getQuantidade()));
        etPreco.setText(MascaraUtil.setValorCampoMoeda(item.getPrecoUnitario()));
        etUnidade.setText(item.getProduto().getUnidade());
        produtoSelecionado = item.getProduto();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "ORCAMENTO CANCELADO", Toast.LENGTH_SHORT).show();
    }
}
