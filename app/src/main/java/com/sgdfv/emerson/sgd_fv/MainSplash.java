package com.sgdfv.emerson.sgd_fv;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.sgdfv.emerson.sgd_fv.db.DBManager;
import com.sgdfv.emerson.sgd_fv.model.Produto;
import com.sgdfv.emerson.sgd_fv.model.Usuario;

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

import pl.droidsonroids.gif.GifImageView;


public class MainSplash extends AppCompatActivity {
    private DBManager dbManager;
    private TextView tvLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        dbManager = new DBManager(this);

        tvLoading = (TextView) findViewById(R.id.tvCarregamento);


        if (dbManager.getListaUsuarios().size() > 0) {
            Long id = dbManager.getUltimoUsuario().getIdUsuario();
            new UsuarioAsyncTask()
                    .execute("http://192.168.1.3/php/usuarios.php?atualizar=" + id);
        } else {

            UsuarioAsyncTask buscarUsuarios = new UsuarioAsyncTask();
            buscarUsuarios
                    .execute("http://192.168.1.3/php/usuarios.php");
        }
    }

    class UsuarioAsyncTask extends AsyncTask<String, Void, List<Usuario>> {
        @Override
        protected List<Usuario> doInBackground(String... params) {
            String urlString = params[0];
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(urlString);
            List<Usuario> usuarios = new ArrayList<>();
            try {
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String json = getStringFromInputStream(instream);
                    instream.close();

                    usuarios = getUsuario(json);
                }
            } catch (Exception e) {
                Log.e("Error", "Falha ao acessar Web service", e);
            }
            return usuarios;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvLoading.setText("Atualizando Clientes, Por Favor Aguarde... ");
        }

        private List<Usuario> getUsuario(String jsonString) {

            List<Usuario> usuarios = new ArrayList<>();

            try {
                JSONArray locationLists = new JSONArray(jsonString);
                JSONObject usuarioJson;
                for (int i = 0; i < locationLists.length(); i++) {
                    usuarioJson = new JSONObject(locationLists.getString(i));
                    Log.i("TESTE", "id=" + usuarioJson.getString("idusuario"));
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(usuarioJson.getLong("idusuario"));
                    usuario.setTipo(usuarioJson.getString("tipo"));
                    usuario.setNome(usuarioJson.getString("nome"));
                    dbManager.inserirUsuario(usuario);
                    usuarios.add(usuario);
                }
            } catch (JSONException e) {
                Log.e("Error", "Erro no parsing do JSON", e);
            }

            return usuarios;

        }

        @Override
        protected void onPostExecute(List<Usuario> result) {
            super.onPostExecute(result);
            if(dbManager.getListaTodosProdutos().size() > 0){
                Long id = dbManager.getUlitmoProduto().getIdProduto();
                new ProdutoAsyncTask().execute("http://192.168.1.3/php/produtos.php?atualizar="+id);
            }else{
                new ProdutoAsyncTask().execute("http://192.168.1.3/php/produtos.php");
            }

        }
    }

    class ProdutoAsyncTask extends AsyncTask<String, Void, List<Produto>> {

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
            tvLoading.setText("Atualizando Produtos, Por Favor Aguarde...");
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
                    dbManager.inserirProduto(produto);
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
            Intent intent = new Intent(MainSplash.this, MainActivity.class);
            startActivity(intent);
            finish();
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
}
