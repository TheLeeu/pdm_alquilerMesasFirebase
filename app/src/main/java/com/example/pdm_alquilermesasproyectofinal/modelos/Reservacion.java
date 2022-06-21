package com.example.pdm_alquilermesasproyectofinal.modelos;

public class Reservacion {
    private int idReservacion;
    private Mesas mesa;
    private String fecha;
    private String horaEntrada;
    private String horaSalida;
    private Pago pago;

    public Reservacion() {
    }

    public Reservacion(int idReservacion, Mesas mesa, String fecha, String horaEntrada, String horaSalida, Pago pago) {
        this.idReservacion = idReservacion;
        this.mesa = mesa;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.pago = pago;
    }

    public int getIdReservacion() {
        return idReservacion;
    }

    public void setIdReservacion(int idReservacion) {
        this.idReservacion = idReservacion;
    }

    public Mesas getMesa() {
        return mesa;
    }

    public void setMesa(Mesas mesa) {
        this.mesa = mesa;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }
}
