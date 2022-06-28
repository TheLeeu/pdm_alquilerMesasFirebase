package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pdm_alquilermesasproyectofinal.adaptadores.AdaptadorMesa;
import com.example.pdm_alquilermesasproyectofinal.modelos.Mesas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaMesasActivity extends AppCompatActivity {

    private ListView lv_mesas;
    public FirebaseDatabase database;
    public DatabaseReference referenciaData;
    private ArrayList<Mesas> listMesa;

    private int idLocal;
    private String nombreLocal;
    private String direccionLocal;
    private String telefonoLocal;
    private String coordenadasLocal;
    private String fotoLocal;

    private int itemSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mesas);

        lv_mesas = (ListView) findViewById(R.id.listHorariosAtencion);

        listMesa = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        referenciaData = database.getReference();
        referenciaData.child(MainActivity.TBL_MESAS).addValueEventListener(cargarMesas);

        if(getIntent().getStringExtra("ACTIVITY").equals("ListaLocalesActivity")) {
            idLocal = Integer.parseInt(getIntent().getStringExtra("idLocal"));
            nombreLocal = getIntent().getStringExtra("nombreLocal");
            direccionLocal = getIntent().getStringExtra("direccionLocal");
            telefonoLocal = getIntent().getStringExtra("telefonoLocal");
            coordenadasLocal = getIntent().getStringExtra("coordenadasLocal");
            fotoLocal = getIntent().getStringExtra("fotoLocal");
        }

        lv_mesas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionado = i;
                abrirDialogo();
            }
        });

    }

    public ValueEventListener cargarMesas = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                listMesa.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    Mesas mesa = items.getValue(Mesas.class);
                    if(mesa.getLocal().getIdLocal() == idLocal){
                        listMesa.add(mesa);
                    }
                }
                AdaptadorMesa ap = new AdaptadorMesa(listMesa, getApplicationContext());
                lv_mesas.setAdapter(ap);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        referenciaData.child(MainActivity.TBL_MESAS).addValueEventListener(cargarMesas);
    }

    public void btnAgregar(View view) {
        Intent intent = new Intent(this, CRUDMesasActivity.class);
        intent.putExtra("ACTIVITY", "ListaMesasActivity_agregar");
        intent.putExtra("idLocal", String.valueOf(idLocal));
        intent.putExtra("nombreLocal", nombreLocal);
        intent.putExtra("direccionLocal", direccionLocal);
        intent.putExtra("telefonoLocal", telefonoLocal);
        intent.putExtra("coordenadasLocal", coordenadasLocal);
        intent.putExtra("fotoLocal", fotoLocal);
        startActivity(intent);
    }

    public void abrirDialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione una opcion")
                .setItems(new String[]{"Modificar datos de mesa", "Rentar mesa"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Log.d("which", String.valueOf(which));
                        switch (which){
                            case 0:
                                Intent intent = new Intent(getApplicationContext(), CRUDMesasActivity.class);
                                intent.putExtra("ACTIVITY", "ListaMesasActivity_item");
                                intent.putExtra("idMesa", listMesa.get(itemSeleccionado).getIdMesa());
                                intent.putExtra("capacidadMesa", String.valueOf(listMesa.get(itemSeleccionado).getCapacidad()));
                                intent.putExtra("idEstadoMesa", String.valueOf(listMesa.get(itemSeleccionado).getEstado().getIdEstadoMesa()));
                                intent.putExtra("estadoMesa", listMesa.get(itemSeleccionado).getEstado().getEstadoMesa());
                                intent.putExtra("numeroMesa", String.valueOf(listMesa.get(itemSeleccionado).getNumeroMesa()));
                                intent.putExtra("precioMesa", String.valueOf(listMesa.get(itemSeleccionado).getPrecioReserva()));
                                intent.putExtra("fotoMesa", listMesa.get(itemSeleccionado).getFoto());

                                intent.putExtra("idLocal", String.valueOf(idLocal));
                                intent.putExtra("nombreLocal", nombreLocal);
                                intent.putExtra("direccionLocal", direccionLocal);
                                intent.putExtra("telefonoLocal", telefonoLocal);
                                intent.putExtra("coordenadasLocal", coordenadasLocal);
                                intent.putExtra("fotoLocal", fotoLocal);

                                startActivity(intent);
                                break;
                            case 1:
                                Intent i = new Intent(getApplicationContext(), CRUDReservasActivity.class);
                                i.putExtra("ACTIVITY", "ListaMesasActivity");
                                i.putExtra("idMesa", listMesa.get(itemSeleccionado).getIdMesa());
                                i.putExtra("capacidadMesa", String.valueOf(listMesa.get(itemSeleccionado).getCapacidad()));
                                i.putExtra("idEstadoMesa", String.valueOf(listMesa.get(itemSeleccionado).getEstado().getIdEstadoMesa()));
                                i.putExtra("estadoMesa", listMesa.get(itemSeleccionado).getEstado().getEstadoMesa());
                                i.putExtra("numeroMesa", String.valueOf(listMesa.get(itemSeleccionado).getNumeroMesa()));
                                i.putExtra("precioMesa", String.valueOf(listMesa.get(itemSeleccionado).getPrecioReserva()));
                                i.putExtra("fotoMesa", listMesa.get(itemSeleccionado).getFoto());

                                i.putExtra("idLocal", String.valueOf(idLocal));
                                i.putExtra("nombreLocal", nombreLocal);
                                i.putExtra("direccionLocal", direccionLocal);
                                i.putExtra("telefonoLocal", telefonoLocal);
                                i.putExtra("coordenadasLocal", coordenadasLocal);
                                i.putExtra("fotoLocal", fotoLocal);
                                startActivity(i);
                                break;
                        }
                    }
                }).show();
    }
}