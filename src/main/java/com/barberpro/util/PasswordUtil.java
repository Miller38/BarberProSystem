
package com.barberpro.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Miller
 */
public class PasswordUtil {
    
    // encriptar
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }
    
    // verificar
    public static boolean verificarPassword(
                String password,
                String hash) {
        return BCrypt.checkpw(password, hash);
    }
    
}
