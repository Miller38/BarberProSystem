
package com.barberpro.dao;

import com.barberpro.config.ConexionSQLite;
import com.barberpro.model.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miller
 */
public class ServicioDAO {
    
    //------------------------------------- Metodo para insertar servicio----------------------------//
    public boolean  insertar(Servicio servicio)  {
        
        String sql = """
                     INSERT INTO servicios
                     (nombre, categoria, precio)
                     VALUES (?, ?, ?)
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1,servicio.getNombre());
            pst.setString(2,servicio.getCategoria());
            pst.setDouble(3, servicio.getPrecio());
            
            pst.executeUpdate();
            
            return true;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
     //------------------------------------- Metodo para listar servicio----------------------------//
    public List<Servicio> listar() {
        List<Servicio> lista = new ArrayList<>();
        
        String sql = """
                     SELECT * FROM servicios
                     WHERE estado = 1
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {
            
           while(rs.next())  {
               Servicio s = new Servicio();
               
               s.setId(rs.getInt("id"));
               s.setNombre(rs.getString("nombre"));
                s.setCategoria(rs.getString("categoria"));
               s.setPrecio(rs.getDouble("precio"));
               
               lista.add(s);
           }           
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }
    
    //------------------------------------- Metodo para listar servicio----------------------------//
    public boolean actualizar(Servicio servicio)  {
        String sql = """
                     UPDATE servicios
                     SET nombre = ?,
                            categoria = ?,
                            precio = ?
                     WHERE id = ?
                     """;
        
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)){
            
            pst.setString(1,    servicio.getNombre());
            pst.setString(2,servicio.getCategoria());
            pst.setDouble(3,servicio.getPrecio());
            pst.setInt(4,servicio.getId());
            
            return pst.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    //------------------------------------Metodo eliminar (soft delete) ---------------------------//
    public boolean eliminar(int id)  {
        String sql = """
                     UPDATE servicios
                     SET estado = 0
                     WHERE id = ?
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, id);
            
            return pst.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
      //---------------------------------------Metodo para buscar--------------------------------//
    public List<Servicio> buscar(String texto) {

    List<Servicio> lista = new ArrayList<>();

    String sql = """
                 SELECT *
                 FROM servicios
                 WHERE estado = 1
                 AND nombre LIKE ?
                 ORDER BY nombre
                 """;

    try(Connection conn = ConexionSQLite.conectar();
        PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, "%" + texto + "%");

        ResultSet rs = pst.executeQuery();

        while(rs.next()) {

            Servicio s = new Servicio();

            s.setId(rs.getInt("id"));
            s.setNombre(rs.getString("nombre"));
            s.setCategoria(rs.getString("categoria"));
            s.setPrecio(rs.getDouble("precio"));

            lista.add(s);
        }

    } catch(Exception e) {
        System.out.println(e.getMessage());
    }

    return lista;
}
}
