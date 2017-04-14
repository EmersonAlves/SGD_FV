package com.sgdfv.emerson.sgd_fv;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.sgdfv.emerson.sgd_fv.db.DBManager;
import com.sgdfv.emerson.sgd_fv.model.Produto;
import com.sgdfv.emerson.sgd_fv.model.Usuario;


public class MainSplash extends AppCompatActivity {
    private static int  SPLASH_TIME_OUT = 3000;
    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        dbManager = new DBManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                Intent intent = new Intent(MainSplash.this,MainActivity.class);
                startActivity(intent);
                // Fecha esta activity
                finish();
            }
        },SPLASH_TIME_OUT);
    }
    private class CarregaDadosAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
