package com.barberpro.controller;

import com.barberpro.config.FontManager;
import com.barberpro.config.FrameManager;
import com.barberpro.dao.ConfiguracionDAO;
import com.barberpro.util.BackupManager;
import com.barberpro.util.ThemeManager;

import com.barberpro.view.SettingsPanel;
import java.awt.Font;

import javax.swing.*;

import java.io.File;

public class SettingsController {

    private SettingsPanel view;

    public SettingsController(
            SettingsPanel view) {

        this.view = view;

        iniciarEventos();
    }

    private void iniciarEventos() {

        // TEMAS
        view.btnDark
                .addActionListener(
                        e -> ThemeManager.darkMode());

        view.btnLight
                .addActionListener(
                        e -> ThemeManager.lightMode());

        view.btnDarcula
                .addActionListener(
                        e -> ThemeManager.darculaMode());

        view.btnMacDark
                .addActionListener(
                        e -> ThemeManager.macDarkMode());

        view.btnMonokai.addActionListener(e -> ThemeManager.monokaiMode());

        view.btnOneDark.addActionListener(e -> ThemeManager.oneDarkMode());

        // BACKUP
        view.btnBackup
                .addActionListener(
                        e -> crearBackup());

        // RESTORE
        view.btnRestore
                .addActionListener(
                        e -> restaurar());

        // fuente
        view.btnGuardarFuente
                .addActionListener(e -> guardarFuente());
    }

    // BACKUP
    private void crearBackup() {

        BackupManager.crearBackup();

        JOptionPane.showMessageDialog(
                null,
                "✅ Backup creado");
    }

    // RESTORE
    private void restaurar() {

        JFileChooser chooser
                = new JFileChooser();

        int opcion
                = chooser.showOpenDialog(null);

        if (opcion
                == JFileChooser.APPROVE_OPTION) {

            File file
                    = chooser.getSelectedFile();

            BackupManager.restaurarBackup(
                    file);

            JOptionPane.showMessageDialog(
                    null,
                    "✅ Backup restaurado");
        }
    }

    //------------------------- Metodo para guardar fuente ------------------------------//
 private void guardarFuente() {

    String size =
            view.cbFontSize
                    .getSelectedItem()
                    .toString();

    Font nuevaFuente =
            new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    Integer.parseInt(size));

    // Guarda el tamaño seleccionado
    FontManager.aplicarFuente(
            Integer.parseInt(size));

    // Actualiza todos los componentes abiertos
    FontManager.actualizarFuente(
            FrameManager.getFrame(),
            nuevaFuente);

    SwingUtilities.updateComponentTreeUI(
            FrameManager.getFrame());

    FrameManager.getFrame().revalidate();
    FrameManager.getFrame().repaint();

    JOptionPane.showMessageDialog(
            null,
            "Tamaño de fuente aplicado");
}
}
