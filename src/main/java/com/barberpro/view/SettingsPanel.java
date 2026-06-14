package com.barberpro.view;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {

    // ==========================
    // TEMAS
    // ==========================
    public JButton btnDark;
    public JButton btnLight;
    public JButton btnDarcula;
    public JButton btnMacDark;
    public JButton btnMonokai;
    public JButton btnOneDark;

    // ==========================
    // FUENTES
    // ==========================
    public JComboBox<String> cbFontSize;
    public JButton btnGuardarFuente;

    // ==========================
    // SISTEMA
    // ==========================
    public JButton btnBackup;
    public JButton btnRestore;

    public SettingsPanel() {

        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout(15, 15));

        setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20));

        // =====================================================
        // PANEL PRINCIPAL
        // =====================================================
        JPanel centerPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                20,
                                20));

        // =====================================================
        // PANEL TEMAS
        // =====================================================
        JPanel panelTemas =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                10,
                                10));

        panelTemas.setBorder(
                BorderFactory.createTitledBorder(
                        "Temas"));

        btnDark =
                new JButton("Dark");

        btnLight =
                new JButton("Light");

        btnDarcula =
                new JButton("Darcula");

        btnMacDark =
                new JButton("Mac Dark");

        btnMonokai =
                new JButton("Monokai Pro");

        btnOneDark =
                new JButton("One Dark");

        panelTemas.add(btnDark);
        panelTemas.add(btnLight);
        panelTemas.add(btnDarcula);
        panelTemas.add(btnMacDark);
        panelTemas.add(btnMonokai);
        panelTemas.add(btnOneDark);

        // =====================================================
        // PANEL FUENTE
        // =====================================================
        JPanel panelFuente =
                new JPanel(
                        new GridBagLayout());

        panelFuente.setBorder(
                BorderFactory.createTitledBorder(
                        "Tamaño de Fuente"));

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        5,
                        5,
                        5,
                        5);

        gbc.gridx = 0;
        gbc.gridy = 0;

        panelFuente.add(
                new JLabel(
                        "Tamaño:"),
                gbc);

        cbFontSize =
                new JComboBox<>(
                        new String[]{
                            "12",
                            "14",
                            "16",
                            "18",
                            "20"
                        });

        gbc.gridx = 1;

        panelFuente.add(
                cbFontSize,
                gbc);

        btnGuardarFuente =
                new JButton(
                        "Guardar");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        panelFuente.add(
                btnGuardarFuente,
                gbc);

        centerPanel.add(panelTemas);
        centerPanel.add(panelFuente);

        // =====================================================
        // PANEL SISTEMA
        // =====================================================
        JPanel panelSistema =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                20,
                                10));

        panelSistema.setBorder(
                BorderFactory.createTitledBorder(
                        "Sistema"));

        btnBackup =
                new JButton(
                        "Crear Backup");

        btnRestore =
                new JButton(
                        "Restaurar Backup");

        panelSistema.add(btnBackup);
        panelSistema.add(btnRestore);

        // =====================================================
        // AGREGAR COMPONENTES
        // =====================================================
        add(centerPanel,
                BorderLayout.CENTER);

        add(panelSistema,
                BorderLayout.SOUTH);
    }
}