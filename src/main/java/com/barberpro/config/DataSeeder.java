
package com.barberpro.config;

import com.barberpro.dao.UsuarioDAO;
import com.barberpro.model.Usuario;
/**
 *
 * @author Miller
 */
public class DataSeeder {
    
    public static void crearAdmin() {
        
       UsuarioDAO dao = new UsuarioDAO();
        
        boolean existe = dao.existeUsuario("admin");
        
        if(!existe)  {
            Usuario admin = new Usuario(
                          "Administardor",
                          "admin",
                          "123456",
                         "ADMIN"
            );
            
            dao.insertar(admin);
            
            System.out.println("Admin creado con exito");
        } else  {
            System.out.println("Admin ya existe.");
        }
        
    }
    
}
