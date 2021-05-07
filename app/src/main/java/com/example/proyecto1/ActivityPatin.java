package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityPatin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patin);
    }

    public void abrirMain(View view) {
        Intent ejemplo = new Intent(this, ActivityMain.class);
        startActivity(ejemplo);
    }

    public void abrirUsuario(View view) {
        boolean logeado = ((App) this.getApplication()).isLogeado();
        if (logeado) {
            Intent login = new Intent(this, ActivityUsuario.class);
            startActivity(login);
        } else {
            Intent usuario = new Intent(this, ActivityLogin.class);
            startActivity(usuario);
        }

    }
}