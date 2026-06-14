
package com.barberpro.model;

/**
 *
 * @author Miller
 */
public class Servicio {
    
    private int id;
    private String nombre;
    private double precio;
    private int estado;
    private String categoria;

    public Servicio() {
    }

    public Servicio(String nombre, String categoria, double precio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public String getCategoria() {
    return categoria;
}

public void setCategoria(String categoria) {
    this.categoria = categoria;
}
    
    @Override
    public String toString() {
        return nombre;
    }
    
}
