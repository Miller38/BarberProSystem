package com.barberpro.dao;

import com.barberpro.config.ConexionSQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardDAO {

    // TOTAL CLIENTES
    public int totalClientes() {

        String sql = """
            SELECT COUNT(*) total
            FROM clientes
            WHERE estado = 1
            """;

        try (
                Connection conn =
                        ConexionSQLite.conectar();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()) {

            if (rs.next()) {

                return rs.getInt("total");
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return 0;
    }

    // TOTAL CITAS HOY
    public int totalCitasHoy() {

        String sql = """
            SELECT COUNT(*) total
            FROM citas
            WHERE fecha = date('now')
            """;

        try (
                Connection conn =
                        ConexionSQLite.conectar();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()) {

            if (rs.next()) {

                return rs.getInt("total");
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return 0;
    }

    // TOTAL INGRESOS
    public double totalIngresos() {

        String sql = """
            SELECT SUM(total) total
            FROM ventas
            """;

        try (
                Connection conn =
                        ConexionSQLite.conectar();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()) {

            if (rs.next()) {

                return rs.getDouble("total");
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return 0;
    }

    // TOTAL SERVICIOS
    public int totalServicios() {

        String sql = """
            SELECT COUNT(*) total
            FROM servicios
            WHERE estado = 1
            """;

        try (
                Connection conn =
                        ConexionSQLite.conectar();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()) {

            if (rs.next()) {

                return rs.getInt("total");
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return 0;
    }
}