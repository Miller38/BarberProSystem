
package com.barberpro.dao;

import com.barberpro.config.ConexionSQLite;
import com.barberpro.model.Usuario;
import com.barberpro.util.FechaUtil;
import com.barberpro.util.PasswordUtil;
import com.barberpro.dao.AuditoriaDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miller
 */
public class UsuarioDAO {
    
    private LoginLogDAO logDAO =
        new LoginLogDAO();
    
    //------------------------------------------ Metodo para loguearse --------------------------------//
  public Usuario login(
        String usuario,
        String password) {

    String sql = """
            SELECT *
            FROM usuarios
            WHERE usuario = ?
            AND estado = 1
            """;

    try (Connection conn =
                 ConexionSQLite.conectar();

         PreparedStatement pst =
                 conn.prepareStatement(sql)) {

        pst.setString(1, usuario);

        ResultSet rs =
                pst.executeQuery();

        if (rs.next()) {

            String hash =
                    rs.getString("password");

            boolean passwordCorrecta =
                    PasswordUtil.verificarPassword(
                            password,
                            hash);

            if (passwordCorrecta) {

                // Actualizar último acceso
                actualizarUltimoAcceso(
                        rs.getInt("id"));

                // Registrar log de login
                logDAO.registrar(
                        usuario,
                        "EXITOSO",
                        "Login correcto");

                // Registrar sesión
                SesionDAO sesionDAO =
                        new SesionDAO();

                sesionDAO.iniciarSesion(
                        usuario);
                AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
                
                auditoriaDAO.registrar( usuario,
        "INICIO DE SESION");

                Usuario u =
                        new Usuario();

                u.setId(
                        rs.getInt("id"));

                u.setNombre(
                        rs.getString("nombre"));

                u.setUsuario(
                        rs.getString("usuario"));

                u.setRol(
                        rs.getString("rol"));

                u.setEmail(
                        rs.getString("email"));

                u.setTelefono(
                        rs.getString("telefono"));

                u.setEstado(
                        rs.getInt("estado"));

                u.setBloqueado(
                        rs.getInt("bloqueado"));

                return u;

            } else {

                logDAO.registrar(
                        usuario,
                        "FALLIDO",
                        "Contraseña incorrecta");
            }
        } else {

            logDAO.registrar(
                    usuario,
                    "FALLIDO",
                    "Usuario no existe");
        }

    } catch (Exception e) {

        System.out.println(
                "Error login: "
                + e.getMessage());
    }

    return null;
}
    
    //--------------------------------------Metodo para insertar usuarios-------------------------------//
    public boolean  insertar(Usuario usuario) {
        
        String sql = """
                     INSERT INTO usuarios
                     (nombre, usuario, password, rol, email, telefono, foto, estado)
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                     """;
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)){
            
            pst.setString(1,usuario.getNombre());
            pst.setString(2,usuario.getUsuario());            
             // ---------------------------Password encriptada-------------------------------------------//
             pst.setString(3,PasswordUtil.hashPassword(usuario.getPassword()));             
             pst.setString(4,usuario.getRol());
             pst.setString(5,usuario.getEmail());
             pst.setString(6,usuario.getTelefono());
             pst.setString(7,usuario.getFoto());
             pst.setInt(8,1);
             
             boolean ok =
        pst.executeUpdate() > 0;

if(ok) {

    AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    auditoriaDAO.registrar(
            usuario.getUsuario(),
            "CREO USUARIO");
}

return ok;
            
        } catch (Exception e) {
            System.out.println("Error insertar usuario : " + e.getMessage());
            return false;
        }
    }
    
    //----------------------------------------Metodo para actualizar------------------------------------//
    public boolean actualizar(Usuario usuario) {
        
        String sql = """
                     UPDATE usuarios
                     SET nombre = ?,
                     usuario = ?, 
                     rol = ?,
                     email = ?,
                     telefono = ?,
                     foto = ?,
                     estado = ?
                     WHERE id = ?
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1,usuario.getNombre());
            pst.setString(2,usuario.getUsuario());
            pst.setString(3,usuario.getRol());
            pst.setString(4,usuario.getEmail());
            pst.setString(5,usuario.getTelefono());
            pst.setString(6,usuario.getFoto());
            pst.setInt(7,usuario.getEstado());
            pst.setInt(8,usuario.getId());
            
            boolean ok =
        pst.executeUpdate() > 0;

if(ok) {

    AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    auditoriaDAO.registrar(
            usuario.getUsuario(),
            "MODIFICO USUARIO");
}

return ok;
            
        } catch (Exception e) {
            System.out.println("Error al actualizar usuario : " + e.getMessage());
            return false;
        }
    }
    
    //-------------------------------Metodo para eliminar(Soft delete)---------------------------------//
    public boolean eliminar(int id) {
        String sql = """
                     UPDATE usuarios
                     SET estado = 0
                     WHERE id = ?
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, id);
            
            boolean ok =
        pst.executeUpdate() > 0;

if(ok) {

     AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    auditoriaDAO.registrar(
            "SISTEMA",
            "DESACTIVO USUARIO ID "
            + id);
}

