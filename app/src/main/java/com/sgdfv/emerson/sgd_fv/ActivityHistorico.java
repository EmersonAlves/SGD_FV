package com.sgdfv.emerson.sgd_fv;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sgdfv.emerson.sgd_fv.db.DBManager;
import com.sgdfv.emerson.sgd_fv.model.EnviarOrcamento;
import com.sgdfv.emerson.sgd_fv.model.ItemListView;
import com.sgdfv.emerson.sgd_fv.model.ItemOrcamento;
import com.sgdfv.emerson.sgd_fv.model.Orcamento;
import com.sgdfv.emerson.sgd_fv.model.Produto;
import com.sgdfv.emerson.sgd_fv.model.Usuario;
import com.sgdfv.emerson.sgd_fv.util.AdapterListView;
import com.sgdfv.emerson.sgd_fv.util.MascaraUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

public class ActivityHistorico extends AppCompatActivity {
    private ListView lvOrcamentos;
    private DBManager dbManager;
    private EditText etPesquisar;
    private AdapterListView adapterListView;
    private ArrayList<ItemListView> itens;
    private List<Orcamento> orcamentos;
    private Button btnEnviar;
    private  AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        etPesquisar = (EditText)findViewById(R.id.etPesquisar);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);
        dbManager = new DBManager(this);
        orcamentos = new ArrayList<>();


        lvOrcamentos = (ListView) findViewById(R.id.lvOrcamentos);
        createListView();
    }

    private void createListView() {
        //Criamos nossa lista que preenchera o ListView
        itens = new ArrayList<ItemListView>();
        itens.clear();
        int i = 1;
        orcamentos.clear();
        orcamentos = dbManager.getListaOrcamentos();
        boolean verificar = false;
        for(Orcamento orcamento:orcamentos){
            ItemListView item1 = null;
            if(orcamento.getStatus().equals("ENVIADO")) {
                item1 = new ItemListView(i, orcamento.getUsuario().getNome(), R.drawable.confirm);
            }else if(orcamento.getStatus().equals("NAO ENVIADO")){
                item1 = new ItemListView(i, orcamento.getUsuario().getNome(), R.drawable.attention);
                verificar = true;
            }else{
                item1 = new ItemListView(i, orcamento.getUsuario().getNome(), R.drawable.cancelado);
            }
            itens.add(item1);
            i++;

            if(verificar == true){
                btnEnviar.setVisibility(View.VISIBLE);
            }else{
                btnEnviar.setVisibility(View.INVISIBLE);
            }
        }
        //Cria o adapter
        adapterListView = new AdapterListView(this, itens);

        //Define o Adapter
        lvOrcamentos.setAdapter(adapterListView);
        //Cor quando a lista é selecionada para ralagem.
        lvOrcamentos.setCacheColorHint(Color.TRANSPARENT);
        actionEvent();
    }

    public void actionEvent() {
        lvOrcamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Orcamento orcamento = orcamentos.get(((int) adapterListView.getItemId(position))-1);
                if(orcamento.getStatus().equals("NAO ENVIADO")) {
                    CancelarOrcamento(orcamento);
                }
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
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarOrcamento enviarOrcamento = new EnviarOrcamento();
                enviarOrcamento.setUrl("http://192.168.1.3/php/orcamentos.php");
                List<Orcamento> listaEnvio = new ArrayList<Orcamento>();
                for(Orcamento orc : orcamentos){
                    if(orc.getStatus().equals("NAO ENVIADO")){
                        listaEnvio.add(orc);
                    }
                }
                enviarOrcamento.setLista(listaEnvio);
                new OrcamentoAsyncTask().execute(enviarOrcamento);

            }
        });
    }

    class OrcamentoAsyncTask extends AsyncTask<EnviarOrcamento, Void,  List<Orcamento>> {
        private Dialog dialog;
        @Override
        protected List<Orcamento> doInBackground(EnviarOrcamento... params) {
            String urlString = params[0].getUrl();
            List<Orcamento> orcamentos = params[0].getLista();
            try {
                HttpContext localContext = new BasicHttpContext();
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(urlString);
                post.setHeader("Content-type", "application/json");
                //post.setHeader("Authorization",token);
                //post.setHeader("Cookie","ASP.NET_SessionId="+sessao+"; path=/; HttpOnly");
                JSONObject obj = new JSONObject();
                JSONArray jaorcas = new JSONArray();
                for(Orcamento orcamento : orcamentos) {
                    JSONObject objorc = new JSONObject();
                    objorc.put("idusuario", orcamento.getUsuario().getIdUsuario());
                    objorc.put("idvendedor", orcamento.getVendedor().getIdUsuario());
                    objorc.put("total", orcamento.getValorTotalOrcamento());
                    JSONArray jaitem = new JSONArray();
                    for (ItemOrcamento item : orcamento.getListaItens()) {
                        JSONObject aux = new JSONObject();
                        aux.put("idproduto", item.getProduto().getIdProduto());
                        aux.put("preco", item.getPrecoUnitario());
                        aux.put("qtde", item.getQuantidade());
                        aux.put("total", item.getValorTotalItem());
                        jaitem.put(aux);
                    }
                    objorc.put("item", jaitem);
                    jaorcas.put(objorc);
                }
                obj.put("orcamentos",jaorcas);

                StringEntity se = new StringEntity(obj.toString());
                post.setEntity(se);
                HttpResponse response = client.execute(post, localContext);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String json = getStringFromInputStream(instream);
                    instream.close();
                    instream.close();
                    orcamentos = getOrcamento(orcamentos,json);
                    return orcamentos;
                }
            } catch (Exception e) {
                Log.e("Error", "Falha ao acessar Web service", e);
            }
            return orcamentos;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ActivityHistorico.this, "Aguarde",
                    "Enviando Orcamentos, Por Favor Aguarde...");

        }
        @Override
        protected void onPostExecute(List<Orcamento> orcamento) {
            super.onPostExecute(orcamentos);
            dialog.dismiss();
            if(orcamentos.size() > 0) {
                createListView();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ActivityHistorico.this).setTitle("Atenção")
                        .setMessage("Não foi possivel acessar o servidor...")
                        .setPositiveButton("OK", null);
                builder.create().show();
            }
        }
        private List<Orcamento> getOrcamento(List<Orcamento> orcamentos,String jsonString) {
            try {
                JSONArray locationLists = new JSONArray(jsonString);
                JSONObject orcJson;
                for (int i = 0; i < locationLists.length(); i++) {
                    orcJson = new JSONObject(locationLists.getString(i));
                    Orcamento orcamento = orcamentos.get(i);
                    Log.i("TESTE", "id=" + orcJson.getLong("idorcamento"));
                    orcamento.setIdServidor(orcJson.getLong("idorcamento"));
                    orcamento.setStatus("ENVIADO");
                    dbManager.atualizarOrcamento(orcamento);
                }
            } catch (JSONException e) {
                Log.e("Error", "Erro no parsing do JSON", e);
            }
            return orcamentos;
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
    public void CancelarOrcamento(final Orcamento orcamento){
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(orcamento.getUsuario().getNome());
        //define a mensagem
        builder.setMessage("Deseja cancelar orcamento ?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Orcamento orca = orcamento;
                orca.setStatus("CANCELADO");
                dbManager.atualizarOrcamento(orca);
                createListView();
                alerta.dismiss();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alerta.dismiss();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
