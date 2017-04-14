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

import com.sgdfv.emerson.sgd_fv.db.DBManager;
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

    private DBManager dbManager;

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
            List<Usuario> cliente = new ArrayList<>();
            List<Usuario> vendedor = new ArrayList<>();
            vendedor.add(null);
            for (Usuario usuario : dbManager.getListaUsuarios()) {
                if (usuario.getTipo().equals("FUNCIONARIO")) {
                    vendedor.add(usuario);
                } else {
                    cliente.add(usuario);
                }
            }

            ArrayAdapter<Usuario> apadterCliente = new ArrayAdapter<Usuario>(this, android.R.layout.simple_dropdown_item_1line, cliente);
            ArrayAdapter<Usuario> apadterVendedor = new ArrayAdapter<Usuario>(this, android.R.layout.simple_dropdown_item_1line, vendedor);

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
                etEndereco.setText(usuarioSelecionado.getEndereco());
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
