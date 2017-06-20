package com.sgdfv.emerson.sgd_fv;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sgdfv.emerson.sgd_fv.db.DBManager;
import com.sgdfv.emerson.sgd_fv.model.Cidade;
import com.sgdfv.emerson.sgd_fv.model.Endereco;
import com.sgdfv.emerson.sgd_fv.model.EnviarOrcamento;
import com.sgdfv.emerson.sgd_fv.model.ItemOrcamento;
import com.sgdfv.emerson.sgd_fv.model.Orcamento;
import com.sgdfv.emerson.sgd_fv.model.UrlConnection;
import com.sgdfv.emerson.sgd_fv.model.Usuario;
import com.sgdfv.emerson.sgd_fv.util.Mask;

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


public class MainUsuario extends AppCompatActivity {
    private AutoCompleteTextView atCidade;
    private Spinner spLogradouro;
    private Button btnSalvar;
    private DBManager dbManager;
    private EditText etNome;
    private EditText etCnpfCpf;
    private EditText etFone;
    private EditText etEndereco;
    private EditText etNumero;
    private EditText etBairro;
    private EditText etComplemento;
    private EditText etNomeFantasia;
    private Cidade cidade;
    private String url;
    private Spinner spVendedor;
    private Usuario vendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        etNome = (EditText) findViewById(R.id.etNome);
        etNomeFantasia = (EditText) findViewById(R.id.etNomeFantasia);
        etCnpfCpf = (EditText) findViewById(R.id.etCnpfCpf);
        etFone = (EditText) findViewById(R.id.etFone);
        etEndereco = (EditText) findViewById(R.id.etEndereco);
        etNumero = (EditText) findViewById(R.id.etNumero);
        etBairro = (EditText) findViewById(R.id.etBairro);
        etComplemento = (EditText) findViewById(R.id.etComplemento);
        atCidade = (AutoCompleteTextView) findViewById(R.id.atCidade);
        spLogradouro = (Spinner) findViewById(R.id.spLogradouro);
        spVendedor = (Spinner) findViewById(R.id.spVendedor);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        dbManager = new DBManager(this);

