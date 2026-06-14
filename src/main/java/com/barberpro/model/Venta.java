package com.barberpro.model;

import java.util.ArrayList;
import java.util.List;

public class Venta {

    private int id;
    private int clienteId;
    private String cliente;
    private String usuario;
    private double subtotal;
    private double impuesto;
    private double descuento;
    private double total;
    private String metodoPago;
    private String fecha;
    private String estado;
    
    // Nueva: lista de items (servicios y productos)
    private List<DetalleVenta> detalles;

    public Venta() {
        this.detalles = new ArrayList<>();
    }

    public Venta(int clienteId, String cliente, String usuario,
                 double subtotal, double impuesto, double descuento, double total,
                 String metodoPago, String fecha, String estado) {
        this.clienteId = clienteId;
        this.cliente = cliente;
        this.usuario = usuario;
        this.subtotal = subtotal;
        this.impuesto = impuesto;
        this.descuento = descuento;
        this.total = total;
        this.metodoPago = metodoPago;
        this.fecha = fecha;
        this.estado = estado;
        this.detalles = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getImpuesto() { return impuesto; }
    public void setImpuesto(double impuesto) { this.impuesto = impuesto; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
    
    public void agregarDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
    }
    
    public void limpiarDetalles() {
        this.detalles.clear();
    }
    
    // Método para calcular totales a partir de detalles
    public void calcularTotales() {
        this.subtotal = 0;
        for (DetalleVenta d : detalles) {
            this.subtotal += d.getSubtotal();
        }
        this.impuesto = this.subtotal * 0.19;
        this.total = this.subtotal + this.impuesto - this.descuento;
    }

    @Override
    public String toString() {
        return "Venta #" + id + " - " + cliente + " - $" + total;
    }
}