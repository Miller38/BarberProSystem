package com.barberpro.view;

import javax.swing.*;

import java.awt.*;

public class HomePanel extends JPanel {

    public JLabel lblClientes;

    public JLabel lblCitas;

    public JLabel lblIngresos;

    public JLabel lblServicios;

    public JPanel chartPanel;

    public HomePanel() {

        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20));

        // =========================
        // CARDS
        // =========================

        JPanel cards =
                new JPanel(
                        new GridLayout(
                                1, 4, 15, 15));

        lblClientes =
                new JLabel("0");

        lblCitas =
                new JLabel("0");

        lblIngresos =
                new JLabel("$0");

        lblServicios =
                new JLabel("0");

        cards.add(
                crearCard(
                        "Clientes",
                        lblClientes));

        cards.add(
                crearCard(
                        "Citas Hoy",
                        lblCitas));

        cards.add(
                crearCard(
                        "Ingresos",
                        lblIngresos));

        cards.add(
                crearCard(
                        "Servicios",
                        lblServicios));

        // =========================
        // CHART PANEL
        // =========================

        chartPanel =
                new JPanel(
                        new BorderLayout());

        chartPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Ingresos"));

        add(cards,
                BorderLayout.NORTH);

        add(chartPanel,
                BorderLayout.CENTER);
    }

    // CARD
    private JPanel crearCard(
            String titulo,
            JLabel valor) {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS));

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20));

        JLabel lblTitulo =
                new JLabel(titulo);

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        18));

        valor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30));

        panel.add(lblTitulo);

        panel.add(
                Box.createVerticalStrut(10));

        panel.add(valor);

        return panel;
    }
}