        etFone.addTextChangedListener(Mask.insert(Mask.CELULAR_MASK, etFone));
        url = "http://"+dbManager.getListaIp().get(0).getIp()+":8090/php/";
        popularComponents();
        actionEvent();

    }
    public void popularComponents() {
        if (dbManager.getListaTodosCidades().size() > 0) {
            List<Cidade> cidades = dbManager.getListaTodosCidades();
            if (cidades.size() > 0) {
                ArrayAdapter<Cidade> apadterCliente = new ArrayAdapter<Cidade>(this, android.R.layout.simple_dropdown_item_1line, cidades);
                atCidade.setAdapter(apadterCliente);
                atCidade.setThreshold(1);
            }
        }
        List<String> logradouros = new ArrayList<>();
        logradouros.add("RUA");
        logradouros.add("AVENIDA");
        logradouros.add("TRAVESSA");
        logradouros.add("ALAMEDA");
        logradouros.add("ESTRADA");
        logradouros.add("LOTEAMENTO");
        logradouros.add("RODOVIA");
        logradouros.add("RODOVIA");
        logradouros.add("DISTRITO");
        ArrayAdapter<String> apadterLogradouro = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, logradouros);
        spLogradouro.setAdapter(apadterLogradouro);
        List<Usuario> vendedor = new ArrayList<>();
        for(Usuario usuario : dbManager.getListaUsuarios()){
            if (usuario.getTipo().equals("FUNCIONARIO")) {
                vendedor.add(usuario);
            }
        }
        ArrayAdapter<Usuario> apadterVendedor = new ArrayAdapter<Usuario>(this, android.R.layout.simple_dropdown_item_1line, vendedor);

        spVendedor.setAdapter(apadterVendedor);
    }

    public void actionEvent() {
        atCidade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cidade = (Cidade) parent.getItemAtPosition(position);
                etBairro.requestFocus();
            }
        });
        spVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vendedor = (Usuario) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String logradouro = spLogradouro.getSelectedItem().toString();
                Usuario usuario = new Usuario();
                if(etNome.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Informe o Nome do Cliente",Toast.LENGTH_SHORT).show();
                    etNome.requestFocus();
                    return;
                }
                if(etEndereco.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Informe o Endereco do Cliente",Toast.LENGTH_SHORT).show();
                    etEndereco.requestFocus();
                    return;
                }

                if(cidade == null){
                    Toast.makeText(getApplicationContext(),"Informe a Cidade",Toast.LENGTH_SHORT).show();
                    atCidade.requestFocus();
                    return;
                }
                usuario.setNome(etNome.getText().toString());
                usuario.setNomeFantasia(etNomeFantasia.getText().toString());
                usuario.setCpfcnpj(etCnpfCpf.getText().toString());
                usuario.setFone(etFone.getText().toString());

                Endereco endereco = new Endereco();
                endereco.setLogradouro(logradouro);
                endereco.setEndereco(etEndereco.getText().toString());
                endereco.setIdcidade(cidade.getIdCidade());
                endereco.setIdestado(cidade.getCodigoestado());
                endereco.setNumero(etNumero.getText().toString());
                endereco.setIdestado(cidade.getNome());
                endereco.setComplemento(etComplemento.getText().toString());
                endereco.setBairro(etBairro.getText().toString());

                usuario.setEndereco(endereco);
                usuario.setUrl(url+"salvarUsuario.php");
                new UsuarioAsyncTask().execute(usuario);
            }
        });
    }

    class UsuarioAsyncTask extends AsyncTask<Usuario, Void, Usuario> {
        private Dialog dialog;

        @Override
        protected Usuario doInBackground(Usuario... params) {
            String urlString = params[0].getUrl();
            try {
                Usuario usuario = params[0];
                HttpContext localContext = new BasicHttpContext();
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(urlString);
                post.setHeader("Content-type", "application/json");
                //post.setHeader("Authorization",token);
                //post.setHeader("Cookie","ASP.NET_SessionId="+sessao+"; path=/; HttpOnly");
                JSONObject obj = new JSONObject();
                JSONArray ja = new JSONArray();
                obj.put("nome", usuario.getNome());
                obj.put("nomefantasia", usuario.getNomeFantasia());
                obj.put("cpfcnpj", usuario.getCpfcnpj());
                obj.put("fone", usuario.getFone());
                obj.put("vendedor", vendedor.getIdUsuario());

                obj.put("bairro", usuario.getEndereco().getBairro());
                obj.put("complemento",usuario.getEndereco().getComplemento());
                obj.put("endereco", usuario.getEndereco().getEndereco());
                obj.put("logradouro", usuario.getEndereco().getLogradouro());
                obj.put("numero",usuario.getEndereco().getNumero());
                obj.put("codigocidade",cidade.getIdCidade());
                obj.put("codigoestado",cidade.getCodigoestado());

                StringEntity se = new StringEntity(obj.toString());
                post.setEntity(se);
                HttpResponse response = client.execute(post, localContext);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String json = getStringFromInputStream(instream);
                    instream.close();
                    instream.close();
                    usuario = getUsuario(usuario, json);
                    return usuario;
                }
            } catch (Exception e) {
                Log.e("Error", "Falha ao acessar Web service", e);
            }
            return params[0];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainUsuario.this, "Aguarde",
                    "Cadastrando Usuario, Por Favor Aguarde...");
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            super.onPostExecute(usuario);
            dialog.dismiss();
            if (usuario.getIdServidor() != null) {
                Intent intent = new Intent(MainUsuario.this,MainSplash.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Usuario Cadastrado", Toast.LENGTH_LONG).show();
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainUsuario.this).setTitle("Atenção")
                        .setMessage("Não foi possivel acessar o servidor...")
                        .setPositiveButton("OK", null);
                builder.create().show();
            }
        }

        private Usuario getUsuario(Usuario usuario, String jsonString) {
            List<Usuario> usuarios = new ArrayList<>();

            try {
                JSONArray locationLists = new JSONArray(jsonString);
                JSONObject orcJson;
                for (int i = 0; i < locationLists.length(); i++) {
                    orcJson = new JSONObject(locationLists.getString(i));

                    Log.i("TESTE", "id=" + orcJson.getLong("idusuario"));

                    usuario.setIdServidor(orcJson.getLong("idusuario"));
                    usuarios.add(usuario);
                }
            } catch (JSONException e) {
                Log.e("Error", "Erro no parsing do JSON", e);
            }
            return usuarios.get(0);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}