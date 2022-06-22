package com.example.pdm_alquilermesasproyectofinal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdm_alquilermesasproyectofinal.modelos.AdaptadorMesa;
import com.example.pdm_alquilermesasproyectofinal.modelos.AdaptadorReservaciones;
import com.example.pdm_alquilermesasproyectofinal.modelos.Mesas;
import com.example.pdm_alquilermesasproyectofinal.modelos.Reservacion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class List_Reservaciones extends AppCompatActivity {

    public FirebaseDatabase database;
    public DatabaseReference referenciData;
    public ArrayList<Reservacion> arrayListReserva;
    public ListView listaReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reservaciones);

        database = FirebaseDatabase.getInstance();
        referenciData = database.getReference();

        arrayListReserva = new ArrayList<Reservacion>();
        listaReserva = findViewById(R.id.ListReserva);

        referenciData.child(MainActivity.TBL_RESERVACIONES).addValueEventListener(cargarReservaciones);

    }

    public ValueEventListener cargarReservaciones = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {

                long ahora = System.currentTimeMillis();
                Date fecha = new Date(ahora);

                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String FechaHoy = df.format(fecha);

                Log.d("Fecha ",FechaHoy);

                arrayListReserva.clear();

                for (DataSnapshot items : snapshot.getChildren()) {

                    Log.d("Fecha For",FechaHoy);

                    Reservacion reservacion = items.getValue(Reservacion.class);

                    Log.d("Fecha Reserva",reservacion.getFecha());

                    if (reservacion.getFecha().equals(FechaHoy)) {

                        Log.d("Fecha IF",FechaHoy);

                        arrayListReserva.add(reservacion);
                    }

                }
                AdaptadorReservaciones adaptadorReservaciones = new AdaptadorReservaciones(arrayListReserva,
                                                                    getApplicationContext());
                listaReserva.setAdapter(adaptadorReservaciones);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

}