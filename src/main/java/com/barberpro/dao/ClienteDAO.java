package com.barberpro.dao;

import com.barberpro.config.ConexionSQLite;
import com.barberpro.model.Cliente;
import java.io.File;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Miller
 */
public class ClienteDAO {

    //--------------------------------- Metodo para insertar clientes ----------------------------------//
    public boolean insertar(Cliente cliente) {

        String sql = """
                    INSERT INTO clientes
                     (nombre, telefono, email, direccion, foto)
                     VALUES (?, ?, ?, ?, ?)
                    """;

        System.out.println("=== INSERTAR EN DAO ===");
        System.out.println("Nombre: " + cliente.getNombre());
        System.out.println("Foto a guardar: " + cliente.getFoto());

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, cliente.getNombre());
            pst.setString(2, cliente.getTelefono());
            pst.setString(3, cliente.getEmail());
            pst.setString(4, cliente.getDireccion());
            pst.setString(5, cliente.getFoto());

            pst.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //----------------------------------- Metodo para listar --------------------------------------------//
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, telefono, email, direccion, fecha_registro, foto FROM clientes WHERE estado = 1 ORDER BY id DESC";

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setDireccion(rs.getString("direccion"));
                c.setFechaRegistro(rs.getString("fecha_registro"));
                c.setFoto(rs.getString("foto"));  // ← Leer foto
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    //----------------------------------- Metodo para actualizar---------------------------------------//
    public boolean actualizar(Cliente cliente) {

        String sql = """
                     
                     UPDATE clientes 
                     SET nombre = ?,
                     telefono = ?,
                     email = ?,
                     direccion = ?,
                     foto = ?
                     WHERE id = ?
                     """;

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, cliente.getNombre());
            pst.setString(2, cliente.getTelefono());
            pst.setString(3, cliente.getEmail());
            pst.setString(4, cliente.getDireccion());
            pst.setString(5, cliente.getFoto());
            pst.setInt(6, cliente.getId());

            pst.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //----------------------------------- Metodo soft delete------------------------------------------//
    public boolean eliminar(int id) {
        // Primero obtener el nombre de la foto antes de eliminar
        String foto = obtenerFotoPorId(id);

        String sql = "UPDATE clientes SET estado = 0 WHERE id = ?";

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            int resultado = pst.executeUpdate();

            // Eliminar el archivo de foto si existe
            if (resultado > 0 && foto != null && !foto.isEmpty()) {
                File archivoFoto = new File("fotos_clientes/" + foto);
                if (archivoFoto.exists()) {
                    archivoFoto.delete();
                }
            }
            return resultado > 0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    // Verificar y agregar columna foto si no existe

    public void verificarColumnaFoto() {
        String sql = "PRAGMA table_info(clientes)";
        try (Connection conn = ConexionSQLite.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            boolean existeFoto = false;
            while (rs.next()) {
                if ("foto".equals(rs.getString("name"))) {
                    existeFoto = true;
                    break;
                }
            }

            if (!existeFoto) {
                stmt.execute("ALTER TABLE clientes ADD COLUMN foto TEXT");
                System.out.println("✅ Columna 'foto' agregada a la tabla clientes");
            }

        } catch (Exception e) {
            System.out.println("Error verificando columna foto: " + e.getMessage());
        }
    }

// Obtener cliente por ID (para cargar la foto)
    public Cliente obtenerPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ? AND estado = 1";
        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setFechaRegistro(rs.getString("fecha_registro"));
                c.setFoto(rs.getString("foto"));
                return c;
            }
        } catch (Exception e) {
            System.out.println("Error obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    //-----------------------------Metodo obtener foto-----------------------------------------------//
    // Método auxiliar para obtener la foto por ID
    private String obtenerFotoPorId(int id) {
        String sql = "SELECT foto FROM clientes WHERE id = ?";
        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("foto");
            }
        } catch (Exception e) {
        }
        return null;
    }

    //----------------------------------- Metodo para buscar ------------------------------------------//
    public List<Cliente> buscar(String texto) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, telefono, email, fecha_registro, foto FROM clientes WHERE estado = 1 AND (UPPER(nombre) LIKE UPPER(?) OR UPPER(telefono) LIKE UPPER(?) OR UPPER(email) LIKE UPPER(?)) ORDER BY nombre";

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {

            String filtro = "%" + texto + "%";
            pst.setString(1, filtro);
            pst.setString(2, filtro);
            pst.setString(3, filtro);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setFechaRegistro(rs.getString("fecha_registro"));
                c.setFoto(rs.getString("foto"));  // ← Leer foto
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    //----------------------------------- Metodo existe cliente-----------------------------------------//
    public boolean existeCliente(String telefono, String email) {

        String sql = """
                     SELECT COUNT(*)
                     FROM clientes
                     WHERE telefono = ?
                     OR email = ?
                     """;

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, telefono);
            pst.setString(2, email);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    //-------------------------- Metodo evitar duplicados al actualizar-----------------------------//
    public boolean existeClienteActualizar(int id, String telefono, String email) {
        String sql = """
                     SELECT COUNT(*)
                     FROM clientes
                     WHERE (telefono = ?
                     OR email = ?)
                     AND id <> ?
                     """;

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, telefono);
            pst.setString(2, email);
            pst.setInt(3, id);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    //----------------------------------- Metodo para contar citas-----------------------------------//
    public int contarCitas(int clienteId) {
        String sql = """
                     SELECT COUNT(*)
                     FROM citas
                     WHERE cliente_id = ?
                     """;

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, clienteId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    //------------------------------------- Metodo total gastado------------------------------------//
    public double totalGastado(int clienteId) {
        String sql = """
                     SELECT COALESCE(
                                    SUM(v.total), 0)
                     FROM ventas v
                     WHERE v.cliente_id = ?
                     """;

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, clienteId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    //------------------------------------- Metodo ultima visita------------------------------------//
    public String ultimaVisita(int clienteId) {
        String sql = """
                     SELECT MAX (fecha)
                     FROM citas
                     WHERE cliente_id = ?
                     """;

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, clienteId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String fecha = rs.getString(1);
                return fecha != null ? fecha : "Sin visitas";
            }

        } catch (Exception e) {
            System.out.println("Error ultimaVisita: " + e.getMessage());
        }
        return "Sin visitas";
    }

    //------------------------------------- Metodo servicios frecuentes------------------------------//
    public String serviciosFrecuentes(int clienteId) {
        StringBuilder sb = new StringBuilder();

        String sql = """
                     SELECT DISTINCT  s.nombre                     
                     FROM citas c              
                     INNER JOIN servicios s  ON  c.servicio_id = s.id                     
                     WHERE c.cliente_id = ?
                     ORDER BY s.nombre
                     """;

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, clienteId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                sb.append("• ").append(rs.getString("nombre")).append("\n");
            }

            if (sb.length() == 0) {
                return "No hay servicios registrados";
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Error al cargar servicios";
        }
        return sb.toString();
    }

}
