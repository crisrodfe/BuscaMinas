package com.example.crisrodfe.buscaminas.MisClases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by CrisRodFe.
 *
 * Clase que crea una base de datos SQLite en el dispositivo del usuario para almacenar las puntuaciones de los jugadores de manera local.
 */
public class MiBBDD extends SQLiteOpenHelper
{
    //Variables de la bd,tabla y columnas.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "puntuaciones.db";
    private static final String TABLE_PUNTUACIONES = "puntuaciones";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_JUGADOR = "jugador";
    public static final String COLUMN_TIEMPO = "tiempo";
    public static final String COLUMN_PUNTOS = "puntos";


    public MiBBDD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //Creación de la tabla.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +TABLE_PUNTUACIONES + "("
                                        + COLUMN_ID + " INTEGER PRIMARY KEY,"
                                        + COLUMN_JUGADOR+ " TEXT,"
                                        + COLUMN_TIEMPO+ " TEXT, "
                                        + COLUMN_PUNTOS + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUNTUACIONES);
        onCreate(db);
    }

    //Añadirá un nuevo registro a la tabla de puntuaciones.
    public void addJugador(String jugador, String tiempo, int puntos)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_JUGADOR, jugador);
        values.put(COLUMN_TIEMPO, tiempo);
        values.put(COLUMN_PUNTOS, puntos);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PUNTUACIONES, null, values);
    }

    /**
     * Devuelve una cadena con formato con una cabecera y todos los registros de la tabla de puntuaciones.
     *
     * @return String
     */
    public String verPuntuaciones()
    {
        String query = "Select * FROM " + TABLE_PUNTUACIONES +" ORDER BY "+COLUMN_PUNTOS+" DESC";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);


        cursor.moveToFirst();
        String formato = "%-8s%-8s%-8s\n";
        String jugador = String.format(formato," NOMBRE","TIEMPO","PUNTOS");
        if(cursor.getCount() > 0) {//Si no devuelve ningún registro solo aparecerá la cabecera de las columnas, si devuelve algún registro, se recorre ek cursor.
            do {
                jugador += String.format(formato, cursor.getString(1), cursor.getString(2), cursor.getString(3));
            } while (cursor.moveToNext());
        }
        return jugador;
    }
}

