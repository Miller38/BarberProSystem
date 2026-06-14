
package com.barberpro.dao;

import com.barberpro.config.ConexionSQLite;
import com.barberpro.model.Cita;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miller
 */
public class CitaDAO {
    
    // Horarios disponibles (debe estar como variable de instancia)
    private String[] horarios = {
        "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
        "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
        "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
        "18:00", "18:30", "19:00"
    };
    
    //------------------------------Metodo para insertar cita ----------------------------------//
    public boolean insertar(Cita cita)  {
        
        String sql = """
                     INSERT INTO citas
                     (cliente_id, 
                     servicio_id,
                     fecha,
                     hora,
                     estado)
                     VALUES (?, ?, ?, ?, ?)
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1,cita.getClienteId());
            pst.setInt(2,cita.getServicioId());
            pst.setString(3,cita.getFecha());
            pst.setString(4,cita.getHora());
            pst.setString(5,cita.getEstado());
            
            pst.executeUpdate();
            return true;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
     //------------------------------Metodo para listar cita -----------------------------------//
    public List<Cita> listar()  {
        List<Cita> lista = new ArrayList<>();
        
        String sql = """
                     SELECT 
                            c.id,
                            cl.nombre AS cliente,
                            s.nombre AS servicio,
                            c.fecha,
                            c.hora,
                            c.estado
                     
                     FROM  citas c
                     
                     INNER JOIN clientes cl
                            ON c.cliente_id = cl.id
                     
                     INNER JOIN servicios s
                            ON c.servicio_id = s.id
                     
                     ORDER BY c.id DESC
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {
            
            while(rs.next()) {
                
                System.out.println(
        "ID: " + rs.getInt("id")
        + " Estado: "
        + rs.getString("estado"));
                
                Cita c = new Cita();
                
                c.setId(rs.getInt("id"));
                c.setCliente(rs.getString("cliente"));
                c.setServicio(rs.getString("servicio"));
                c.setFecha(rs.getString("fecha"));
                c.setHora(rs.getString("hora"));
                c.setEstado(rs.getString("estado"));
                
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()); 
        }
        return lista;
    }
    
    //-------------------------------Metodo horario disponible ------------------------------//
    public boolean horarioDisponible(
        String fecha,
        String hora) {

    String sql = """
        SELECT id FROM citas
        WHERE fecha = ?
        AND hora = ?
        AND estado != 'Cancelada'
        """;

    try (
            Connection conn =
                    ConexionSQLite.conectar();

            PreparedStatement ps =
                    conn.prepareStatement(sql)) {

        ps.setString(1, fecha);

        ps.setString(2, hora);

        ResultSet rs =
                ps.executeQuery();

        return !rs.next();

    } catch (Exception e) {

        System.out.println(e.getMessage());

        return false;
    }
}
    
