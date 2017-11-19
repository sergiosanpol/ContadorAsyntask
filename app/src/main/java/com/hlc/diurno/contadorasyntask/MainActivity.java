package com.hlc.diurno.contadorasyntask;

import android.os.AsyncTask;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cont = (TextView) findViewById(R.id.textView_cont);
        start = (Button) findViewById(R.id.button_comenzar);
        stop = (Button) findViewById(R.id.button_parar);
    }

    public void comenzarCont(View v) {
        Contador contador = new Contador();
        contador.execute();
    }

    // Async Task

    class Contador extends AsyncTask<Void, Integer, Void> {

        private ProgressBar bar;

        @Override
        protected void onPreExecute() {
            if (activo == false) {
                activo = true;
            } else {
                this.onCancelled();
            }
        }

        @Override
        protected Void doInBackground(Void... n) {

            for (int i = 0; i <= 10 && !isCancelled(); i++) {
                cont.setText(i);
                SystemClock.sleep(1000);
                //publishProgress(i*100 / i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... porc) {
        }

        @Override
        protected void onCancelled() {
            cont.append("0");
        }

    }

}
