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
import com.example.pdm_alquilermesasproyectofinal.modelos.Reservacion;

import java.util.ArrayList;

public class AdaptadorReservaciones extends BaseAdapter {

    public ArrayList<Reservacion> data;
    public Context context;
    TextView Txtfecha, TxthoraEntrada, TxthoraSalida, Txtmesa, TxtUsuario;

    public AdaptadorReservaciones(ArrayList<Reservacion> lista, Context context){

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

        Reservacion reservacion = (Reservacion) getItem(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_reservacion, null);

        Txtfecha = view.findViewById(R.id.TxtViewfecha);
        TxtUsuario = view.findViewById(R.id.TxtViewUsuario);
        Txtmesa = view.findViewById(R.id.TxtViewmesa);
        TxthoraEntrada = view.findViewById(R.id.TxtViewhoraEntrada);
        TxthoraSalida = view.findViewById(R.id.TxtViewhoraSalida);
        ImageView img = view.findViewById(R.id.imageView);

        String Cliente = reservacion.getPago().getNombre()+" "+reservacion.getPago().getUsuario().getApellido();

        TxtUsuario.setText(Cliente);
        Txtmesa.setText("Numero de mesa "+reservacion.getMesa());
        Txtfecha.setText("Fecha "+reservacion.getFecha());
        TxthoraEntrada.setText("Entrada "+reservacion.getHoraEntrada());
        TxthoraSalida.setText("Salida "+reservacion.getHoraSalida());
        Glide.with(context).load(reservacion.getMesa().getFoto()).into(img);

        return view;
    }
}
