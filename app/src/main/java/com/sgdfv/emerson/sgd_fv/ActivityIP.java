package com.sgdfv.emerson.sgd_fv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sgdfv.emerson.sgd_fv.db.DBManager;
import com.sgdfv.emerson.sgd_fv.model.Ip;

public class ActivityIP extends AppCompatActivity {

    private DBManager dbManager;
    private Button btnSalva;
    private EditText etIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        dbManager = new DBManager(this);
        btnSalva = (Button) findViewById(R.id.btnSalvaIp);
        etIP = (EditText) findViewById(R.id.etIp);
        if(dbManager.getListaIp().size() > 0){
            etIP.setText(dbManager.getListaIp().get(0).getIp());
        }
        btnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbManager.getListaIp().size() > 0){
                    Ip ip = dbManager.getListaIp().get(0);
                    ip.setIp(etIP.getText().toString());
                    dbManager.atualizarIp(ip);
                    Toast.makeText(getApplicationContext(), "IP atualizado", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Ip ip = new Ip();
                    ip.setIp(etIP.getText().toString());
                    Toast.makeText(getApplicationContext(), "IP atualizado", Toast.LENGTH_SHORT).show();
                    dbManager.inserirIp(ip);
                    finish();
                }
            }
        });
    }
}
