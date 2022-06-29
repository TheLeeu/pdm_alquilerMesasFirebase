package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.pdm_alquilermesasproyectofinal.adaptadores.AdaptadorEmpleado;
import com.example.pdm_alquilermesasproyectofinal.modelos.Empleado;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaEmpleadosActivity extends AppCompatActivity {

    ListView lv_empleados;
    private ArrayList<Empleado> listEmpleados;
    public FirebaseDatabase database;
    public DatabaseReference referenciaData;
    private Local local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_empleados);

        lv_empleados = (ListView) findViewById(R.id.lvReservasAdministrador);

        listEmpleados = new ArrayList<Empleado>();
        database = FirebaseDatabase.getInstance();
        referenciaData = database.getReference();
        referenciaData.child(MainActivity.TBL_EMPLEADOS).addValueEventListener(cargarEmpleados);

        if(getIntent().getStringExtra("ACTIVITY").equals("ListaLocalesActivity")) {
            local = new Local(Integer.parseInt(getIntent().getStringExtra("idLocal")),
                    getIntent().getStringExtra("nombreLocal"),
                    getIntent().getStringExtra("direccionLocal"),
                    getIntent().getStringExtra("telefonoLocal"),
                    getIntent().getStringExtra("coordenadasLocal"),
                    getIntent().getStringExtra("fotoLocal"));
        }

        /*lv_empleados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), RegistroUsuariosAdministradoActivity.class);
                intent.putExtra("ACTIVITY", "ListaEmpeladosActivity item");
                intent.putExtra("idLocal", String.valueOf(local.getIdLocal()));
                intent.putExtra("nombreLocal", local.getNombre());
                intent.putExtra("direccionLocal", local.getDireccion());
                intent.putExtra("telefonoLocal", local.getTelefono());
                intent.putExtra("coordenadasLocal", local.getCoordenadasGps());
                intent.putExtra("fotoLocal", local.getFoto());
                startActivity(intent);
            }
        });*/


    }

    public ValueEventListener cargarEmpleados = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                listEmpleados.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    Empleado empleado = items.getValue(Empleado.class);
                    if(empleado.getLocal().getIdLocal() == local.getIdLocal()) {
                        listEmpleados.add(empleado);
                    }
                }
                AdaptadorEmpleado ap = new AdaptadorEmpleado(listEmpleados, getApplicationContext());
                lv_empleados.setAdapter(ap);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void btnAgregarReserva(View view) {
        Intent intent = new Intent(this, RegistroUsuariosAdministradoActivity.class);
        intent.putExtra("ACTIVITY", "ListaEmpeladosActivity btn");
        intent.putExtra("idLocal", String.valueOf(local.getIdLocal()));
        intent.putExtra("nombreLocal", local.getNombre());
        intent.putExtra("direccionLocal", local.getDireccion());
        intent.putExtra("telefonoLocal", local.getTelefono());
        intent.putExtra("coordenadasLocal", local.getCoordenadasGps());
        intent.putExtra("fotoLocal", local.getFoto());
        startActivity(intent);
    }
}