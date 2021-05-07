package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityUsuario extends AppCompatActivity {
    private static final String STRING_PREFERENCE = "ROLITY";
    private ArrayList<EditText> eCampos;
    private ArrayList<String> campos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        String nomUsuario = obtenerUsuario();
        Cursor fila = Modelo.leerUsuario(this, nomUsuario);
        ArrayList<EditText> editTexts = cargarEditTexts();

        if (fila.moveToFirst()) {
            for (int i = 0; i < fila.getColumnCount(); i++) {
                editTexts.get(i).setText(fila.getString(i));
            }
        }
        
        Button btnGuardarDatos = findViewById(R.id.botonGuardarDatos);
        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatosUsuario();
            }
        });

        Button btnCerrarSesion = findViewById(R.id.botonCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE, MODE_PRIVATE);
                preferences.edit().putString("usuario", "").apply();
                preferences.edit().putBoolean("sesion",false).apply();
                Intent login = new Intent(ActivityUsuario.this, ActivityLogin.class);
                startActivity(login);
                finish();
            }
        });

    }

    private void guardarDatosUsuario() {
        UsuarioDTO usr = new UsuarioDTO();

        eCampos = cargarEditTexts();
        campos = new ArrayList<>();

        boolean error;
        error = comprobarCampos();
        if (!error) {
            error = comprobarUsuario();
            if (!error) {
                error = comprobarEmail();
                if (!error) {
                    usr.setNom_usuario(campos.get(0));
                    usr.setContrasenia(campos.get(1));
                    usr.setEmail(campos.get(2));
                    usr.setNombre(campos.get(3));
                    usr.setApellidos(campos.get(4));
                    usr.setTlf(campos.get(5));
                    usr.setPais(campos.get(6));
                    usr.setCP(campos.get(7));
                    usr.setProvincia(campos.get(8));
                    usr.setLocalidad(campos.get(9));
                    usr.setDireccion(campos.get(10));

                    int resUpdate = Modelo.modificarUsuario(ActivityUsuario.this, usr);

                    if (resUpdate == 1) {
                        Toast.makeText(ActivityUsuario.this, "Usuario actualizado!",  Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(ActivityUsuario.this, "Error",  Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean comprobarCampos() {
        for (int i = 0; i < eCampos.size(); i++) {
            if (eCampos.get(i).getText().toString().isEmpty()) {
                Toast.makeText(ActivityUsuario.this,"Completa todos los campos!", Toast.LENGTH_SHORT).show();
                return true;
            }
            campos.add(eCampos.get(i).getText().toString());
        }
        return false;
    }

    private boolean comprobarUsuario() {
        Cursor fila = Modelo.leerUsuario(ActivityUsuario.this, campos.get(0).trim());

        if (fila.moveToFirst()) {
            Toast.makeText(ActivityUsuario.this,"Este nombre de usuario ya existe!", Toast.LENGTH_SHORT).show();
            return true;
        }

        else
            return false;
    }

    private boolean comprobarEmail() {
        Cursor fila = Modelo.leerCorreo(ActivityUsuario.this, campos.get(2).trim());

        if (fila.moveToFirst()) {
            Toast.makeText(ActivityUsuario.this,"Este email ya está en uso!", Toast.LENGTH_SHORT).show();
            return true;
        }

        else
            return false;
    }

    public String obtenerUsuario() {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE, MODE_PRIVATE);
        return preferences.getString("usuario", "");
    }

    private ArrayList<EditText> cargarEditTexts() {
        ArrayList<EditText> editTexts = new ArrayList<>();

        editTexts.add(findViewById(R.id.logedUsuario));
        editTexts.add(findViewById(R.id.logedContrasenia));
        editTexts.add(findViewById(R.id.logedEmail));
        editTexts.add(findViewById(R.id.logedNombre));
        editTexts.add(findViewById(R.id.logedApellidos));
        editTexts.add(findViewById(R.id.logedTlf));
        editTexts.add(findViewById(R.id.logedPais));
        editTexts.add(findViewById(R.id.logedCp));
        editTexts.add(findViewById(R.id.logedProvincia));
        editTexts.add(findViewById(R.id.logedLocalidad));
        editTexts.add(findViewById(R.id.logedDireccion));

        return editTexts;
    }

    public void abrirMain(View view) {
        Intent ejemplo = new Intent(this, ActivityMain.class);
        startActivity(ejemplo);
    }

    public void abrirLista(View view) {
        Intent ejemplo = new Intent(this, ActivityLista.class);
        startActivity(ejemplo);
    }
}