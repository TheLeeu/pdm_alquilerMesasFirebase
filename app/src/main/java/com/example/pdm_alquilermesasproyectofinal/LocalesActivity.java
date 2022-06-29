package com.example.pdm_alquilermesasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pdm_alquilermesasproyectofinal.adaptadores.AdaptadorLocal;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LocalesActivity extends AppCompatActivity {


    private ArrayList<Local> listLocal;
    public FirebaseDatabase database;
    public DatabaseReference referenciaData;
    private ListView lv_locales;
    private int posicionItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locales);

        lv_locales = (ListView)findViewById(R.id.ListLocalesUsuario);

        listLocal = new ArrayList<Local>();
        database = FirebaseDatabase.getInstance();
        referenciaData = database.getReference();
        referenciaData.child(MainActivity.TBL_LOCALES).addValueEventListener(cargarLocales);

        lv_locales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                posicionItem = i;

                Intent inte = new Intent(getApplicationContext(), List_DipsMesas.class);
                if(getIntent().getStringExtra("ACTIVITY").equals("ClienteMainActivityDis")) {
                    inte.putExtra("ACTIVITY", "LocalesActivityDis");
                }

                if(getIntent().getStringExtra("ACTIVITY").equals("ClienteMainActivity")) {
                    inte.putExtra("ACTIVITY", "LocalesActivity");
                }

                inte.putExtra("idLocal", String.valueOf(listLocal.get(posicionItem).getIdLocal()));
                inte.putExtra("nombreLocal", listLocal.get(posicionItem).getNombre());
                inte.putExtra("direccionLocal", listLocal.get(posicionItem).getDireccion());
                inte.putExtra("telefonoLocal", listLocal.get(posicionItem).getTelefono());
                inte.putExtra("coordenadasLocal", listLocal.get(posicionItem).getCoordenadasGps());
                inte.putExtra("fotoLocal", listLocal.get(posicionItem).getFoto());
                startActivity(inte);

            }
        });

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

}