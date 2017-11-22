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
    private ProgressBar bar;
    private Contador contador = new Contador();

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
        if(!contador.getStatus().equals(AsyncTask.Status.RUNNING)) { //Si hay algún contador activo no inicia el hilo
            contador = new Contador();
            contador.execute();
        }
    }

    /**
     * Método que cancela el proceso. Si no hay ningún proceso activo no hace nada
     * @param v
     */
    public void cancelarCont(View v){
        //Si contador está ejecitandose podremos para el hilo, si no, no podemos parar el contador ya que no
        // hay ningún proceso de contador activo
        if(contador.getStatus().equals(AsyncTask.Status.RUNNING)) {
            contador.cancel(true);
            Toast.makeText(this, "Contador parado", Toast.LENGTH_LONG).show();
        }
    }

    // Async Task

    class Contador extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            //Antes de la ejecución del proceso. Inicializamos el contador y la barra de proceso a 0
            cont.setText("0");
            bar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... n) {
            //Hacemos las iteraciones del for hasta 10 o se cancele el proceso
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
            //Aquí se actualizará la barra de poceso y la vista que muestra los números del contador
            bar.setProgress(porc[0]);
            cont.setText(porc[0].toString());

        }

    }

}
