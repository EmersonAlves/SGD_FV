package com.sgdfv.emerson.sgd_fv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sgdfv.emerson.sgd_fv.model.Orcamento;
import com.sgdfv.emerson.sgd_fv.model.Usuario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainOrcamento extends AppCompatActivity {
    private Button btnProximo;
    private AutoCompleteTextView atNome;
    private EditText etEndereco;
    private Spinner spVendedor;

    private Orcamento orcamento;
    private Usuario usuarioSelecionado;
    private Usuario vendedorSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento);

        atNome = (AutoCompleteTextView) findViewById(R.id.atNome);
        btnProximo = (Button) findViewById(R.id.btnProximo);
        etEndereco = (EditText) findViewById(R.id.etEndereco);
        spVendedor = (Spinner) findViewById(R.id.spVendedor);

        actionButton();
        popularCliente();
        popularVendedor();
        selecionaUsuario();
    }
    public void actionButton(){
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usuarioSelecionado != null) {
                    orcamento = new Orcamento();
                    orcamento.setUsuario(usuarioSelecionado);
                    orcamento.setVendedor(usuarioSelecionado);
                    orcamento.setDtEmissao(new Date());

                    Intent intent = new Intent(MainOrcamento.this, ActivityItem.class);
                    intent.putExtra("orcamento",orcamento);

                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Usuario não selecionado",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void popularCliente(){
        List<Usuario> usuarios = new ArrayList<>();
        Usuario usuario1 = new Usuario();
        usuario1.setIdUsuario(1l);
        usuario1.setNome("Antonio");
        usuario1.setEndereco("Rua Luis N 11");

        Usuario usuario2 = new Usuario();
        usuario2.setIdUsuario(2l);
        usuario2.setNome("Jose");
        usuario2.setEndereco("Rua AAA N 112");

        usuarios.add(usuario1);
        usuarios.add(usuario2);

        ArrayAdapter<Usuario> apadter = new ArrayAdapter<Usuario>(this,android.R.layout.simple_dropdown_item_1line,usuarios);
        atNome.setAdapter(apadter);
        atNome.setThreshold(1);
    }

    public void popularVendedor(){
        List<Usuario> vendedores = new ArrayList<>();
        Usuario usuario1 = new Usuario();
        usuario1.setIdUsuario(3l);
        usuario1.setNome("Ze");
        usuario1.setEndereco("Rua Luis N 11");

        Usuario usuario2 = new Usuario();
        usuario2.setIdUsuario(4l);
        usuario2.setNome("Luiz");
        usuario2.setEndereco("Rua Luis N 11");

        vendedores.add(usuario1);
        vendedores.add(usuario2);

        ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(this,android.R.layout.simple_dropdown_item_1line,vendedores);
        spVendedor.setAdapter(adapter);
    }

    public void selecionaUsuario(){
        atNome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usuarioSelecionado = (Usuario) parent.getItemAtPosition(position);
                etEndereco.setText(usuarioSelecionado.getEndereco());
            }
        });
    }
}
