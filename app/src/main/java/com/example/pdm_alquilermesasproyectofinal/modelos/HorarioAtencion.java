package com.example.pdm_alquilermesasproyectofinal.modelos;

public class HorarioAtencion {
    private String dia;
    private String horaApertura;
    private String horaCierre;
    private int idHorarioAtencion;
    private Local local;

    public HorarioAtencion() {
    }

    public HorarioAtencion(String dia, String horaApertura, String horaCierre, int idHorarioAtencion, Local local) {
        this.dia = dia;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.idHorarioAtencion = idHorarioAtencion;
        this.local = local;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(String horaApertura) {
        this.horaApertura = horaApertura;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
    }

    public int getIdHorarioAtencion() {
        return idHorarioAtencion;
    }

    public void setIdHorarioAtencion(int idHorarioAtencion) {
        this.idHorarioAtencion = idHorarioAtencion;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }
}
