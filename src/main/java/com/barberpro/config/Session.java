
package com.barberpro.config;

import com.barberpro.model.Usuario;

/**
 *
 * @author Miller
 */
public class Session {
    
    private static Usuario usuarioActual;
    
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public static void setUsuarioActual(Usuario usuario)   {
        usuarioActual = usuario;
    }
    
    public static void cerrarSession()  {
        usuarioActual = null;
    }
    
}
