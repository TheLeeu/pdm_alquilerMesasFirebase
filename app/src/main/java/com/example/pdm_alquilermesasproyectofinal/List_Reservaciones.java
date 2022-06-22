package com.example.pdm_alquilermesasproyectofinal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdm_alquilermesasproyectofinal.modelos.AdaptadorMesa;
import com.example.pdm_alquilermesasproyectofinal.modelos.AdaptadorReservaciones;
import com.example.pdm_alquilermesasproyectofinal.modelos.Empleado;
import com.example.pdm_alquilermesasproyectofinal.modelos.Mesas;
import com.example.pdm_alquilermesasproyectofinal.modelos.Reservacion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth mAuth;
    public DatabaseReference referenciData;
    public ArrayList<Reservacion> arrayListReserva;
    Empleado empleado = new Empleado();
    public ListView listaReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reservaciones);

        database = FirebaseDatabase.getInstance();
        referenciData = database.getReference();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        arrayListReserva = new ArrayList<Reservacion>();
        listaReserva = findViewById(R.id.ListReserva);

        referenciData.child(MainActivity.TBL_EMPLEADOS).child(currentUser.getUid()).addValueEventListener(getEmpleado);

        referenciData.child(MainActivity.TBL_RESERVACIONES).addValueEventListener(cargarReservaciones);

    }

    public ValueEventListener getEmpleado = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                empleado = snapshot.getValue(Empleado.class);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public ValueEventListener cargarReservaciones = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {

                long ahora = System.currentTimeMillis();
                Date fecha = new Date(ahora);

                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String FechaHoy = df.format(fecha);

                arrayListReserva.clear();

                for (DataSnapshot items : snapshot.getChildren()) {

                    Reservacion reservacion = items.getValue(Reservacion.class);

                    if (reservacion.getFecha().equals(FechaHoy)) {

                        if (reservacion.getMesa().getLocal().getNombre().equals(empleado.getLocal().getNombre())){

                            Log.d("Dentro de los 2 if",FechaHoy);

                            arrayListReserva.add(reservacion);

                        }

                    }

                }
                AdaptadorReservaciones adaptadorReservaciones = new AdaptadorReservaciones(arrayListReserva, getApplicationContext());
                listaReserva.setAdapter(adaptadorReservaciones);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

}