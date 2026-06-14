
package com.barberpro.dao;

import com.barberpro.config.ConexionSQLite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Miller
 */
public class ConfiguracionDAO {
    
    public String obtener(String clave) {
        
        String sql = "SELECT valor FROM configuracion"
                + "WHERE clave = ?";
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1,clave);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()) {
                return rs.getString("valor");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
    
    //-----------------------------------Metodo guardar ---------------------------------//
    public boolean guardar(String clave, String valor) {
        
        String sql = """
                     INSERT OR REPLACE INTO configuracion
                     (clave, valor)
                     VALUES (?, ?)
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1,clave);
            pst.setString(2,valor);
            
            return pst.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
}
