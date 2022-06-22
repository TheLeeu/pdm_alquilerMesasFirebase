package com.example.pdm_alquilermesasproyectofinal.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pdm_alquilermesasproyectofinal.R;
import com.example.pdm_alquilermesasproyectofinal.modelos.Local;

import java.util.ArrayList;

public class AdaptadorLocal extends BaseAdapter {

    private ArrayList<Local> data;
    private Context context;

    public AdaptadorLocal(ArrayList<Local> data, Context context) {
        this.data = data;
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
        Local local = (Local) getItem(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_local, null);

        TextView tv_idLocal = view.findViewById(R.id.tvIdItemLocal);
        TextView tv_nombre = view.findViewById(R.id.tvNombreItemLocal);
        TextView tv_direccion = view.findViewById(R.id.tvDireccionItemLocal);
        TextView tv_telefono = view.findViewById(R.id.tvTelefonoItemLocal);
        TextView tv_coordenadas = view.findViewById(R.id.tvCoordenadasItemLocal);
        ImageView img = view.findViewById(R.id.imageView3);

        tv_idLocal.setText("idLocal: " + local.getIdLocal());
        tv_nombre.setText("Nombre: " + local.getNombre());
        tv_direccion.setText("Direccion: "+ local.getDireccion());
        tv_telefono.setText("Telefono: "+local.getTelefono());
        tv_coordenadas.setText("Coordenadas: "+local.getCoordenadasGps());
        Glide.with(context).load(local.getFoto()).into(img);
        return view;
    }
}
