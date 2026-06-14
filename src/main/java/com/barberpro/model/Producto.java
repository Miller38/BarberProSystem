package com.barberpro.model;

/**
 *
 * @author Miller
 */
public class Producto {
    
    private int id;
    private String nombre;
    private String categoria;
    private int stock;
    private int stockMinimo;
    private double precioCompra;
    private double precioVenta;    
    private String fechaVencimiento;
    private int estado;

    public Producto() {
         this.estado = 1;
    }

    public Producto(String nombre, String categoria, int stock, int stockMinimo, double precioCompra, double precioVenta, String fechaVencimiento) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.fechaVencimiento = fechaVencimiento;
        this.estado=1;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public String getFechaVencimiento() {
    return fechaVencimiento;
}

public void setFechaVencimiento(
        String fechaVencimiento) {

    this.fechaVencimiento =
            fechaVencimiento;
}

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    // ✅ AGREGAR ESTO
    @Override
    public String toString() {
        return nombre + " - $" + precioVenta + " (Stock: " + stock + ")";
    }
    
    
}
