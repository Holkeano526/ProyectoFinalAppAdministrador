package com.example.pruebafotofinal.modelo;

public class plato {
    private String id;
    private String nombre;
    private double precio;
    private String descripcion;
    private String Url;

    public plato(){

    }

    public plato(String id, String nombre, double precio, String url) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.Url = url;
    }


    public String getUrl() {
        return Url;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((plato)obj).id);
    }
}
