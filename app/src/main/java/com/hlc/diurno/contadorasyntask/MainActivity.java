package com.hlc.diurno.contadorasyntask;

import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView cont;
    private Button start, stop;
    private boolean activo = false;
    private ProgressBar bar;
    private Contador contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cont = (TextView) findViewById(R.id.textView_cont);
        start = (Button) findViewById(R.id.button_comenzar);
        stop = (Button) findViewById(R.id.button_parar);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bar.setMin(0);
        }
        bar.setMax(10);
    }

    public void comenzarCont(View v) {
        if(!activo) { //Si hay algún contador activo no inicia el hilo
            contador = new Contador();
            contador.execute();
        }
    }

    public void cancelarCont(View v){
        if(contador != null) {
            contador.cancel(true);
        }
    }

    // Async Task

    class Contador extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            activo = true;
            cont.setText("0");
            bar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... n) {

            for (int i = 0; i <= 10 && !isCancelled(); i++) {
                onProgressUpdate(i);
                SystemClock.sleep(1000);
                //publishProgress(i*100 / i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... porc) {
            bar.setProgress(porc[0]);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cont.setText(porc[0].toString());
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            activo = false;
        }

        @Override
        protected void onCancelled() {
            if(activo == true) {
                activo = false;
            }
        }

    }

}
