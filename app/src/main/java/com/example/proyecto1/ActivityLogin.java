package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class ActivityLogin extends AppCompatActivity {
    private boolean isSesionChecked;
    private static final String STRING_PREFERENCE = "ROLITY";

    private Button btnRegistro;
    private Button btnInicioSesion;
    private RadioButton btnSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnRegistro = findViewById(R.id.botonRegistro);
        btnInicioSesion = findViewById(R.id.botonEntrar);
        btnSesion = findViewById(R.id.botonSesion);

        isSesionChecked = btnSesion.isChecked();

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro = new Intent(ActivityLogin.this, ActivityRegistro.class);
                startActivity(registro);
                finish();
            }
        });

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eUsuario = findViewById(R.id.sesionUsuario);
                EditText eContrasenia = findViewById(R.id.sesionContrasenia);
                String usuario = eUsuario.getText().toString();
                String contrasenia = eContrasenia.getText().toString();
                 
                Cursor fila = Modelo.leerUsuario(ActivityLogin.this, usuario);


                if (fila.moveToFirst()) {
                    if (contrasenia.equals(fila.getString(1))) {
                        setLogeado(usuario);
                        guardarEstadoSesion();
                        guardarUsuario(usuario);
                        Intent login = new Intent(ActivityLogin.this, ActivityUsuario.class);
                        startActivity(login);
                        finish();
                    } else
                        Toast.makeText(ActivityLogin.this, "Contraseña incorrecta!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ActivityLogin.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            }
        });

        btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSesionChecked)
                    btnSesion.setChecked(false);

                isSesionChecked = btnSesion.isChecked();
            }
        });
    }

    private void guardarUsuario(String usuario) {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE, MODE_PRIVATE);
        preferences.edit().putString("usuario", usuario).apply();
    }

    public void guardarEstadoSesion() {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE, MODE_PRIVATE);
        preferences.edit().putBoolean("sesion",btnSesion.isChecked()).apply();
    }

    private void setLogeado(String usuario) {
        ((App) this.getApplication()).setLogeado(true);
        ((App) this.getApplication()).setNomUsuario(usuario);
    }

    public void abrirMain(View view) {
        Intent ejemplo = new Intent(this, ActivityMain.class);
        startActivity(ejemplo);
    }
}