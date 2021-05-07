package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityLista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
    }

    public void abrirMain(View view) {
        Intent ejemplo = new Intent(this, ActivityMain.class);
        startActivity(ejemplo);
    }

    public void abrirUsuario(View view) {
        Intent ejemplo = new Intent(this, ActivityUsuario.class);
        startActivity(ejemplo);
    }

    public void abrirPatin(View view) {
        Intent ejemplo = new Intent(this, ActivityPatin.class);
        startActivity(ejemplo);
    }
}