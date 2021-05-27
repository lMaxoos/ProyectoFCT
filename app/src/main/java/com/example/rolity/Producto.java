package com.example.rolity;

public class Producto {
    private String nombrePatin;
    private String precioPatin;

    public Producto(String nombrePatin, String precioPatin) {
        this.nombrePatin = nombrePatin;
        this.precioPatin = precioPatin;
    }

    public String getNombrePatin() {
        return nombrePatin;
    }

    public String getPrecioPatin() {
        return precioPatin;
    }
}
