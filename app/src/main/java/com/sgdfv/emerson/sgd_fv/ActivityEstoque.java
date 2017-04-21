package com.sgdfv.emerson.sgd_fv;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ListView;

import com.sgdfv.emerson.sgd_fv.db.DBManager;
import com.sgdfv.emerson.sgd_fv.model.ItemListView;
import com.sgdfv.emerson.sgd_fv.model.ItemOrcamento;
import com.sgdfv.emerson.sgd_fv.model.Orcamento;
import com.sgdfv.emerson.sgd_fv.model.Produto;
import com.sgdfv.emerson.sgd_fv.model.UrlConnection;
import com.sgdfv.emerson.sgd_fv.util.AdapterListView;
import com.sgdfv.emerson.sgd_fv.util.MascaraUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class ActivityEstoque extends AppCompatActivity {
    private ListView lvProdutos;
    private DBManager dbManager;
    private AlertDialog alerta;
    private AdapterListView adapterListView;
    private ArrayList<ItemListView> itens;
    private List<Produto> listaProduto;
    private EditText etPesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);

        listaProduto = new ArrayList<>();

        lvProdutos = (ListView) findViewById(R.id.lvProdutos);
        etPesquisar = (EditText) findViewById(R.id.etPesquisar);
        dbManager = new DBManager(this);

        listaProduto = dbManager.getListaTodosProdutos();
        if (listaProduto.size() > 0) {
            createListView();

        }
    }
    private void createListView() {
        //Criamos nossa lista que preenchera o ListView
        itens = new ArrayList<ItemListView>();
        int i = 1;
        for(Produto produto : listaProduto) {
            ItemListView item1 = new ItemListView(i,produto.getDescricao(), R.drawable.trolley);
            itens.add(item1);
            i++;
        }
        //Cria o adapter
        adapterListView = new AdapterListView(this, itens);

        //Define o Adapter
        lvProdutos.setAdapter(adapterListView);
        //Cor quando a lista é selecionada para ralagem.
        lvProdutos.setCacheColorHint(Color.TRANSPARENT);
        actionEvent();
    }

    public void actionEvent() {
        lvProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produto produtoSelecionado = listaProduto.get(((int) adapterListView.getItemId(position))-1);
                new ProdutoAsyncTask().execute(UrlConnection.URL+"buscarProduto.php?idproduto="+produtoSelecionado.getIdProduto());
            }
        });
        etPesquisar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterListView.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    class ProdutoAsyncTask extends AsyncTask<String, Void, List<Produto>> {
        private Dialog dialog;
        @Override
        protected List<Produto> doInBackground(String... params) {
            String urlString = params[0];
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(urlString);
            List<Produto> produtos = new ArrayList<>();
            try {
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String json = getStringFromInputStream(instream);
                    instream.close();

                    produtos = getProduto(json);
                }
            } catch (Exception e) {
                Log.e("Error", "Falha ao acessar Web service", e);
            }
            return produtos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ActivityEstoque.this, "Aguarde",
                    "Consultando Produto, Por Favor Aguarde...");

        }

        private List<Produto> getProduto(String jsonString) {

            List<Produto> produtos = new ArrayList<>();

            try {
                JSONArray locationLists = new JSONArray(jsonString);
                JSONObject produtoJson;
                for (int i = 0; i < locationLists.length(); i++) {
                    produtoJson = new JSONObject(locationLists.getString(i));

                    Log.i("TESTE", "id=" + produtoJson.getString("idproduto"));

                    Produto produto = new Produto();
                    produto.setIdProduto(produtoJson.getLong("idproduto"));
                    produto.setDescricao(produtoJson.getString("descricao"));
                    produto.setUnidade(produtoJson.getString("unidade"));
                    produto.setPreco(produtoJson.getDouble("preco"));
                    produto.setEstoque(produtoJson.getInt("estoque"));
                    produtos.add(produto);
                }
            } catch (JSONException e) {
                Log.e("Error", "Erro no parsing do JSON", e);
            }

            return produtos;

        }

        @Override
        protected void onPostExecute(List<Produto> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(result.size() > 0){
                Produto produto = result.get(0);
                mostrarProduto(produto);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ActivityEstoque.this).setTitle("Atenção")
                        .setMessage("Não foi possivel acessar o servidor...")
                        .setPositiveButton("OK", null);
                builder.create().show();
            }
        }
    }

    public void mostrarProduto(Produto produto){
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(produto.getDescricao());
        //define a mensagem
        builder.setMessage("PREÇO: "+ MascaraUtil.setValorCampoMoeda(produto.getPreco())+"\nQUANTIDADE NO ESTOQUE: "+produto.getEstoque()+"\nUNIDADE: "+produto.getUnidade());
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    public static String getStringFromInputStream(InputStream stream) throws IOException {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, "UTF8");
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }
}
