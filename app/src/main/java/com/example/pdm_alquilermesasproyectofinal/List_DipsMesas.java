package com.example.pdm_alquilermesasproyectofinal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdm_alquilermesasproyectofinal.modelos.AdaptadorMesa;
import com.example.pdm_alquilermesasproyectofinal.modelos.Empleado;
import com.example.pdm_alquilermesasproyectofinal.modelos.EstadoMesa;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.example.pdm_alquilermesasproyectofinal.modelos.Mesas;
import com.example.pdm_alquilermesasproyectofinal.modelos.Usuario;
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
    public ListView listaMesa;
    public String LocalEmp = "";

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

        /*  CAMBIO DE MESA A OCUPADA
        listaMesa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                EstadoMesa estado = new EstadoMesa();
                Mesas modelmesa = arrayListMesa.get(i);

                estado.setIdEstadoMesa(1);
                estado.setEstadoMesa("Ocupado");

                referenciData.child(MainActivity.TBL_MESAS).child(modelmesa.getIdMesa()).child("estado").setValue(estado);

                referenciData.child(MainActivity.TBL_MESAS).addValueEventListener(cargarMesas);

            }
        });


         */
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
                    Mesas mesa = items.getValue(Mesas.class);

                    Log.d("Local Empleado ", empleado.getLocal().getNombre());

                    if (mesa.getLocal().getNombre().equals(empleado.getLocal().getNombre())){

                        if (mesa.getEstado().getEstadoMesa().equals(MainActivity.ESTADO_MESA_DISPONIBLE.getEstadoMesa())) {
                            arrayListMesa.add(mesa);
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