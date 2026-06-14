package com.barberpro.dao;

import com.barberpro.config.ConexionSQLite;
import com.barberpro.util.FechaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

public class LoginLogDAO {

    public void registrar(
            String usuario,
            String estado,
            String mensaje) {

        String sql = """
                INSERT INTO logs_login
                (usuario, fecha, estado, mensaje)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn
                = ConexionSQLite.conectar(); PreparedStatement pst
                = conn.prepareStatement(sql)) {

            pst.setString(1, usuario);            
           pst.setString(2, FechaUtil.ahora());
            pst.setString(3, estado);
            pst.setString(4, mensaje);

            pst.executeUpdate();

        } catch (Exception e) {

            System.out.println(
                    "Error log login: "
                    + e.getMessage());
        }
    }
}
