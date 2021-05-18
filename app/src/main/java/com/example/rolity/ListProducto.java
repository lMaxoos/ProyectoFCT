package com.example.rolity;

public class ListProducto {
    private int imgFoto;
    private String nombrePatin;
    private String precioPatin;

    public ListProducto(int imgFoto, String nombrePatin, String precioPatin) {
        this.imgFoto = imgFoto;
        this.nombrePatin = nombrePatin;
        this.precioPatin = precioPatin;
    }

    public int getImgFoto() {
        return imgFoto;
    }

    public String getNombrePatin() {
        return nombrePatin;
    }

    public String getPrecioPatin() {
        return precioPatin;
    }
}
