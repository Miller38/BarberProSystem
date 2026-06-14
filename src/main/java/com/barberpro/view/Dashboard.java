package com.barberpro.view;

import com.barberpro.config.FrameManager;
import com.barberpro.config.Session;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import com.barberpro.config.ActualizacionConfig;
import com.barberpro.config.ActualizadorApp;

/**
 *
 * @author Miller
 */
public class Dashboard extends javax.swing.JFrame {

    public boolean sideBarColapsado;

    public Dashboard() {

        // Configurar Look and Feel antes de initComponents
//        configurarLookAndFeel();
        initComponents();

        // ✅ AGREGAR VERIFICACIÓN DE ACTUALIZACIONES AQUÍ
        verificarActualizacionesAlIniciar();

        jPanel2.setLayout(new BorderLayout());
        jPanel2.setBackground(Color.WHITE);
        jPanel2.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Configurar sidebar
        configurarSidebar();

        this.setTitle("BarberPro Dashboard");
        this.setSize(1500, 950);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FrameManager.setFrame(this);

        lblUsuario.setText("👤 " + Session.getUsuarioActual().getNombre());
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(Color.WHITE);

        // Mostrar versión actual
        lblVersion.setText("Versión " + ActualizacionConfig.getVersion());
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(new Color(180, 180, 180));

    }
    
     // -----------------------------------Verificar si existen actualizaciones -------------------------//
    private void verificarActualizacionesAlIniciar() {
        // Usar un Timer para dar tiempo a que el Dashboard se renderice completamente
        javax.swing.Timer timer = new javax.swing.Timer(2000, e -> {
            // Pasar 'this' como el JFrame padre (Dashboard)
            ActualizadorApp.verificarActualizacion(this);
            ((javax.swing.Timer) e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void configurarSidebar() {
        // Configurar panel sidebar
        sidebar.setBackground(new Color(52, 73, 94));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
        sidebar.setPreferredSize(new Dimension(230, 0));

        // Logo / Título
        javax.swing.JLabel lblLogo = new javax.swing.JLabel("BARBER PRO");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Agregar logo al sidebar (ajustar layout)
        sidebar.setLayout(new javax.swing.BoxLayout(sidebar, javax.swing.BoxLayout.Y_AXIS));
        sidebar.add(lblLogo);

        // Configurar todos los botones del sidebar
        configurarBotonMenu(btnDashboard, "Dashboard");
        configurarBotonMenu(btnClientes, "Clientes");
        configurarBotonMenu(btnCitas, "Citas");
        configurarBotonMenu(btnServicios, "Servicios");
        configurarBotonMenu(btnVentas, "Ventas");
        configurarBotonMenu(btnInventario, "Inventario");
        configurarBotonMenu(btnUsuarios, "Usuarios");
        configurarBotonMenu(btnSettings, "Configuración");
        configurarBotonMenu(btnPrueba, "Actualizacion");
       
        

        // Espacio flexible
        sidebar.add(javax.swing.Box.createVerticalGlue());

        // Botón cerrar sesión (con color diferente)
        configurarBotonMenu(btnLogout, "Cerrar sesión", new Color(231, 76, 60));

        // Espacio antes de la versión
        sidebar.add(javax.swing.Box.createVerticalStrut(15));

        // Configurar y agregar el label de versión
        lblVersion.setText("      Versión " + ActualizacionConfig.getVersion());  // ← Agrega esta línea
        sidebar.add(lblVersion);

        // Espacio después de la versión
        sidebar.add(javax.swing.Box.createVerticalStrut(10));
    }

    private void configurarBotonMenu(javax.swing.JButton boton, String texto) {
        configurarBotonMenu(boton, texto, new Color(52, 73, 94));
    }

    private void configurarBotonMenu(javax.swing.JButton boton, String texto, Color colorFondo) {
        boton.setText(texto);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setIconTextGap(15);
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(200, 42));
        boton.setMaximumSize(new Dimension(200, 42));
        boton.setMinimumSize(new Dimension(200, 42));

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!boton.getBackground().equals(new Color(231, 76, 60))) {
                    boton.setBackground(new Color(41, 128, 185));
                } else {
                    boton.setBackground(new Color(192, 57, 43));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
            }
        });

        sidebar.add(boton);
        sidebar.add(javax.swing.Box.createVerticalStrut(5));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebar = new javax.swing.JPanel();
        btnClientes = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnCitas = new javax.swing.JButton();
        btnServicios = new javax.swing.JButton();
        btnVentas = new javax.swing.JButton();
        btnDashboard = new javax.swing.JButton();
        btnSettings = new javax.swing.JButton();
        btnInventario = new javax.swing.JButton();
        btnUsuarios = new javax.swing.JButton();
        lblVersion = new javax.swing.JLabel();
        btnPrueba = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sidebar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        sidebar.setPreferredSize(new java.awt.Dimension(240, 700));

        btnClientes.setText("Clientes");
        btnClientes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnClientes.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnClientes.setIconTextGap(10);

        btnLogout.setText("Cerrar session");
        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogout.setIconTextGap(10);

        btnCitas.setText("Citas");
        btnCitas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCitas.setIconTextGap(10);

        btnServicios.setText("Servicios");
        btnServicios.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnServicios.setIconTextGap(10);

        btnVentas.setText("Ventas");
        btnVentas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnVentas.setIconTextGap(10);

        btnDashboard.setText("Dashboard");
        btnDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDashboard.setIconTextGap(10);

        btnSettings.setText("Settings");
        btnSettings.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSettings.setIconTextGap(10);

        btnInventario.setText("Inventario");
        btnInventario.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInventario.setIconTextGap(10);

        btnUsuarios.setText("Usuarios");
        btnUsuarios.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUsuarios.setIconTextGap(10);

        lblVersion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        btnPrueba.setText("Actualizacion");

        javax.swing.GroupLayout sidebarLayout = new javax.swing.GroupLayout(sidebar);
        sidebar.setLayout(sidebarLayout);
        sidebarLayout.setHorizontalGroup(
            sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidebarLayout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(lblVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
            .addGroup(sidebarLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnClientes, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                    .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCitas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnServicios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPrueba, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sidebarLayout.setVerticalGroup(
            sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarLayout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(btnClientes)
                .addGap(29, 29, 29)
                .addComponent(btnCitas)
                .addGap(29, 29, 29)
                .addComponent(btnServicios)
                .addGap(29, 29, 29)
                .addComponent(btnVentas)
                .addGap(27, 27, 27)
                .addComponent(btnDashboard)
                .addGap(28, 28, 28)
                .addComponent(btnSettings)
                .addGap(28, 28, 28)
                .addComponent(btnInventario)
                .addGap(27, 27, 27)
                .addComponent(btnUsuarios)
                .addGap(26, 26, 26)
                .addComponent(btnPrueba)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 362, Short.MAX_VALUE)
                .addComponent(lblVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnLogout)
                .addGap(51, 51, 51))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(562, 562, 562)
                .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(737, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Dashboard().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCitas;
    public javax.swing.JButton btnClientes;
    public javax.swing.JButton btnDashboard;
    public javax.swing.JButton btnInventario;
    public javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPrueba;
    public javax.swing.JButton btnServicios;
    public javax.swing.JButton btnSettings;
    public javax.swing.JButton btnUsuarios;
    public javax.swing.JButton btnVentas;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JLabel lblUsuario;
    public javax.swing.JLabel lblVersion;
    public javax.swing.JPanel sidebar;
    // End of variables declaration//GEN-END:variables

}
