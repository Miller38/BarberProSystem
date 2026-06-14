package com.barberpro.view;

import javax.swing.*;

import java.awt.*;

public class SplashScreen
        extends JWindow {

    private JProgressBar progress;

    public SplashScreen() {

        initComponents();

        cargar();
    }

    private void initComponents() {

        setSize(700, 500);

        setLocationRelativeTo(null);

        JPanel panel =
                new JPanel(
                        new BorderLayout());

        JLabel logo =
                new JLabel(
                        "BARBER PRO",
                        SwingConstants.CENTER);

        logo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        40));

        progress =
                new JProgressBar();

        progress.setStringPainted(true);

        panel.add(
                logo,
                BorderLayout.CENTER);

        panel.add(
                progress,
                BorderLayout.SOUTH);

        add(panel);
    }

    private void cargar() {

        new Thread(() -> {

            try {

                for (int i = 0;
                     i <= 100;
                     i++) {

                    progress.setValue(i);

                    Thread.sleep(20);
                }

                dispose();

            } catch (Exception e) {

                System.out.println(
                        e.getMessage());
            }

        }).start();
    }
}