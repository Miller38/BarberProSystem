package com.barberpro.util;

import com.barberpro.config.FrameManager;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;


import javax.swing.*;

public class ThemeManager {

    // DARK
    public static void darkMode() {
        aplicarTema(new FlatDarkLaf());
    }

    // LIGHT
    public static void lightMode() {
        aplicarTema(new FlatLightLaf());
    }

    // DARCULA
    public static void darculaMode() {
        aplicarTema(new FlatDarculaLaf());
    }

     // MAC DARK
    public static void macDarkMode() {
        aplicarTema(new FlatMacDarkLaf());
    }
    
    // MONOKAI
    public static void monokaiMode() {        
        aplicarTema(new FlatMonokaiProIJTheme());
    }

   // ONE DARK
    public static void oneDarkMode() {        
        aplicarTema(new FlatOneDarkIJTheme());
    }

    // METODO GENERAL
    private static void aplicarTema(
            LookAndFeel laf) {

        try {

            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(
                    FrameManager.getFrame());

            FrameManager.getFrame().repaint();

        } catch (Exception e) {

            System.out.println(
                    e.getMessage());
        }
    }
}