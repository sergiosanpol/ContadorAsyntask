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
import android.widget.Toast;

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

        //Cargamos los recursos
        cont = (TextView) findViewById(R.id.textView_cont);
        start = (Button) findViewById(R.id.button_comenzar);
        stop = (Button) findViewById(R.id.button_parar);
        bar = (ProgressBar) findViewById(R.id.progressBar);

        //Configuramos la barra de progreso, que tendrá un máximo de 10
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bar.setMin(0);
        }
        bar.setMax(10);
    }

    /**
     * Método que hace comenzar el contador. Si ya hay un proceso activo no comenzará uno nuevo
     * @param v El botón de la vista "comenzar contador"
     */
    public void comenzarCont(View v) {
        if(!activo) { //Si hay algún contador activo no inicia el hilo
            contador = new Contador();
            contador.execute();
        }
    }

    /**
     * Método que cancela el proceso. Si no hay ningún proceso activo no hace nada
     * @param v
     */
    public void cancelarCont(View v){
        //Si contador no está inicializado, no podemos para el contador ya que no hay ningún proceso de contador activo
        if(contador != null) {
            contador.cancel(true);
            Toast.makeText(this, "Contador parado", Toast.LENGTH_LONG).show();
        }
    }

    // Async Task

    class Contador extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            //Antes de la ejecución del proceso. Ponemos activo a true para inicar que hay un proceso activo
            //Inicializamos el contador y la barra de proceso a 0
            activo = true;
            cont.setText("0");
            bar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... n) {
            //Hacemos las iteraciones del for hasta 10
            for (int i = 0; i <= 10 && !isCancelled(); i++) {
                //Actualizamos la barra de proceso. Este método le envia la información al método onProgressUpdate
                publishProgress(i);
                //Dejamos el proceso dormido 1 segundo
                SystemClock.sleep(1000);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... porc) {
            //Aquí se actualizará la barra de poceso y la vista que muestra los números del contador, que debemos realizar esa actualización
            //desde el hilo principal ya que no podemos hacerlo en un hilo secundario
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
            //Cuando termina el proceso, ponemos a false el booleano activo para que se pueda ejecutar de nuevo el proceso
            activo = false;
        }

        @Override
        protected void onCancelled() {
            //Cuando se cancela activo se vuelve falso para poder iniciar de nuevo la cuenta
            if(activo == true) {
                activo = false;
            }
        }

    }

}
