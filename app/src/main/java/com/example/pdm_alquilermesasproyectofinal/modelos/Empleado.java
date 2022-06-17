package com.example.pdm_alquilermesasproyectofinal.modelos;

public class Empleado {
    private Local local;

    public Empleado() {
    }

    public Empleado(Local local) {
        this.local = local;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }
}
