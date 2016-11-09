package com.example.crisrodfe.buscaminas.MisClases;

import android.content.Context;
import android.widget.Button;

/**
 * Created by CrisRodFe
 *
 * Clase que deriva de Button.
 * Tiene propiedades propias para indicar:
 * - si el botón es o no una mina.
 * - el número de minas que tiene alrededor
 * - el número de columna en el que está colocado en el layout
 * - si la casilla que ocupa ha sido o no volteada
 * - si el usuario le ha colocado o no una bandera
 */
public class MisBotones extends Button
{
    private boolean esMina = false;
    private int minasCerca = 0;
    private int numeroColumna = 0;
    private boolean volteada = false;
    private boolean bandera = false;

    /**
     * Constructor de la clase.
     * @param context
     */
    public MisBotones(Context context)
    {
        super(context);
    }

    /**
     *  Getters y Setters de las propiedades
     */
    public boolean isBandera() {
        return bandera;
    }

    public void setBandera(boolean bandera) {
        this.bandera = bandera;
    }

    public boolean isVolteada()
    {
        return volteada;
    }

    public void setVolteada(boolean volteada)
    {
        this.volteada = volteada;
    }

    public boolean isEsMina() {
        return esMina;
    }

    public void setEsMina(boolean esMina) {
        this.esMina = esMina;
    }

    public int getMinasCerca() {
        return minasCerca;
    }

    public void setMinasCerca(int minasCerca) {
        this.minasCerca = minasCerca;
    }

    public int getNumeroColumna() {
        return numeroColumna;
    }

    public void setNumeroColumna(int numeroColumna) {
        this.numeroColumna = numeroColumna;
    }
}
