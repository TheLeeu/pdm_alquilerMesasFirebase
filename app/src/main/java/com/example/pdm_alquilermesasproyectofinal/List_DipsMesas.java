package com.example.pdm_alquilermesasproyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdm_alquilermesasproyectofinal.adaptadores.AdaptadorMesa;
import com.example.pdm_alquilermesasproyectofinal.modelos.Empleado;
import com.example.pdm_alquilermesasproyectofinal.modelos.Mesas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class List_DipsMesas extends AppCompatActivity {

    public FirebaseDatabase database;
    private FirebaseAuth mAuth;
    public DatabaseReference referenciData;
    public ArrayList<Mesas> arrayListMesa;
    Empleado empleado = new Empleado();
    Mesas mesas = new Mesas();
    public ListView listaMesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dips_mesas);

        database = FirebaseDatabase.getInstance();
        referenciData = database.getReference();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        arrayListMesa = new ArrayList<Mesas>();
        listaMesa = findViewById(R.id.ListMesa);

        referenciData.child(MainActivity.TBL_EMPLEADOS).child(currentUser.getUid()).addValueEventListener(getEmpleado);

        referenciData.child(MainActivity.TBL_MESAS).addValueEventListener(cargarMesas);

        listaMesa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("Nom Local", arrayListMesa.get(i).getLocal().getNombre());


                Intent iten = new Intent(getApplicationContext(), CRUDReservasActivity.class);
                iten.putExtra("ACTIVITY", "List_DipsMesas");
                iten.putExtra("idMesa", arrayListMesa.get(i).getIdMesa());
                iten.putExtra("capacidadMesa", String.valueOf(arrayListMesa.get(i).getCapacidad()));
                iten.putExtra("idEstadoMesa", String.valueOf(arrayListMesa.get(i).getEstado().getIdEstadoMesa()));
                iten.putExtra("estadoMesa", arrayListMesa.get(i).getEstado().getEstadoMesa());
                iten.putExtra("numeroMesa", String.valueOf(arrayListMesa.get(i).getNumeroMesa()));
                iten.putExtra("precioMesa", String.valueOf(arrayListMesa.get(i).getPrecioReserva()));
                iten.putExtra("fotoMesa", arrayListMesa.get(i).getFoto());

                iten.putExtra("idLocal", String.valueOf(arrayListMesa.get(i).getLocal().getIdLocal()));
                iten.putExtra("nombreLocal", arrayListMesa.get(i).getLocal().getNombre());
                iten.putExtra("direccionLocal", arrayListMesa.get(i).getLocal().getDireccion());
                iten.putExtra("telefonoLocal", arrayListMesa.get(i).getLocal().getTelefono());
                iten.putExtra("coordenadasLocal", arrayListMesa.get(i).getLocal().getCoordenadasGps());
                iten.putExtra("fotoLocal", arrayListMesa.get(i).getLocal().getFoto());
                startActivity(iten);

            }
        });

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

    public ValueEventListener cargarMesas = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {

                arrayListMesa.clear();

                for (DataSnapshot items : snapshot.getChildren()) {
                    mesas = items.getValue(Mesas.class);

                    if (mesas.getLocal().getNombre().equals(empleado.getLocal().getNombre())) {

                        if (mesas.getEstado().getEstadoMesa().equals(MainActivity.ESTADO_MESA_DISPONIBLE.getEstadoMesa())) {

                            arrayListMesa.add(mesas);
                        }

                    }

                }
                AdaptadorMesa adaptadorMesa = new AdaptadorMesa(arrayListMesa, getApplicationContext());
                listaMesa.setAdapter(adaptadorMesa);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}