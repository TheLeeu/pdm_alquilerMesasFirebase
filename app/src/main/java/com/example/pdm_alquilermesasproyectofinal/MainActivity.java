package com.example.pdm_alquilermesasproyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cargarLoginActivity();
    }

    public void cargarLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void cargarClienteMainActivity(){
        Intent intent = new Intent(this, ClienteMainActivity.class);
        startActivity(intent);
    }

    public void cargarAdministradorMainActivity(){
        Intent intent = new Intent(this, AdministradorMainActivity.class);
        startActivity(intent);
    }
}