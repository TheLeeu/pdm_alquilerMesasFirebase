package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.pdm_alquilermesasproyectofinal.modelos.EstadoUsuario;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.example.pdm_alquilermesasproyectofinal.modelos.Mesas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CRUDReservasActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private long id = 0;
    private ArrayList<Local> listLocales;
    private ArrayList<Mesas> listMesas;
    private Spinner sp_locales, sp_mesas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudreservas);

        sp_locales = (Spinner)findViewById(R.id.spLocalesCRUDReservasActivity);
        sp_mesas = (Spinner)findViewById(R.id.spMesasCRUDReservasActivity);

        listLocales = new ArrayList<>();
        listMesas = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child(MainActivity.TBL_LOCALES).addValueEventListener(cargarLocales);
        ref.child(MainActivity.TBL_MESAS).addValueEventListener(cargarMesas);

        //PARA CAPTURAR CUANTAS RESERVACIONES SE HAN HECHO Y TOMAR EL VALOR DEL ID PARA EL NUEVO REGISTRO
        ref.child(MainActivity.TBL_RESERVACIONES);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    id = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public ValueEventListener cargarLocales = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                for (DataSnapshot items: snapshot.getChildren()){
                    Local local = items.getValue(Local.class);
                    listLocales.add(local);
                }
                ArrayAdapter<Local> adapter = new ArrayAdapter<>(CRUDReservasActivity.this, android.R.layout.simple_dropdown_item_1line, listLocales);
                sp_locales.setAdapter(adapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d("onCancellerLocales",error.toString());
        }
    };

    public ValueEventListener cargarMesas = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                for (DataSnapshot items: snapshot.getChildren()){
                    Mesas mesa = items.getValue(Mesas.class);
                    if(mesa.getEstado().getEstadoMesa().equals(MainActivity.ESTADO_MESA_DISPONIBLE.getEstadoMesa())){
                        listMesas.add(mesa);
                    }

                }
                ArrayAdapter<Mesas> adapter = new ArrayAdapter<>(CRUDReservasActivity.this, android.R.layout.simple_dropdown_item_1line, listMesas);
                sp_mesas.setAdapter(adapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d("onCancellerLocales",error.toString());
        }
    };
}