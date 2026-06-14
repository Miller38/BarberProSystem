package com.barberpro.dao;

import com.barberpro.config.ConexionSQLite;
import com.barberpro.util.FechaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SesionDAO {

    public void iniciarSesion(
            String usuario) {

        String sql = """
                INSERT INTO sesiones
                (usuario, login)
                VALUES (?, ?)
                """;

        try(Connection conn =
                    ConexionSQLite.conectar();

            PreparedStatement pst =
                    conn.prepareStatement(sql)) {

            pst.setString(1, usuario);
            pst.setString(2, FechaUtil.ahora());

            pst.executeUpdate();

        } catch(Exception e) {

            System.out.println(e.getMessage());
        }
    }
}