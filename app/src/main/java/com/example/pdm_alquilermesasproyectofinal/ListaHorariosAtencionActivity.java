package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.pdm_alquilermesasproyectofinal.adaptadores.AdaptadorHorarioAtencion;
import com.example.pdm_alquilermesasproyectofinal.modelos.AdaptadorMesa;
import com.example.pdm_alquilermesasproyectofinal.modelos.HorarioAtencion;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.example.pdm_alquilermesasproyectofinal.modelos.Mesas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaHorariosAtencionActivity extends AppCompatActivity {

    private ListView lv_horarios;
    public FirebaseDatabase database;
    public DatabaseReference referenciaData;
    private ArrayList<HorarioAtencion> listHorario;
    private Local local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_horarios_atencion);

        lv_horarios = (ListView) findViewById(R.id.listHorariosAtencion);

        listHorario = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        referenciaData = database.getReference();
        referenciaData.child(MainActivity.TBL_HORARIOS_ATENCION).addValueEventListener(cargarHorarios);

        local = new Local(Integer.parseInt(getIntent().getStringExtra("idLocal")),
                getIntent().getStringExtra("nombreLocal"),
                getIntent().getStringExtra("direccionLocal"),
                getIntent().getStringExtra("telefonoLocal"),
                getIntent().getStringExtra("coordenadasLocal"),
                getIntent().getStringExtra("fotoLocal"));

        lv_horarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CRUDHorariosAtencionActivity.class);
                intent.putExtra("ACTIVITY", "ListaHorariosAtencionActivity item");

                intent.putExtra("idHorario", String.valueOf(listHorario.get(i).getIdHorarioAtencion()));
                intent.putExtra("dia", listHorario.get(i).getDia());
                intent.putExtra("HoraApertura", listHorario.get(i).getHoraApertura());
                intent.putExtra("HoraCierre", listHorario.get(i).getHoraCierre());

                intent.putExtra("idLocal", String.valueOf(local.getIdLocal()));
                intent.putExtra("nombreLocal", local.getNombre());
                intent.putExtra("direccionLocal", local.getDireccion());
                intent.putExtra("telefonoLocal", local.getTelefono());
                intent.putExtra("coordenadasLocal", local.getCoordenadasGps());
                intent.putExtra("fotoLocal", local.getFoto());
                startActivity(intent);
            }
        });

    }

    public ValueEventListener cargarHorarios = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                listHorario.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    HorarioAtencion horarioA = items.getValue(HorarioAtencion.class);
                    if(horarioA.getLocal().getIdLocal() == local.getIdLocal()){
                        listHorario.add(horarioA);
                    }
                }
                AdaptadorHorarioAtencion ap = new AdaptadorHorarioAtencion(listHorario, getApplicationContext());
                lv_horarios.setAdapter(ap);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        referenciaData.child(MainActivity.TBL_HORARIOS_ATENCION).addValueEventListener(cargarHorarios);
    }

    public void btnAgregar(View view) {
        Intent intent = new Intent(this, CRUDHorariosAtencionActivity.class);
        intent.putExtra("ACTIVITY", "ListaHorariosAtencionActivity btnAgregar");
        intent.putExtra("idLocal", String.valueOf(local.getIdLocal()));
        intent.putExtra("nombreLocal", local.getNombre());
        intent.putExtra("direccionLocal", local.getDireccion());
        intent.putExtra("telefonoLocal", local.getTelefono());
        intent.putExtra("coordenadasLocal", local.getCoordenadasGps());
        intent.putExtra("fotoLocal", local.getFoto());
        startActivity(intent);
    }
}