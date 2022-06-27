package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.pdm_alquilermesasproyectofinal.adaptadores.AdaptadorLocal;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaLocalesActivity extends AppCompatActivity {

    private ArrayList<Local> listLocal;
    public FirebaseDatabase database;
    public DatabaseReference referenciaData;
    private ListView lv_locales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_locales);

        lv_locales = (ListView)findViewById(R.id.listLocales);

        listLocal = new ArrayList<Local>();
        database = FirebaseDatabase.getInstance();
        referenciaData = database.getReference();
        referenciaData.child(MainActivity.TBL_LOCALES).addValueEventListener(cargarLocales);
    }

    public ValueEventListener cargarLocales = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                listLocal.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    Local local = items.getValue(Local.class);
                    listLocal.add(local);
                }
                AdaptadorLocal ap = new AdaptadorLocal(listLocal, getApplicationContext());
                lv_locales.setAdapter(ap);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        referenciaData.child(MainActivity.TBL_LOCALES).addValueEventListener(cargarLocales);
    }

    public void btnAgregar(View view) {
    }
}