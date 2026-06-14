 package com.barberpro.dao;

import com.barberpro.config.ConexionSQLite;
import com.barberpro.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    
    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (nombre, categoria, stock, stock_minimo, precio_compra, precio_venta, fecha_vencimiento, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = ConexionSQLite.conectar();
            PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, p.getNombre());
            pst.setString(2, p.getCategoria());
            pst.setInt(3, p.getStock());
            pst.setInt(4, p.getStockMinimo());
            pst.setDouble(5, p.getPrecioCompra());
            pst.setDouble(6, p.getPrecioVenta());
            pst.setString(7, p.getFechaVencimiento());
            pst.setInt(8, p.getEstado());
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error insertar: " + e.getMessage());
            return false;
        }
    }
    
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE estado = 1 ORDER BY id DESC";
        try(Connection conn = ConexionSQLite.conectar();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()) {
            while(rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setPrecioCompra(rs.getDouble("precio_compra"));
                p.setPrecioVenta(rs.getDouble("precio_venta"));
                p.setFechaVencimiento(rs.getString("fecha_vencimiento"));
                p.setEstado(rs.getInt("estado"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error listar: " + e.getMessage());
        }
        return lista;
    }
    
    // ✅ MÉTODO PARA DESCONTAR STOCK - CORREGIDO
    public boolean descontarStock(int id, int cantidad) {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, cantidad);
            pst.setInt(2, id);
            pst.setInt(3, cantidad);
            int actualizados = pst.executeUpdate();
            System.out.println("descontarStock - ID: " + id + ", Cantidad: " + cantidad + " - Filas afectadas: " + actualizados);
            return actualizados > 0;
        } catch (Exception e) {
            System.out.println("Error descontarStock: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ✅ MÉTODO PARA AUMENTAR STOCK (para anular ventas)
    public boolean aumentarStock(int id, int cantidad) {
        String sql = "UPDATE productos SET stock = stock + ? WHERE id = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, cantidad);
            pst.setInt(2, id);
            int actualizados = pst.executeUpdate();
            System.out.println("aumentarStock - ID: " + id + ", Cantidad: " + cantidad + " - Filas afectadas: " + actualizados);
            return actualizados > 0;
        } catch (Exception e) {
            System.out.println("Error aumentarStock: " + e.getMessage());
            return false;
        }
    }
    
    // ✅ MÉTODO PARA VERIFICAR STOCK
    public boolean verificarStock(int id, int cantidad) {
        String sql = "SELECT stock FROM productos WHERE id = ? AND estado = 1";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int stockActual = rs.getInt("stock");
                System.out.println("verificarStock - ID: " + id + ", Stock actual: " + stockActual + ", Solicitado: " + cantidad);
                return stockActual >= cantidad;
            }
        } catch (Exception e) {
            System.out.println("Error verificarStock: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizarStock(int id, int stock) {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";
        try(Connection conn = ConexionSQLite.conectar();
            PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, stock);
            pst.setInt(2, id);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public List<Producto> stockBajo() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE stock <= stock_minimo AND estado = 1";
        try(Connection conn = ConexionSQLite.conectar();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()) {
            while(rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }
    
    public List<Producto> vencidos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE date(fecha_vencimiento) <= date('now')";
        try(Connection conn = ConexionSQLite.conectar();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()) {
            while(rs.next()) {
                Producto p = new Producto();
                p.setNombre(rs.getString("nombre"));
                p.setFechaVencimiento(rs.getString("fecha_vencimiento"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }
    
    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, categoria = ?, stock = ?, stock_minimo = ?, precio_compra = ?, precio_venta = ?, fecha_vencimiento = ? WHERE id = ?";
        try(Connection conn = ConexionSQLite.conectar();
            PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, p.getNombre());
            pst.setString(2, p.getCategoria());
            pst.setInt(3, p.getStock());
            pst.setInt(4, p.getStockMinimo());
            pst.setDouble(5, p.getPrecioCompra());
            pst.setDouble(6, p.getPrecioVenta());
            pst.setString(7, p.getFechaVencimiento());
            pst.setInt(8, p.getId());
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(int id) {
        String sql = "UPDATE productos SET estado = 0 WHERE id = ?";
        try(Connection conn = ConexionSQLite.conectar();
            PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public List<Producto> buscar(String texto) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE estado = 1 AND (nombre LIKE ? OR categoria LIKE ?) ORDER BY nombre";
        try(Connection conn = ConexionSQLite.conectar();
            PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + texto + "%");
            pst.setString(2, "%" + texto + "%");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setPrecioCompra(rs.getDouble("precio_compra"));
                p.setPrecioVenta(rs.getDouble("precio_venta"));
                p.setFechaVencimiento(rs.getString("fecha_vencimiento"));
                p.setEstado(rs.getInt("estado"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }
}