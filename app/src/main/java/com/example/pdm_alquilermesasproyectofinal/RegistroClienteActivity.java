package com.example.pdm_alquilermesasproyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistroClienteActivity extends AppCompatActivity {
    private Button btnRegistroPrueba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);

        //METODO SOLO PARA PRESENTACION
        btnRegistroPrueba = (Button) findViewById(R.id.button2);
        btnRegistroPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registroCompleto();
            }
        });
    }

    public void registroCompleto(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void btnRegistrateCliente(View view) {
        registroCompleto();
    }
}