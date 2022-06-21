package com.example.pdm_alquilermesasproyectofinal.modelos;

public class Pago {
    private int idPago;
    private double precio;
    private String nombre;
    private String nTarjeta;
    private String cvv;
    private Usuario usuario;

    public Pago() {
    }

    public Pago(int idPago, double precio, String nombre, String nTarjeta, String cvv, Usuario usuario) {
        this.idPago = idPago;
        this.precio = precio;
        this.nombre = nombre;
        this.nTarjeta = nTarjeta;
        this.cvv = cvv;
        this.usuario = usuario;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getnTarjeta() {
        return nTarjeta;
    }

    public void setnTarjeta(String nTarjeta) {
        this.nTarjeta = nTarjeta;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
