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
        String nomUsuario = obtenerUsuario();
        if (nomUsuario.equals("")) {
            Intent login = new Intent(this, ActivityLogin.class);
            startActivity(login);
        } else {
            Intent usuario = new Intent(this, ActivityUsuario.class);
            startActivity(usuario);
        }
    }

    public String obtenerUsuario() {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE, MODE_PRIVATE);
        return preferences.getString("usuario", "");
    }
}