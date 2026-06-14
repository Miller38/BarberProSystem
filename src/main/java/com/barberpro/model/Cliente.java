
package com.barberpro.model;

/**
 *
 * @author Miller
 */
public class Cliente {
    
    private int id;
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;
    private int estado;
    private String fechaRegistro;
    private String foto;

    public Cliente() {
    }

    public Cliente(String nombre, String telefono, String email, String direccion) {
       
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
       
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public String getFechaRegistro() {
    return fechaRegistro;
}

public void setFechaRegistro(
        String fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
}

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


    
    @Override
    public String toString() {
        return nombre;
    }
       
    
}
