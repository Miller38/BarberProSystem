package com.barberpro.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;

public class EstadoCitaRenderer
        extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        Component c =
                super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column);

        String estado =
                table.getValueAt(row, 5)
                        .toString();

        switch (estado) {

            case "Pendiente":

                c.setBackground(
                        new Color(255, 243, 205));

                break;

            case "Confirmada":

                c.setBackground(
                        new Color(209, 231, 221));

                break;

            case "Finalizada":

                c.setBackground(
                        new Color(207, 226, 255));

                break;

            case "Cancelada":

                c.setBackground(
                        new Color(248, 215, 218));

                break;

            default:

                c.setBackground(Color.WHITE);
        }

        return c;
    }
}