package com.sgdfv.emerson.sgd_fv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnOrcamento;
    private Button btnUsuario;
    private Button btnHistoricoOrcamento;
    private Button btnEstoque;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOrcamento = (Button) findViewById(R.id.btnOrcamento);
        btnUsuario = (Button) findViewById(R.id.btnUsuario);
        btnHistoricoOrcamento = (Button) findViewById(R.id.btnListaOrcamentos);
        btnEstoque = (Button) findViewById(R.id.btnEstoque);

        actionButton();
    }

    public void actionButton(){
        btnOrcamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainOrcamento.class);
                startActivity(intent);
            }
        });
        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainUsuario.class);
                startActivity(intent);
                finish();
            }
        });
        btnHistoricoOrcamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ActivityHistorico.class);
                startActivity(intent);
            }
        });
        btnEstoque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ActivityEstoque.class);
                startActivity(intent);
            }
        });
    }
}
