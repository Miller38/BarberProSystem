package com.barberpro.dao;

import com.barberpro.config.ConexionSQLite;
import com.barberpro.util.FechaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AuditoriaDAO {

    public void registrar(
            String usuario,
            String accion) {

        String sql = """
                INSERT INTO auditoria
                (usuario, accion, fecha)
                VALUES (?, ?, ?)
                """;

        try(Connection conn =
                    ConexionSQLite.conectar();

            PreparedStatement pst =
                    conn.prepareStatement(sql)) {

            pst.setString(1, usuario);
            pst.setString(2, accion);
            pst.setString(3, FechaUtil.ahora());

            pst.executeUpdate();

        } catch(Exception e) {

            System.out.println(e.getMessage());
        }
    }
}