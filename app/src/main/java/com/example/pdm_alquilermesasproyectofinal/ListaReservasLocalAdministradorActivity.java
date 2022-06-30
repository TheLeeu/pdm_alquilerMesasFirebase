package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.pdm_alquilermesasproyectofinal.adaptadores.AdaptadorEmpleado;
import com.example.pdm_alquilermesasproyectofinal.adaptadores.AdaptadorReservas;
import com.example.pdm_alquilermesasproyectofinal.modelos.Empleado;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.example.pdm_alquilermesasproyectofinal.modelos.Reservacion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaReservasLocalAdministradorActivity extends AppCompatActivity {

    ListView lv_reservas;
    private ArrayList<Reservacion> listReservas;
    public FirebaseDatabase database;
    public DatabaseReference referenciaData;
    private Local local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reservas_local_administrador);

        lv_reservas = (ListView) findViewById(R.id.lvReservasAdministrador);

        listReservas = new ArrayList<Reservacion>();
        database = FirebaseDatabase.getInstance();
        referenciaData = database.getReference();
        referenciaData.child(MainActivity.TBL_RESERVACIONES).addValueEventListener(cargarReservaciones);


        if(getIntent().getStringExtra("ACTIVITY").equals("ListaLocalesActivity") ||
        getIntent().getStringExtra("ACTIVITY").equals("ListaMesasActivity")) {
            local = new Local(Integer.parseInt(getIntent().getStringExtra("idLocal")),
                    getIntent().getStringExtra("nombreLocal"),
                    getIntent().getStringExtra("direccionLocal"),
                    getIntent().getStringExtra("telefonoLocal"),
                    getIntent().getStringExtra("coordenadasLocal"),
                    getIntent().getStringExtra("fotoLocal"));
        }
    }

    public ValueEventListener cargarReservaciones = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                listReservas.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    Reservacion reserva = items.getValue(Reservacion.class);
                    if(reserva.getMesa().getLocal().getIdLocal() == local.getIdLocal()) {
                        if(getIntent().getStringExtra("ACTIVITY").equals("ListaMesasActivity")){
                            if(getIntent().getStringExtra("idMesa").equals(reserva.getMesa().getIdMesa().toString())){
                                listReservas.add(reserva);
                            }
                        }else{
                            listReservas.add(reserva);
                        }

                    }
                }
                AdaptadorReservas ap = new AdaptadorReservas(listReservas, getApplicationContext());
                lv_reservas.setAdapter(ap);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void btnAgregarReserva(View view) {
    }
}