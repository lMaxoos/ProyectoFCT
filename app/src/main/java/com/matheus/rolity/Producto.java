package com.matheus.rolity;

import java.io.Serializable;

public class Producto implements Serializable {
    private String nombreProducto;
    private String precioProducto;
    private String imagen;
    private String categoria;
    private String path;

    public Producto(String nombreProducto, String precioProducto, String imagen, String categoria, String path) {
        this.nombreProducto = nombreProducto;
        this.precioProducto = precioProducto;
        this.imagen = imagen;
        this.categoria = categoria;
        this.path = path;
    }

    public Producto(String nombreProducto, String precioProducto, String imagen, String categoria) {
        this.nombreProducto = nombreProducto;
        this.precioProducto = precioProducto;
        this.imagen = imagen;
        this.categoria = categoria;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getPrecioProducto() {
        return precioProducto;
    }

    public String getImagen() {
        return imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getPath() {
        return path;
    }
}
