package com.example.crisrodfe.buscaminas.MisClases;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.crisrodfe.buscaminas.MainActivity;
import com.example.crisrodfe.buscaminas.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by CrisRodFe
 */
public class MisDialogos
{
    /**
     * Diálogo que muestra un texto con las instrucciones del juego.
     * El texto está en un archivo .txt almacenado como recurso en la carpeta \raw.
     * Es utilizado con la opción de menú de 'Instrucciones'
     *
     * @param mainActivity
     * @throws IOException
     */
    public void dialogoInstrucciones(MainActivity mainActivity) throws IOException
    {
        AlertDialog.Builder dialogoInstrucciones = new AlertDialog.Builder(mainActivity);
        dialogoInstrucciones.setTitle("Instrucciones");

        String linea;

        InputStream isr = mainActivity.getResources().openRawResource(R.raw.instrucciones);
        BufferedReader br = new BufferedReader(new InputStreamReader(isr));
        TextView textview = new TextView(mainActivity);
        textview.setMovementMethod(new ScrollingMovementMethod());
        while ((linea = br.readLine())!=null)
        {
            textview.append(linea+"\n");
        }
        isr.close();
        dialogoInstrucciones.setView(textview);
        dialogoInstrucciones.setNeutralButton("OK", null);
        dialogoInstrucciones.show();
    }


    /**
     * Diálogo de fin de juego.
     * Aparecerá cuando se pulse una casilla con una mina.
     *
     * @param mainActivity
     */
    public void dialogoMinaDescubierta(MainActivity mainActivity)
    {
        AlertDialog.Builder dialogoMinaDescubierta = new AlertDialog.Builder(mainActivity);
        dialogoMinaDescubierta.setTitle("Fin de Juego");
        dialogoMinaDescubierta.setMessage("\t¡Mina Descubierta! \nHas perdido...");
        dialogoMinaDescubierta.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogoMinaDescubierta, int id) {
            }
        });
        dialogoMinaDescubierta.show();
    }

    /**
     * Diálogo de fin de juego.
     * Aparecerá cuando se haga una pulsación para poner una banderita sobre una casilla que no contiene una mina.
     *
     * @param mainActivity
     */
    public void dialogoNoEsMina(MainActivity mainActivity)
    {
        AlertDialog.Builder dialogoNoEsMina = new AlertDialog.Builder(mainActivity);
        dialogoNoEsMina.setTitle("Fin de Juego");
        dialogoNoEsMina.setMessage("\t¡Ahí no había una mina! \nHas perdido...");
        dialogoNoEsMina.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogoMinaDescubierta, int id) {
            }
        });
        dialogoNoEsMina.show();
    }

    /**
     * Diálogo que muestra todos los registros de las puntuaciones almacenadas en la base de datos.
     * Se ha añadido una opción de menú para ello.
     *
     * @param mainActivity
     */
    public void verResultados(MainActivity mainActivity)
    {
        AlertDialog.Builder dialogoInstrucciones = new AlertDialog.Builder(mainActivity);
        dialogoInstrucciones.setTitle("Puntuaciones");
        MiBBDD bbdd = new MiBBDD(mainActivity, null, null, 1);
        TextView textView = new TextView(mainActivity);
        textView.setTypeface(Typeface.MONOSPACE);//Tiene que estar configurado como MONOSPACE para que el string aparezca con el formato prefijado(ancho de columna de 8 caracteres)
        textView.append(bbdd.verPuntuaciones());
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setTextSize(20);
        dialogoInstrucciones.setView(textView);
        dialogoInstrucciones.setNeutralButton("OK", null);
        dialogoInstrucciones.show();
    }

    /**
     * Diálogo de fin de juego cuando se ha completado.
     * Recibe como argumentos el tiempo de juego transcurrido y el nivel en el que se ha jugado,
     * Hace una llamada a un método de la clase MiBBDD que creará un registro con esos datos.
     * Además pide el nombre al jugador, si no se introduce ninguno, por defecto en la bbdd el valor en la columna jugador es 'Jugador'
     *
     * @param mainActivity
     * @param crono
     * @param nivel
     */
    public void dialogoVictoria(MainActivity mainActivity, Chronometer crono, int nivel,int numeroClicks)
    {
        final MiBBDD bd = new MiBBDD(mainActivity.getApplicationContext(), null, null, 1);
        final String tiempo = crono.getText().toString();
        final int puntos = nivel * 1000 - numeroClicks*10;
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Fin del Juego");
        builder.setMessage("¡Ehnorabuena!Has completado el juego!\nIntroduce tu nombre");
        final EditText input = new EditText(mainActivity);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String nombre = input.getText().toString();
                if(nombre.length() > 7)//Como el ancho de columna está prefijado a 8 caracteres, si el nombre introducido es mayor lo ajustamos.
                        nombre = nombre.substring(0,7);
                String jugador = input.getText().toString().equals("")?"Jugador":nombre;
                Log.i("", jugador);
                bd.addJugador(jugador, tiempo, puntos);
            }
        });
        builder.show();
    }
}
