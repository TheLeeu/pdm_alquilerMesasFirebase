package com.example.pdm_alquilermesasproyectofinal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdm_alquilermesasproyectofinal.adaptadores.AdaptadorReservaciones;
import com.example.pdm_alquilermesasproyectofinal.modelos.Empleado;
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
import java.util.Calendar;
import java.util.Date;

public class List_Reservaciones extends AppCompatActivity {

    public FirebaseDatabase database;
    private FirebaseAuth mAuth;
    public DatabaseReference referenciData;
    public FirebaseUser currentUser;
    public ArrayList<Reservacion> arrayListReserva;
    Empleado empleado = new Empleado();
    public ListView listaReserva;
    private int hora;
    private int minuto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reservaciones);

        database = FirebaseDatabase.getInstance();
        referenciData = database.getReference();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        arrayListReserva = new ArrayList<Reservacion>();
        listaReserva = findViewById(R.id.ListReserva);

        if(getIntent().getStringExtra("ACTIVITY").equals("EmpleadoMainActivity")) {
            referenciData.child(MainActivity.TBL_EMPLEADOS).child(currentUser.getUid()).addValueEventListener(getEmpleado);

            referenciData.child(MainActivity.TBL_RESERVACIONES).addValueEventListener(cargarReservaciones);
        }

        if(getIntent().getStringExtra("ACTIVITY").equals("ClienteMainActivity")) {
            referenciData.child(MainActivity.TBL_RESERVACIONES).addValueEventListener(cargarReservacionesUsuario);
        }

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

    public final ValueEventListener cargarReservaciones = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {

                long ahora = System.currentTimeMillis();
                Calendar calendario = Calendar.getInstance();
                calendario.setTimeInMillis(ahora);
                hora = calendario.get(Calendar.HOUR_OF_DAY);
                minuto = calendario.get(Calendar.MINUTE);

                String h = "";
                String m = "";
                if (hora < 10) {
                    h = "0" + hora;
                } else {
                    h = String.valueOf(hora);
                }
                if ((minuto + 1) < 10) {
                    m = "0" + (minuto + 1);
                } else {
                    m = String.valueOf(minuto + 1);
                }

                String HoraActual = h + ":" + m;
                String HoraSalida;

                Date fecha = new Date(ahora);

                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String FechaHoy = df.format(fecha);

                arrayListReserva.clear();

                for (DataSnapshot items : snapshot.getChildren()) {

                    Reservacion reservacion = items.getValue(Reservacion.class);

                    if (reservacion.getMesa().getLocal().getNombre().equals(empleado.getLocal().getNombre())) {

                        if (reservacion.getFecha().equals(FechaHoy)) {

                            HoraSalida = reservacion.getHoraSalida();
                            String[] parts = HoraSalida.split(":");
                            String HoSa = parts[0];
                            String MiSa = parts[1];

                            parts = HoraActual.split(":");
                            String HoAc = parts[0];
                            String MiAc = parts[1];

                            Log.d("Hora Salida", String.valueOf(Integer.parseInt(HoSa)));
                            Log.d("Hora Mi Salida", String.valueOf(Integer.parseInt(MiSa)));

                            Log.d("Hora Actual", String.valueOf(Integer.parseInt(HoAc)));
                            Log.d("Hora Mi Actual", String.valueOf(Integer.parseInt(MiAc)));


                            if (Integer.parseInt(HoSa) > Integer.parseInt(HoAc)) {

                                Log.d("Pasa Ho", "");

                                arrayListReserva.add(reservacion);

                            }
                            else if (Integer.parseInt(HoSa) == Integer.parseInt(HoAc)){

                                if (Integer.parseInt(MiSa) >= Integer.parseInt(MiAc)) {

                                    Log.d("Pasa Min", "");

                                    arrayListReserva.add(reservacion);
                                }

                            }
                            else{

                                referenciData.child(MainActivity.TBL_RESERVACIONES).child(String.valueOf(reservacion.getIdReservacion())).removeValue();

                            }

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

    public final ValueEventListener cargarReservacionesUsuario = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {

                arrayListReserva.clear();

                for (DataSnapshot items : snapshot.getChildren()) {

                    Reservacion reservacion = items.getValue(Reservacion.class);

                    if (reservacion.getPago().getUsuario().getIdUsuario().equals(currentUser.getUid())) {

                        arrayListReserva.add(reservacion);

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