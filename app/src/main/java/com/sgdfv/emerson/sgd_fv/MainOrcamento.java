package com.sgdfv.emerson.sgd_fv;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sgdfv.emerson.sgd_fv.db.DBManager;
import com.sgdfv.emerson.sgd_fv.model.Endereco;
import com.sgdfv.emerson.sgd_fv.model.Fantasia;
import com.sgdfv.emerson.sgd_fv.model.Orcamento;
import com.sgdfv.emerson.sgd_fv.model.Usuario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainOrcamento extends AppCompatActivity {
    private Button btnProximo;
    private AutoCompleteTextView atNome;
    private AutoCompleteTextView atFantasia;
    private EditText etEndereco;
    private Spinner spVendedor;

    private DBManager dbManager;

    private Orcamento orcamento;
    private Usuario usuarioSelecionado;
    private Usuario vendedorSelecionado;
    private List<Usuario> usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento);

        usuarios = new ArrayList<>();

        atNome = (AutoCompleteTextView) findViewById(R.id.atNome);
        atFantasia = (AutoCompleteTextView) findViewById(R.id.atFantasia);
        btnProximo = (Button) findViewById(R.id.btnProximo);
        etEndereco = (EditText) findViewById(R.id.etEndereco);
        spVendedor = (Spinner) findViewById(R.id.spVendedor);

        dbManager = new DBManager(this);

        actionEvent();
        popularComponents();
        selecionaUsuario();

    }
    public void actionEvent(){
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usuarioSelecionado == null) {
                    Toast.makeText(getApplicationContext(),"Cliente não selecionado",Toast.LENGTH_LONG).show();
                    return;
                }
                if(vendedorSelecionado == null || spVendedor.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(),"Vendedor não selecionado",Toast.LENGTH_LONG).show();
                    return;
                }
                orcamento = new Orcamento();
                orcamento.setUsuario(usuarioSelecionado);
                orcamento.setVendedor(vendedorSelecionado);
                orcamento.setDtEmissao(new Date());

                Intent intent = new Intent(MainOrcamento.this, ActivityItem.class);
                intent.putExtra("orcamento",orcamento);

                startActivity(intent);
            }
        });
        atNome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == true && atNome.getText().toString().isEmpty()){
                    atNome.showDropDown();
                }
            }
        });
    }
    public void popularComponents(){
        if(dbManager.getListaUsuarios().size() > 0) {
            List<Usuario> vendedor = new ArrayList<>();
            List<Fantasia> fantasia = new ArrayList<>();

            vendedor.add(null);
            for (Usuario usuario : dbManager.getListaUsuarios()) {
                if (usuario.getTipo().equals("FUNCIONARIO")) {
                    vendedor.add(usuario);
                } else {
                    usuarios.add(usuario);
                    Fantasia fantasia1 = new Fantasia(usuario);
                    fantasia.add(fantasia1);
                }
            }
            ArrayAdapter<Usuario> apadterCliente = new ArrayAdapter<Usuario>(this, android.R.layout.simple_dropdown_item_1line, usuarios);
            ArrayAdapter<Usuario> apadterVendedor = new ArrayAdapter<Usuario>(this, android.R.layout.simple_dropdown_item_1line, vendedor);
            ArrayAdapter<Fantasia> apadterFantasia = new ArrayAdapter<Fantasia>(this, android.R.layout.simple_dropdown_item_1line, fantasia);

            atFantasia.setAdapter(apadterFantasia);
            atFantasia.setThreshold(1);

            atNome.setAdapter(apadterCliente);
            atNome.setThreshold(1);

            spVendedor.setAdapter(apadterVendedor);
        }
    }

    public void selecionaUsuario(){
        atNome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usuarioSelecionado = (Usuario) parent.getItemAtPosition(position);
                atFantasia.setText(usuarioSelecionado.getNomeFantasia());
                Endereco endereco = dbManager.getEndereco(usuarioSelecionado.getIdUsuario());
                etEndereco.setText(endereco.getLogradouro()+" "+endereco.getEndereco()+" "+endereco.getNumero());
                ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        atNome.getWindowToken(), 0);
            }
        });

        atFantasia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usuarioSelecionado = (Fantasia) parent.getItemAtPosition(position);
                atNome.setText(usuarioSelecionado.getNome());
                Endereco endereco = dbManager.getEndereco(usuarioSelecionado.getIdUsuario());
                etEndereco.setText(endereco.getLogradouro()+" "+endereco.getEndereco()+" "+endereco.getNumero());
                ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        atFantasia.getWindowToken(), 0);
            }
        });

        spVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vendedorSelecionado = (Usuario) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atNome.setText("");
        etEndereco.setText("");
        spVendedor.setSelection(0);
        usuarioSelecionado = null;
        orcamento = null;
    }
}
