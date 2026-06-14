package com.barberpro.config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Miller
 */
public class ConexionSQLite {

    private static final String URL = "jdbc:sqlite:data/barberia.db";

    public static Connection conectar() {

        try {
            // ✅ Mostrar la RUTA EXACTA de la BD que está usando
            System.out.println("🔍 RUTA DE LA BD: " + URL);
            //----------crea carpeta data donde se guardara la base datos-----------//
            // Crear carpeta data/ si no existe
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            Connection conn = DriverManager.getConnection(URL);

            //----------------------Activar foreign keys----------------------------------//
            try (Statement stmt = conn.createStatement()) {

                java.sql.ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table';");
                System.out.println("--- TABLAS EN LA BD ---");
                while (rs.next()) {
                    System.out.println("Tabla: " + rs.getString("name"));
                }
                System.out.println("------------------------");
                
                stmt.execute("PRAGMA foreign_keys = ON");
                stmt.execute("PRAGMA journal_mode = WAL");
            }
            System.out.println("Conexion SQLite exitosa.");
            return conn;

        } catch (SQLException e) {
            System.out.println("Error de conexion : " + e.getMessage());
            return null;
        }
    }

}
