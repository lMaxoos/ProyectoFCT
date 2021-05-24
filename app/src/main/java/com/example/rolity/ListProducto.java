package com.example.rolity;

public class ListProducto {
    private String nombrePatin;
    private String precioPatin;

    public ListProducto(int imgFoto, String nombrePatin, String precioPatin) {
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
