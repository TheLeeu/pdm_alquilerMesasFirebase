package com.example.pdm_alquilermesasproyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ListaReservasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reservas);
    }

    public void btnAgregarReserva(View view) {
        cargarCRUDReservasActivity();
    }

    public void cargarCRUDReservasActivity(){
        Intent intent = new Intent(this, CRUDReservasActivity.class);
        startActivity(intent);
    }
}