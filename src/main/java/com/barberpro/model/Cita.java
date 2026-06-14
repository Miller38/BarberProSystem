
package com.barberpro.model;

/**
 *
 * @author Miller
 */
public class Cita {
    
    private int id;
    private int clienteId;
    private int servicioId;
    private String cliente;
    private String servicio;
    private String fecha;
    private String hora;
    private String estado;

    public Cita() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getServicioId() {
        return servicioId;
    }

    public void setServicioId(int servicoId) {
        this.servicioId = servicoId;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
     
    
    
}
