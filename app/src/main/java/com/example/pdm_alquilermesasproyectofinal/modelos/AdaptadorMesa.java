package com.example.pdm_alquilermesasproyectofinal.modelos;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pdm_alquilermesasproyectofinal.R;

import java.util.ArrayList;

public class AdaptadorMesa extends BaseAdapter {

    public ArrayList<Mesas> data;
    public Context context;
    TextView TxtNumMesa, TxtPrecioReserva, TxtCapacidad, TxtLocal;

    public AdaptadorMesa(ArrayList<Mesas> lista, Context context){

        this.data = lista;
        this.context = context;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Mesas mesa = (Mesas) getItem(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_mesa, null);

        TxtNumMesa = view.findViewById(R.id.TxtViewNumMesa);
        TxtCapacidad = view.findViewById(R.id.TxtViewCapacidad);
        TxtPrecioReserva = view.findViewById(R.id.TxtViewPrecioReserva);
        TxtLocal = view.findViewById(R.id.TxtViewLocal);
        ImageView img = view.findViewById(R.id.imageView2);

        TxtNumMesa.setText("Numero de mesa "+mesa.getNumeroMesa());
        TxtCapacidad.setText("Capacidad "+mesa.getCapacidad()+" personas");
        TxtPrecioReserva.setText("Precio de Reserva "+mesa.getPrecioReserva());
        TxtLocal.setText("Local "+mesa.getLocal().getNombre());
        Glide.with(context).load(mesa.getFoto()).into(img);

        return view;
    }
}
