package com.example.pdm_alquilermesasproyectofinal.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pdm_alquilermesasproyectofinal.R;
import com.example.pdm_alquilermesasproyectofinal.modelos.HorarioAtencion;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdaptadorHorarioAtencion extends BaseAdapter {

    private ArrayList<HorarioAtencion> data;
    private Context context;

    public AdaptadorHorarioAtencion(ArrayList<HorarioAtencion> data, Context context) {
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
        HorarioAtencion horario = (HorarioAtencion) getItem(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_horarios_atencion, null);

        TextView tv_dia = view.findViewById(R.id.tvDiaItemHorarioAtencion);
        TextView tv_hA = view.findViewById(R.id.tvHoraAperturaItemHorarioAtencion);
        TextView tv_hC = view.findViewById(R.id.tvHoraCierreItemHorarioAtencion);

        tv_dia.setText("Dia: "+horario.getDia());
        tv_hA.setText("Hora apertura: " + horario.getHoraApertura());
        tv_hC.setText("Hora cierre: " + horario.getHoraCierre());

        return  view;
    }
}
