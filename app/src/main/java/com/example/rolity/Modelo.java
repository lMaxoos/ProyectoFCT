package com.example.rolity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Modelo {

    private Modelo() {}

    public static SQLiteDatabase getConn(Context context) {
        ConexionSQLite conn = new ConexionSQLite(context, "dbusuarios", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        return db;
    }

    public static int insertarUsuario(Context context, UsuarioDTO usuario) {
        int res = 0;
        String insertSql = "INSERT INTO usuarios (nom_usuario, contrasenia, email, nombre, apellidos, tlf, pais, CP , provincia, localidad, direccion) " +
                "VALUES ('"+usuario.getNom_usuario()+"', '"+usuario.getContrasenia()+"', '"+usuario.getEmail()+"', '"+usuario.getNombre()+"' , " +
                "'"+usuario.getApellidos()+"' , '"+usuario.getTlf()+"' , '"+usuario.getPais()+"' , '"+usuario.getCP()+"' , '"+usuario.getProvincia()+"', '"+usuario.getLocalidad()+"' , '"+usuario.getDireccion()+"')";

        SQLiteDatabase db = getConn(context);
        try {
            db.execSQL(insertSql);
            res = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static Cursor leerUsuario(Context context, String nomUsuario) {
        String sql = "SELECT * FROM usuarios WHERE nom_usuario = '"+nomUsuario+"'";

        SQLiteDatabase db = getConn(context);
        Cursor fila = db.rawQuery(sql, null);

        return fila;
    }

    public static Cursor leerCorreo(Context context, String correo) {
        String sql = "SELECT * FROM usuarios WHERE email = '"+correo+"'";

        SQLiteDatabase db = getConn(context);
        Cursor fila = db.rawQuery(sql, null);

        return fila;
    }

    public static int modificarUsuario(Context context, UsuarioDTO usuario) {
        int res = 0;
        String update = "UPDATE usuarios " +
                "SET nom_usuario = '"+usuario.getNom_usuario()+"', contrasenia = '"+usuario.getContrasenia()+"', email = '"+usuario.getEmail()+"', nombre = '"+usuario.getNombre()+"', apellidos = '"+usuario.getApellidos()+"', " +
                "tlf = '"+usuario.getTlf()+"', pais = '"+usuario.getPais()+"', CP = '"+usuario.getCP()+"', provincia = '"+usuario.getProvincia()+"', localidad = '"+usuario.getLocalidad()+"', direccion = '"+usuario.getDireccion()+"' " +
                "WHERE nom_usuario = '"+usuario.getNom_usuario()+"'";

        SQLiteDatabase db = getConn(context);
        try {
            db.execSQL(update);
            res = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

}