return ok;
            
        } catch (Exception e) {
            System.out.println("Error al eliminar usuario : " + e.getMessage());
            return false;
        }
    }
    
    //--------------------------------------Metodo para listar-------------------------------------------//
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        
        String sql = """                        
                    SELECT * 
                     FROM usuarios
                     ORDER BY nombre
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {
            
            while(rs.next()) {
                Usuario u = new Usuario();
                
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setRol(rs.getString("rol"));
                u.setEmail(rs.getString("email"));
                u.setTelefono(rs.getString("telefono"));
                u.setEstado(rs.getInt("estado"));
                u.setBloqueado(rs.getInt("bloqueado"));
                u.setUltimoAcceso(rs.getString("ultimo_acceso"));
                
                lista.add(u);
            }
            
        } catch (Exception e) {
            System.out.println("Error al listar usuarios : " + e.getMessage());
        }
        return lista;
    }     
    
      //--------------------------------------Metodo para buscar----------------------------------------//
    public List<Usuario> buscar(String texto)  {
        List<Usuario> lista = new ArrayList<>();
        
        String sql = """
                     SELECT *
                     FROM usuarios
                     WHERE 
                            nombre LIKE ?
                            OR usuario LIKE ?
                            OR email LIKE ?
                            OR telefono LIKE ?
                     ORDER BY nombre
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            String filtro = "%" + texto + "%";
            
            pst.setString(1,filtro);
            pst.setString(2,filtro);
            pst.setString(3,filtro);
            pst.setString(4,filtro);
            
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()) {
                Usuario u = new Usuario();
                
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setRol(rs.getString("rol"));
                u.setEmail(rs.getString("email"));
                u.setTelefono(rs.getString("telefono"));
                u.setEstado(rs.getInt("estado"));
                u.setBloqueado(rs.getInt("bloqueado"));
                
                lista.add(u);
            }
            
        } catch (Exception e) {
            System.out.println("Error al buscar usuario : " + e.getMessage());
        }
        return lista;
    }
    
    //-------------------------------- Metodo para verificar si existe usuario -------------------------//
    public boolean existeUsuario(String usuario)  {
        String sql = """
                     SELECT id FROM usuarios
                     WHERE usuario = ?
                     """;
        
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)){
            
            pst.setString(1,usuario);
            
            ResultSet rs = pst.executeQuery();
            return  rs.next();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    //------------------------------------ Metodo para desactivar-----------------------------------//
   private boolean cambiarEstado(
        int id,
        int estado) {

    String sql = """
                 UPDATE usuarios
                 SET estado = ?
                 WHERE id = ?
                 """;

    try (Connection conn =
                 ConexionSQLite.conectar();

         PreparedStatement pst =
                 conn.prepareStatement(sql)) {

        pst.setInt(1, estado);
        pst.setInt(2, id);

        boolean ok =
                pst.executeUpdate() > 0;

        if (ok) {

            AuditoriaDAO auditoriaDAO =
                    new AuditoriaDAO();

            auditoriaDAO.registrar(
                    "SISTEMA",
                    estado == 1
                    ? "ACTIVÓ USUARIO ID " + id
                    : "DESACTIVÓ USUARIO ID " + id);
        }

        return ok;

    } catch (Exception e) {

        System.out.println(
                "Error cambiar estado: "
                + e.getMessage());

        return false;
    }
}
     //------------------------------------ Metodo para bloquear-----------------------------------//
    public boolean bloquear(int id) {
        
        String sql = """
                     UPDATE usuarios
                     SET bloqueado = 1
                     WHERE id = ?
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, id);
            
            boolean ok =
        pst.executeUpdate() > 0;

if(ok) {
 
    AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    auditoriaDAO.registrar(
            "SISTEMA",
            "BLOQUEO USUARIO ID "
            + id);
}

return ok;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    //--------------------------------- Metodo para desbloquear-----------------------------------//
    public boolean desbloquear(int id) {
        
        String sql = """
                     UPDATE usuarios
                     SET bloqueado = 0,
                            intentos = 0,
                            bloqueado_hasta = NULL
                     WHERE id = ?
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, id);
            
            boolean ok =
        pst.executeUpdate() > 0;

if(ok) {

    AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    auditoriaDAO.registrar(
            "SISTEMA",
            "DESBLOQUEO USUARIO ID "
            + id);
}

return ok;
            
        } catch (Exception e) {
            return false;
        }
    }
    
      //--------------------------------- Metodo ultimo acceso-----------------------------------//
    public void registarUltimoAcceso(String usuario) {
        
        String sql = """
                     UPDATE usuarios
                     SET ultimo_acceso = CURRENT_TIMESTAMP
                     WHERE usuario ?
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1,usuario);
            
            pst.executeUpdate();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    //-------------------------------------Metodo ultimo acceso ------------------------------//
    public void actualizarUltimoAcceso(
        int idUsuario) {

    String sql = """
            UPDATE usuarios
            SET ultimo_acceso = ?
            WHERE id = ?
            """;

    try (Connection conn =
                 ConexionSQLite.conectar();

         PreparedStatement pst =
                 conn.prepareStatement(sql)) {

        pst.setString(1, FechaUtil.ahora());
        pst.setInt(2, idUsuario);

        pst.executeUpdate();

    } catch (Exception e) {

        System.out.println(
                "Error actualizando acceso: "
                + e.getMessage());
    }
}
    
    //---------------------------------activar---------------------------------------//
    public boolean activar(int id) {

    return cambiarEstado(id, 1);
}

public boolean desactivar(int id) {

    return cambiarEstado(id, 0);
}
    
}
