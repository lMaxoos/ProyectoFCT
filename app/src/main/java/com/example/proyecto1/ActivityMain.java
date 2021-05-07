package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class ActivityMain extends AppCompatActivity  {
    private static final String STRING_PREFERENCE = "ROLITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void abrirPatin(View view) {
        Intent ejemplo = new Intent(this, ActivityPatin.class);
        startActivity(ejemplo);
    }

    public void abrirUsuario(View view) {
        boolean logeado = obtenerEstadoSesion();
        if (logeado) {
            Intent login = new Intent(this, ActivityUsuario.class);
            startActivity(login);
        } else {
            Intent usuario = new Intent(this, ActivityLogin.class);
            startActivity(usuario);
        }
    }

    public boolean obtenerEstadoSesion() {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE, MODE_PRIVATE);
        return preferences.getBoolean("sesion", false);
    }
}