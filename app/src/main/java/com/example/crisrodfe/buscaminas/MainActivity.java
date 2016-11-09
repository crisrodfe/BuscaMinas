 package com.example.crisrodfe.buscaminas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.TextView;
import com.example.crisrodfe.buscaminas.MisClases.MiBBDD;
import com.example.crisrodfe.buscaminas.MisClases.MisBotones;
import com.example.crisrodfe.buscaminas.MisClases.MisDialogos;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
{
    //Objetos visuales
    GridLayout g;
    MisBotones b;
    Toolbar toolbar;
    TextView textViewMinas;
    Chronometer crono;

    //Clases utilizadas.
    MiBBDD bbdd;
    MisDialogos misDialogos;

    int nivel = 1; //Variable que almacena el nivel de juego, por defecto 1 (fácil).
    int subnivel = 1;////Variable que almacena el subnivel de juego, por defecto() el nivel por defecto será por tanto fácil 8x8 con 5 minas).

    int minas = 2; //Número de minas totales(predefinido en cada nivel)
                  //Está configurado para que el tablero de inicio sólo tenga dos minas y poder hacer pruebas de puntuaciones,func de clicks,poner quitar banderas,etc. mas fácilmente.

    int numeroMinas = minas; //Número de minas que quedan por descubrir en el tablero(incrementan/decrementa con longclick,poniendo/quitando banderas)
    int numeroClicks = 0;//Número de cliks/interacciones del usuario, solo sirve para calcular los puntos que se le asignarán al jugador.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.example.crisrodfe.buscaminas.R.layout.activity_main);

        //Creación de la barra de herramientas donde estará el menú de opciones.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Opciones");
        setSupportActionBar(toolbar);

        //GridLayout donde colocaremos el tablero.
        g = (GridLayout) findViewById(R.id.gridLayout);

        //Cronómetro y textView que colocaremos debajo del tablero para mostar el tiempo de juego transcurrido y el número de minas restantes.
        textViewMinas = (TextView)findViewById(R.id.textViewMinas);
        crono = (Chronometer)findViewById(R.id.chronometerCrono);

        //Instanciación de las clases propias.
        bbdd = new MiBBDD(this, null, null, 1);
        misDialogos = new MisDialogos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(com.example.crisrodfe.buscaminas.R.menu.menu_main, menu);
        generarTablero(8, 8);//Generación del tablero para que aparezca al iniciarse la aplicación.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Al pulsar sobre el menú de opciones paramos el crono y deshabilitamos los botones, de esta manera el usuario tendrá que crear un nuevo juego.
        crono.stop();
        deshabilitarBotones();
        int id = item.getItemId();
        switch (id)
        {
            case R.id.Instrucciones:
                try {
                   misDialogos.dialogoInstrucciones(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.NuevoJuego:
                //Según el nivel(número de casillas) y el subnivel(número de minas), configuramos el tablero.
                if (nivel == 1)
                {
                    minas = subnivel == 1?5:10;
                    generarTablero(8, 8);
                }
                if (nivel == 2)
                {
                    minas = subnivel == 1?15:30;
                    generarTablero(12, 12);
                }
                if (nivel == 3)
                {
                    minas = subnivel == 1?40:60;
                    generarTablero(16, 16);
                }
                break;
            case R.id.Configuracion:
                opcionNiveles();
                break;
            case R.id.Puntuaciones:
                misDialogos.verResultados(this);
                break;
            default:
                return true;
        }
        return true;
    }

    /**
     * Diálogo que muestra a través de radiobuttons los diferentes niveles de juego(nº de casillas)
     */
    AlertDialog dialogo = null;
    private void opcionNiveles()
    {
        final CharSequence[] items = {" Fácil(8x8) ", " Medio(12x12) ", " Difícil(16x16)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona el nivel de dificultad:");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int item)
            {
                switch (item)
                {
                    case 0:
                        nivel = 1;
                        break;
                    case 1:
                        nivel = 2;
                        break;
                    case 2:
                        nivel = 3;
                        break;
                }
                dialogo.dismiss();
                opcionSubniveles();//después de que el usuario haya elegido el nivel, aparece el diálogo para que elija el número de minas.
            }
        });
        dialogo = builder.show();
        dialogo.show();
    }


    /**
     * Diálogo que muestra a través de radiobuttons los diferentes subniveles de juego(nº de minas)
     */
    AlertDialog dialogoSub = null;
    private void opcionSubniveles()
    {
        //Según el nivel elegido por el usuario anteriormente, aparecerá un número de minas acorde.
        CharSequence[] items = new CharSequence[0];
        if(nivel == 1)
        {
            items = new CharSequence[]{"5", "10"};
        }else
        if(nivel == 2)
        {
            items = new CharSequence[]{"15", "30"};
        }else
        if(nivel == 3)
        {
            items = new CharSequence[]{"40", "60"};
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona el número de minas :");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int item)
            {
                switch (item)
                {
                    case 0:
                        subnivel = 1;
                        break;
                    case 1:
                        subnivel = 2;
                        break;
                }
                dialogoSub.dismiss();
            }
        });
        dialogoSub = builder.show();
        dialogoSub.show();
    }



    /**
     * Limpia el layout y muestra un nuevo tablero.
     * Una vez creado se le asignan las minas a botones aleatorios y se cuentan las minas que tienen alrededor.
     * Se reinicia el cronómetro, el número de clicks y la variable númeroMinas(se le asigna el número de minas totales)
     * @param columnas
     * @param filas
     */
    public void generarTablero(int columnas, int filas)
    {
        g.removeAllViews();
        int numeroColumna = 1;
        for (int i = 0; i < (columnas * filas); i++)
        {
            b = new MisBotones(this);
            g.setColumnCount(columnas);
            g.setRowCount(filas);
            b.setId(i);
            b.setLayoutParams(new ViewGroup.LayoutParams(g.getWidth() / columnas, g.getWidth() / columnas));
            b.setBackgroundResource(R.drawable.cuadrado);
            b.setOnLongClickListener(this);
            b.setOnClickListener(this);
            if (numeroColumna > columnas)
            {
                numeroColumna = 1;
            }
            b.setNumeroColumna(numeroColumna);
            numeroColumna++;
            g.addView(b, i);
        }
        asignarMinas(minas);
        contarMinas();
        numeroMinas = minas;
        numeroClicks = 0;
        textViewMinas.setText("Minas restantes: " + numeroMinas);
        crono.setText("Total: (00:00)");
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();
    }

    /**
     * Elige botones al azar y los marca como minas a través de la propiedad 'esMina' de los botones.
     * El número de botones marcado como minas viene determinado por el nivel de juego.
     *
     * @param minas
     */
    private void asignarMinas(int minas)
    {
        int sonMina = 0;
        int n;
        do{
            n = (int) (Math.random() * (g.getColumnCount() * g.getRowCount()));
            MisBotones miBoton = (MisBotones) g.getChildAt(n);
            if(!miBoton.isEsMina())
            {
                miBoton.setEsMina(true);
                sonMina++;
            }

        }while(sonMina != minas);
    }

    //Calculamos el id de los botones que rodean de manera contigua a un boton específico.
    //Servirá para, con otro método, calcular si en las casillas que rodean un botón hay o no una mina.
    private int[] calcularCasillas(MisBotones boton) {
        int idMiBoton = boton.getId();
        int[] casillas = new int[8];
        casillas[0] = (idMiBoton - g.getRowCount()) - 1;
        casillas[1] = idMiBoton - g.getRowCount();
        casillas[2] = (idMiBoton - g.getRowCount()) + 1;
        casillas[3] = idMiBoton - 1;
        casillas[4] = idMiBoton + 1;
        casillas[5] = (idMiBoton + g.getRowCount()) - 1;
        casillas[6] = idMiBoton + g.getRowCount();
        casillas[7] = (idMiBoton + g.getRowCount()) + 1;
        return casillas;
    }

    //Recorre cada uno de los botones del grid y cada una de las casillas que lo rodean.
    private void contarMinas() {
        int[] casillas;
        for (int i = 0; i < g.getChildCount(); i++)//Bucle que recorre los botones
        {
            MisBotones boton = (MisBotones) g.getChildAt(i);
            casillas = calcularCasillas(boton);//LLamada al método que hace el cálculo de los id de las casillas que rodean el botón pasado por parámetro.
            for (int x = 0; x < casillas.length; x++)//Bucle que recorre las casillas, si la casilla es una mina aumenta en 1 la propiedad numeroDeMinas del boton.
            {
                if (casillas[x] > 0 && casillas[x] < g.getChildCount())//Si el valor es menor a 0 o mayor al número de botones, está fuera del tablero, no los consideramos.
                {
                    MisBotones b = (MisBotones) g.getChildAt(casillas[x]);
                    //Hacemos el ajuste para que la primera columna no cuente las minas de la última y viceversa y comprobamos si la casilla es una mina.
                    if (Math.abs(b.getNumeroColumna() - boton.getNumeroColumna()) != (g.getRowCount() - 1) && b.isEsMina())
                        boton.setMinasCerca(boton.getMinasCerca() + 1);
                }
            }
        }
    }

    //Asigna la imagen de un número a los botones, que especifica el número de minas que lo rodean.
    private void asignarImagenNumero(View v)
    {
        MisBotones b = (MisBotones) v;
        b.setVolteada(true);
        if (b.getMinasCerca() == 1) {
            b.setBackgroundResource(R.drawable.m1);
        }
        if (b.getMinasCerca() == 2) {
            b.setBackgroundResource(R.drawable.m2);
        }
        if (b.getMinasCerca() == 3) {
            b.setBackgroundResource(R.drawable.m3);
        }
        if (b.getMinasCerca() == 4) {
            b.setBackgroundResource(R.drawable.m4);
        }
        if (b.getMinasCerca() == 5) {
            b.setBackgroundResource(R.drawable.m5);
        }
        if (b.getMinasCerca() == 0) {
            b.setBackgroundResource(R.drawable.cero);
        }
    }

    //Controla el número de casillas descubiertas por el usuario. Si es igual el número de casillas que no son minas, el usuario gana la partida.
    private void sumarCasilla()
    {
        int casillasVolteadas = 0;
        for(int i = 0; i < g.getChildCount(); i++)
        {
            MisBotones b = (MisBotones)g.getChildAt(i);
            if(b.isVolteada())
            {
                casillasVolteadas++;
            }

            if (casillasVolteadas == (g.getRowCount() * g.getColumnCount()) - minas)
            {
                misDialogos.dialogoVictoria(this,crono,nivel,numeroClicks);
                deshabilitarBotones();
                crono.stop();
            }
        }

    }

    //Controla el número de casillas con bandera. Si es igual el número de minas que tiene el tablero, el usuario gana la partida.
    private void sumarBandera()
    {
        int casillasConBandera = 0;
        for(int i = 0; i < g.getChildCount(); i++)
        {
            MisBotones b = (MisBotones)g.getChildAt(i);
            if(b.isBandera())
            {
                casillasConBandera++;
            }

            if (casillasConBandera == minas)
            {
                misDialogos.dialogoVictoria(this, crono, nivel,numeroClicks);
                deshabilitarBotones();
                crono.stop();
                break;
            }
        }
    }

    //Deshabilita todos los botones del layout
    private void deshabilitarBotones()
    {
        for (int i = 0; i < g.getChildCount(); i++)
        {
            g.getChildAt(i).setEnabled(false);
        }
    }

    @Override
    public void onClick(View v)
    {
        numeroClicks++;
        MisBotones botonElegido = (MisBotones) v;

        if(botonElegido.isBandera())//Si la casilla tiene una bandera no se puede hacer click,el usuario deberá volver a hacer un click largo y quitarla.
        {
            return;
        }else

        if (botonElegido.isEsMina())//Si al hacer click el botón es una mina se le asigna la imagen de una mina y se hace la llamada al diálogo de fin del juego.
        {
            botonElegido.setBackgroundResource(R.drawable.mina);
            misDialogos.dialogoMinaDescubierta(this);
            deshabilitarBotones();
            crono.stop();
        } else
        {
            //Si el botón no es una mina ocurren dos cosas:
            //- El botón en el que se ha hecho click se descubre y se le asigna una imagen con el número de minas que hay a su alrededor.
            //- Las casillas alrededor del botón pulsado también se descubren si son casillas vacías(ni son mina, ni tienen asignado un nº con las minas que le rodean)
            asignarImagenNumero(botonElegido);
            int[] casillas = calcularCasillas(botonElegido);
            for (int x = 0; x < casillas.length; x++)
            {
                if (casillas[x] > 0 && casillas[x] < g.getChildCount())
                {
                    MisBotones b = (MisBotones) g.getChildAt(casillas[x]);
                    if (Math.abs(b.getNumeroColumna() - botonElegido.getNumeroColumna()) != (g.getRowCount() - 1) && !b.isEsMina() && !b.isVolteada() && (b.getMinasCerca()== 0 ))
                    {
                        asignarImagenNumero(b);
                        b.callOnClick();
                    }
                }
            }
        }
        sumarCasilla();
        botonElegido.setClickable(false);
    }

    @Override
    public boolean onLongClick(View v)
    {
        numeroClicks++;
        MisBotones botonElegido = (MisBotones) v;
        if(!botonElegido.isEsMina())//Al hacer click largo si el botón no esconde una mina, la partida termina.
        {
            misDialogos.dialogoNoEsMina(this);
            crono.stop();
            deshabilitarBotones();
        }
        else
        {
            //Si el botón ya tiene una bandera, la quitamos, si no la tiene, la ponemos,
            botonElegido.setBackgroundResource(botonElegido.isBandera() ? R.drawable.cuadrado : R.drawable.banderita);
            //Controlamos si hemos puesto o quitado la bandera para controlar en contador de minas restantes que puede ver el jugador en el textView inferior.
            if(botonElegido.isBandera())
            {
                botonElegido.setBandera(false);
                numeroMinas++;
            }
            else
            {
                botonElegido.setBandera(true);
                numeroMinas--;
            }
            sumarBandera();
            textViewMinas.setText("Minas restantes: "+numeroMinas);
        }
        return true;
    }
}

