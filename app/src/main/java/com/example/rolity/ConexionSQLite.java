package com.example.rolity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLite extends SQLiteOpenHelper {
    private final String CREATE_TABLE_USUARIOS = "CREATE TABLE usuarios (nom_usuario TEXT PRIMARY KEY, contrasenia TEXT, email TEXT, nombre TEXT, apellidos TEXT, tlf TEXT, pais TEXT, CP TEXT, provincia TEXT, localidad TEXT, direccion TEXT)";
    private final String CREATE_TABLE_PATINES = "CREATE TABLE patines (modelo TEXT PRIMARY KEY, imagen TEXT, email TEXT, nombre TEXT, apellidos TEXT, tlf TEXT, pais TEXT, CP TEXT, provincia TEXT, localidad TEXT, direccion TEXT)";

    public ConexionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USUARIOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
