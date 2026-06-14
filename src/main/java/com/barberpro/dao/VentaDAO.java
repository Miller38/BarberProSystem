package com.barberpro.dao;

import com.barberpro.config.ConexionSQLite;
import com.barberpro.model.Venta;
import com.barberpro.model.DetalleVenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    private ProductoDAO productoDAO = new ProductoDAO();

    public boolean insertar(Venta venta) {
    Connection conn = null;
    PreparedStatement pstVenta = null;
    PreparedStatement pstDetalle = null;
    ResultSet rs = null;
    
    try {
        conn = ConexionSQLite.conectar();
        conn.setAutoCommit(false);
        
        System.out.println("=========================================");
        System.out.println("1. Insertando venta...");
        
        // Insertar venta
        String sqlVenta = "INSERT INTO ventas (cliente_id, usuario, subtotal, impuesto, descuento, total, metodo_pago, fecha, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        pstVenta = conn.prepareStatement(sqlVenta, PreparedStatement.RETURN_GENERATED_KEYS);
        pstVenta.setInt(1, venta.getClienteId());
        pstVenta.setString(2, venta.getUsuario());
        pstVenta.setDouble(3, venta.getSubtotal());
        pstVenta.setDouble(4, venta.getImpuesto());
        pstVenta.setDouble(5, venta.getDescuento());
        pstVenta.setDouble(6, venta.getTotal());
        pstVenta.setString(7, venta.getMetodoPago());
        pstVenta.setString(8, venta.getFecha());
        pstVenta.setString(9, "ACTIVA");
        
        int filas = pstVenta.executeUpdate();
        if (filas == 0) {
            System.out.println("❌ Error: No se insertó la venta");
            conn.rollback();
            return false;
        }
        
        rs = pstVenta.getGeneratedKeys();
        int ventaId = 0;
        if (rs.next()) {
            ventaId = rs.getInt(1);
            venta.setId(ventaId);
            System.out.println("✅ Venta insertada - ID: " + ventaId);
        } else {
            conn.rollback();
            return false;
        }
        
        // Insertar detalles
        String sqlDetalle = "INSERT INTO detalle_ventas (venta_id, tipo_item, item_id, nombre, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?)";
        pstDetalle = conn.prepareStatement(sqlDetalle);
        
        System.out.println("2. Procesando " + venta.getDetalles().size() + " item(s)...");
        
        for (DetalleVenta detalle : venta.getDetalles()) {
            System.out.println("   Item: " + detalle.getTipoItem() + " - " + detalle.getNombre() + " x" + detalle.getCantidad());
            
            pstDetalle.setInt(1, ventaId);
            pstDetalle.setString(2, detalle.getTipoItem());
            pstDetalle.setInt(3, detalle.getItemId());
            pstDetalle.setString(4, detalle.getNombre());
            pstDetalle.setInt(5, detalle.getCantidad());
            pstDetalle.setDouble(6, detalle.getPrecioUnitario());
            pstDetalle.setDouble(7, detalle.getSubtotal());
            pstDetalle.executeUpdate();
            
            // Descontar stock SOLO para productos
            if ("PRODUCTO".equals(detalle.getTipoItem())) {
                System.out.println("      → Descontando stock...");
                String sqlUpdate = "UPDATE productos SET stock = stock - " + detalle.getCantidad() + " WHERE id = " + detalle.getItemId() + " AND stock >= " + detalle.getCantidad();
                System.out.println("      SQL: " + sqlUpdate);
                
                PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdate);
                int actualizados = pstUpdate.executeUpdate();
                pstUpdate.close();
                
                System.out.println("      → Filas actualizadas: " + actualizados);
                if (actualizados == 0) {
                    System.out.println("      ❌ ERROR: No se pudo descontar stock");
                    conn.rollback();
                    return false;
                } else {
                    System.out.println("      ✅ Stock descontado correctamente");
                }
            }
        }
        
        conn.commit();
        System.out.println("=========================================");
        System.out.println("✅ VENTA COMPLETADA CON ÉXITO!");
        return true;
        
    } catch (Exception e) {
        try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
        System.out.println("❌ Error: " + e.getMessage());
        e.printStackTrace();
        return false;
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (pstDetalle != null) pstDetalle.close(); } catch (Exception e) {}
        try { if (pstVenta != null) pstVenta.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}

    public List<Venta> listar() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.id, v.cliente_id, c.nombre AS cliente, v.usuario, v.subtotal, v.impuesto, v.descuento, v.total, v.metodo_pago, v.fecha, v.estado FROM ventas v INNER JOIN clientes c ON c.id = v.cliente_id ORDER BY v.id DESC";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Venta v = new Venta();
                v.setId(rs.getInt("id"));
                v.setClienteId(rs.getInt("cliente_id"));
                v.setCliente(rs.getString("cliente"));
                v.setUsuario(rs.getString("usuario"));
                v.setSubtotal(rs.getDouble("subtotal"));
                v.setImpuesto(rs.getDouble("impuesto"));
                v.setDescuento(rs.getDouble("descuento"));
                v.setTotal(rs.getDouble("total"));
                v.setMetodoPago(rs.getString("metodo_pago"));
                v.setFecha(rs.getString("fecha"));
                v.setEstado(rs.getString("estado"));
                v.setDetalles(cargarDetalles(v.getId(), conn));
                lista.add(v);
            }
        } catch (Exception e) {
            System.out.println("Error listar ventas: " + e.getMessage());
        }
        return lista;
    }
    
    private List<DetalleVenta> cargarDetalles(int ventaId, Connection conn) {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_ventas WHERE venta_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, ventaId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                DetalleVenta d = new DetalleVenta();
                d.setId(rs.getInt("id"));
                d.setVentaId(rs.getInt("venta_id"));
                d.setTipoItem(rs.getString("tipo_item"));
                d.setItemId(rs.getInt("item_id"));
                d.setNombre(rs.getString("nombre"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                d.setSubtotal(rs.getDouble("subtotal"));
                detalles.add(d);
            }
        } catch (Exception e) {
            System.out.println("Error cargar detalles: " + e.getMessage());
        }
        return detalles;
    }

    public boolean anular(int idVenta) {
        String sql = "UPDATE ventas SET estado='ANULADA' WHERE id=?";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = ConexionSQLite.conectar();
            conn.setAutoCommit(false);
            
            // Obtener detalles antes de anular
            List<DetalleVenta> detalles = obtenerDetallesPorVenta(idVenta, conn);
            
            pst = conn.prepareStatement(sql);
            pst.setInt(1, idVenta);
            boolean ok = pst.executeUpdate() > 0;
            
            if (ok) {
                // Restaurar stock de productos
                for (DetalleVenta d : detalles) {
                    if ("PRODUCTO".equals(d.getTipoItem())) {
                        productoDAO.aumentarStock(d.getItemId(), d.getCantidad());
                    }
                }
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            System.out.println("Error anular venta: " + e.getMessage());
            return false;
        } finally {
            try { if (pst != null) pst.close(); if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
    
    private List<DetalleVenta> obtenerDetallesPorVenta(int ventaId, Connection conn) {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_ventas WHERE venta_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, ventaId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                DetalleVenta d = new DetalleVenta();
                d.setTipoItem(rs.getString("tipo_item"));
                d.setItemId(rs.getInt("item_id"));
                d.setCantidad(rs.getInt("cantidad"));
                detalles.add(d);
            }
        } catch (Exception e) {}
        return detalles;
    }

    public double totalIngresos() {
        String sql = "SELECT IFNULL(SUM(total),0) total FROM ventas WHERE estado='ACTIVA'";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getDouble("total");
        } catch (Exception e) {
            System.out.println("Error total ingresos: " + e.getMessage());
        }
        return 0;
    }

    public int totalVentas() {
        String sql = "SELECT COUNT(*) total FROM ventas WHERE estado='ACTIVA'";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            System.out.println("Error total ventas: " + e.getMessage());
        }
        return 0;
    }

    public List<Venta> buscar(String texto) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.id, c.nombre AS cliente, v.total, v.metodo_pago, v.fecha, v.estado FROM ventas v INNER JOIN clientes c ON c.id = v.cliente_id WHERE c.nombre LIKE ? OR v.id LIKE ? ORDER BY v.id DESC";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + texto + "%");
            pst.setString(2, "%" + texto + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Venta v = new Venta();
                v.setId(rs.getInt("id"));
                v.setCliente(rs.getString("cliente"));
                v.setTotal(rs.getDouble("total"));
                v.setMetodoPago(rs.getString("metodo_pago"));
                v.setFecha(rs.getString("fecha"));
                v.setEstado(rs.getString("estado"));
                lista.add(v);
            }
        } catch (Exception e) {
            System.out.println("Error buscar ventas: " + e.getMessage());
        }
        return lista;
    }
}