      //----------------------Metodo para actualizar estado de la cita ------------------------//
    public boolean actualizarEstado(int id, String estado) {
        
        String sql = """
                     UPDATE citas
                     SET estado = ?
                     WHERE id = ?
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1,estado);
            pst.setInt(2,id);
            
            int filas = pst.executeUpdate();
           
            System.out.println("ID :" + id);
            System.out.println("Estado :" + estado);
            System.out.println("Filas afectadas : " + filas);
            
            return filas > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
    }
    
      //----------------------Metodo para contar las citas de hoy----- ------------------------//
    public int totalCitasHoy() {
        
        String sql = """
                     SELECT COUNT(*) total
                     FROM citas
                     WHERE fecha = date('now')
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {
            
            if(rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    
     //---------------------------Metodo para actualizar cita----- ------------------------//
    public boolean  actualizar(Cita cita) {
        String sql = """
                     UPDATE citas
                     SET cliente_id = ?,
                            servicio_id = ?,
                            fecha = ?,
                            hora = ?,
                            estado = ?
                     WHERE id = ?
                     """;
        
        try(Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1,cita.getClienteId());
            pst.setInt(2,cita.getServicioId());
            pst.setString(3,cita.getFecha());
            pst.setString(4,cita.getHora());
            pst.setString(5,cita.getEstado());
            pst.setInt(6,cita.getId());
            
            int filas = pst.executeUpdate();
            return filas > 0;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
     //---------------------------Metodo para eliminar cita----- ------------------------//
    public boolean eliminar(int id) {
    String sql = "DELETE FROM citas WHERE id = ?";
    
    try (Connection conn = ConexionSQLite.conectar();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setInt(1, id);
        int filas = pst.executeUpdate();
        return filas > 0;  // ✅ Retorna true solo si eliminó algo
    } catch (Exception e) {
        System.out.println(e.getMessage());
        return false;
    }
}
    //---------------------------Metodo para fecha valida------ ------------------------//
    // Validar que la fecha no sea pasada
public boolean fechaValida(String fecha) {
    return fecha.compareTo(java.time.LocalDate.now().toString()) >= 0;
}

// Validar horario laboral (ejemplo: 9am a 7pm)
public boolean horarioLaboral(String hora) {
    try {
        int horaInt = Integer.parseInt(hora.split(":")[0]);
        return horaInt >= 9 && horaInt <= 19; // 9am a 7pm
    } catch (Exception e) {
        return false;
    }
}

// Contar citas en una hora específica (límite)
public int contarCitasEnHorario(String fecha, String hora) {
    String sql = "SELECT COUNT(*) FROM citas WHERE fecha = ? AND hora = ? AND estado != 'Cancelada'";
    try (Connection conn = ConexionSQLite.conectar();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, fecha);
        pst.setString(2, hora);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {}
    return 0;
}
    //------------------------------------Filtro por fechas ---------------------------------//
// Listar citas por rango de fechas
public List<Cita> listarPorRango(String fechaInicio, String fechaFin) {
    List<Cita> lista = new ArrayList<>();
    String sql = """
        SELECT c.id, cl.nombre AS cliente, s.nombre AS servicio,
               c.fecha, c.hora, c.estado
        FROM citas c
        INNER JOIN clientes cl ON c.cliente_id = cl.id
        INNER JOIN servicios s ON c.servicio_id = s.id
        WHERE c.fecha BETWEEN ? AND ?
        ORDER BY c.fecha, c.hora
    """;
    
    try (Connection conn = ConexionSQLite.conectar();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, fechaInicio);
        pst.setString(2, fechaFin);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Cita c = new Cita();
            c.setId(rs.getInt("id"));
            c.setCliente(rs.getString("cliente"));
            c.setServicio(rs.getString("servicio"));
            c.setFecha(rs.getString("fecha"));
            c.setHora(rs.getString("hora"));
            c.setEstado(rs.getString("estado"));
            lista.add(c);
        }
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    return lista;
}

// Listar citas de hoy
public List<Cita> listarCitasHoy() {
    return listarPorRango(java.time.LocalDate.now().toString(), 
                          java.time.LocalDate.now().toString());
}
    
    // 1b. Verificar si existe cita en horario (excluyendo una cita)
    public boolean existeCitaEnHorario(String fecha, String hora, int idExcluir) {
        String sql = "SELECT id FROM citas WHERE fecha = ? AND hora = ? AND estado != 'Cancelada' AND id != ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, fecha);
            pst.setString(2, hora);
            pst.setInt(3, idExcluir);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }
    
    // 1c. Obtener horarios ocupados para una fecha
    public List<String> getHorariosOcupados(String fecha) {
        List<String> horarios = new ArrayList<>();
        String sql = "SELECT hora FROM citas WHERE fecha = ? AND estado != 'Cancelada'";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, fecha);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                horarios.add(rs.getString("hora"));
            }
        } catch (Exception e) {}
        return horarios;
    }
    
    // 4. Contar citas por estado
    public int contarPorEstado(String estado) {
        String sql = "SELECT COUNT(*) total FROM citas WHERE estado = ? AND fecha >= date('now')";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, estado);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {}
        return 0;
    }
    //-----------------------Sistema envio correos -----------------------------------------------//
    // Obtener citas del día siguiente (para recordatorios) 
public List<Cita> listarCitasManana() {
    List<Cita> lista = new ArrayList<>();
    String fechaManana = java.time.LocalDate.now().plusDays(1).toString();
    System.out.println("Buscando citas para: " + fechaManana);
   
    
    String sql = "SELECT c.id, cl.nombre AS cliente, s.nombre AS servicio, c.fecha, c.hora, c.estado " +
                 "FROM citas c " +
                 "INNER JOIN clientes cl ON c.cliente_id = cl.id " +
                 "INNER JOIN servicios s ON c.servicio_id = s.id " +
                 "WHERE c.fecha = ? AND c.estado NOT IN ('Cancelada', 'Finalizada')";
    try (Connection conn = ConexionSQLite.conectar();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, fechaManana);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Cita c = new Cita();
            c.setId(rs.getInt("id"));
            c.setCliente(rs.getString("cliente"));
            c.setServicio(rs.getString("servicio"));
            c.setFecha(rs.getString("fecha"));
            c.setHora(rs.getString("hora"));
            c.setEstado(rs.getString("estado"));
            lista.add(c);
        }
    } catch (Exception e) {
        System.out.println("Error listar citas mañana: " + e.getMessage());
    }
    return lista;
}
    
    // 3. Filtrar citas por fecha y estado
    public List<Cita> filtrar(String fecha, String estado) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.id, cl.nombre AS cliente, s.nombre AS servicio, c.fecha, c.hora, c.estado FROM citas c INNER JOIN clientes cl ON c.cliente_id = cl.id INNER JOIN servicios s ON c.servicio_id = s.id WHERE (c.fecha = ? OR ? = '') AND (c.estado = ? OR ? = 'Todos') ORDER BY c.fecha, c.hora";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, fecha.isEmpty() ? "%" : fecha);
            pst.setString(2, fecha);
            pst.setString(3, estado.equals("Todos") ? "%" : estado);
            pst.setString(4, estado);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Cita c = new Cita();
                c.setId(rs.getInt("id"));
                c.setCliente(rs.getString("cliente"));
                c.setServicio(rs.getString("servicio"));
                c.setFecha(rs.getString("fecha"));
                c.setHora(rs.getString("hora"));
                c.setEstado(rs.getString("estado"));
                lista.add(c);
            }
        } catch (Exception e) {}
        return lista;
    }
    

 
}
