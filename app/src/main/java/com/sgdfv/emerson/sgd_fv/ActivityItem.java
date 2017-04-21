package com.sgdfv.emerson.sgd_fv;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sgdfv.emerson.sgd_fv.db.DBManager;
import com.sgdfv.emerson.sgd_fv.model.EnviarOrcamento;
import com.sgdfv.emerson.sgd_fv.model.ItemOrcamento;
import com.sgdfv.emerson.sgd_fv.model.Orcamento;
import com.sgdfv.emerson.sgd_fv.model.Produto;
import com.sgdfv.emerson.sgd_fv.model.UrlConnection;
import com.sgdfv.emerson.sgd_fv.util.MascaraUtil;
import com.sgdfv.emerson.sgd_fv.util.Utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
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

    private DBManager dbManager;
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

        dbManager = new DBManager(this);

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
        produtos.addAll(dbManager.getListaTodosProdutos());
        if(produtos.size() > 0) {
            ArrayAdapter<Produto> adapter = new ArrayAdapter<Produto>(this, android.R.layout.simple_dropdown_item_1line, produtos);
            atDescricao.setAdapter(adapter);
            atDescricao.setThreshold(1);
        }
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
                finalizarOrcamento();
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

    public void finalizarOrcamento(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Finalizar Orcamento");
        builder.setMessage("Deseja finalizar orcamento ?");
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                orcamento.setListaItens(itens);
                orcamento.setValorTotalOrcamento(MascaraUtil.recuperarValorCampoMoeda(etTotal.getText().toString()));
                orcamento.setStatus("NAO ENVIADO");
                dbManager.inserirOrcamento(orcamento);
                alerta.dismiss();
                orcamento = dbManager.getOrcamento();
                enviarOrcamento(orcamento);
            }
        });
        builder.setNegativeButton("NAO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
            }
        });
        alerta = builder.create();
        alerta.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "ORCAMENTO CANCELADO", Toast.LENGTH_SHORT).show();
    }

    class OrcamentoAsyncTask extends AsyncTask<EnviarOrcamento, Void, Orcamento> {
        private Dialog dialog;
        @Override
        protected Orcamento doInBackground(EnviarOrcamento... params) {
            String urlString = params[0].getUrl();
            Orcamento orcamento = params[0].getOrcamento();
            try {
                HttpContext localContext = new BasicHttpContext();
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(urlString);
                post.setHeader("Content-type", "application/json");
                //post.setHeader("Authorization",token);
                //post.setHeader("Cookie","ASP.NET_SessionId="+sessao+"; path=/; HttpOnly");
                JSONObject obj = new JSONObject();
                JSONArray ja = new JSONArray();
                obj.put("idusuario", orcamento.getUsuario().getIdUsuario());
                obj.put("idvendedor",orcamento.getVendedor().getIdUsuario());
                obj.put("total",orcamento.getValorTotalOrcamento());
                for(ItemOrcamento item: orcamento.getListaItens()){
                    JSONObject aux =  new JSONObject();
                    aux.put("idproduto",item.getProduto().getIdProduto());
                    aux.put("preco",item.getPrecoUnitario());
                    aux.put("qtde",item.getQuantidade());
                    aux.put("total",item.getValorTotalItem());
                    ja.put(aux);
                }
                obj.put("item",ja);

                StringEntity se = new StringEntity(obj.toString());
                post.setEntity(se);
                HttpResponse response = client.execute(post, localContext);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String json = getStringFromInputStream(instream);
                    instream.close();
                    instream.close();
                    orcamento = getOrcamento(orcamento,json);
                    return orcamento;
                }
            } catch (Exception e) {
                Log.e("Error", "Falha ao acessar Web service", e);
            }
            return orcamento;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ActivityItem.this, "Aguarde",
                    "Enviado Orcamentos, Por Favor Aguarde...");
        }
        @Override
        protected void onPostExecute(Orcamento orcamento) {
            super.onPostExecute(orcamento);
            dialog.dismiss();
            if(orcamento.getIdServidor() != null) {
                orcamento.setStatus("ENVIADO");
                dbManager.atualizarOrcamento(orcamento);
                Toast.makeText(getApplicationContext(),"ORCAMENTO ENVIADO",Toast.LENGTH_LONG).show();
                finish();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ActivityItem.this).setTitle("Atenção")
                        .setMessage("Não foi possivel acessar o servidor...")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Orcamento Finalizado", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                builder.create().show();

            }
        }
        private Orcamento getOrcamento(Orcamento orcamento,String jsonString) {
            List<Orcamento> orcamentos = new ArrayList<>();

            try {
                JSONArray locationLists = new JSONArray(jsonString);
                JSONObject orcJson;
                for (int i = 0; i < locationLists.length(); i++) {
                    orcJson = new JSONObject(locationLists.getString(i));

                    Log.i("TESTE", "id=" + orcJson.getLong("idorcamento"));

                    orcamento.setIdServidor(orcJson.getLong("idorcamento"));
                    orcamentos.add(orcamento);
                }
            } catch (JSONException e) {
                Log.e("Error", "Erro no parsing do JSON", e);
            }
            return orcamentos.get(0);
        }
    }
    public static String getStringFromInputStream(InputStream stream) throws IOException {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, "UTF8");
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }
    public void enviarOrcamento(Orcamento orcamento){
        EnviarOrcamento enviarOrcamento = new EnviarOrcamento();
        enviarOrcamento.setUrl(UrlConnection.URL+"orcamento.php");
        enviarOrcamento.setOrcamento(orcamento);
        new OrcamentoAsyncTask().execute(enviarOrcamento);
    }
}
