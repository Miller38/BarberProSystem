package com.barberpro.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.io.File;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class ClientesPanel extends JPanel {

    public JTextField txtNombre;
    public JTextField txtTelefono;
    public JTextField txtEmail;
    public JTextField txtDireccion;
    public JTextField txtBuscar;
    

    public JButton btnGuardar;
    public JButton btnActualizar;
    public JButton btnEliminar;
    public JButton btnNuevo;
    public JButton btnHistorial;
    
    // ← NUEVOS COMPONENTES PARA LA FOTO
    public JLabel lblFoto;
    public JButton btnSeleccionarFoto;
    public JButton btnTomarFoto;
    public JButton btnEliminarFoto;

    public JTable tabla;
    public DefaultTableModel modelo;

    public ClientesPanel() {
        initComponents();
        crearCarpetaFotos();
    }
    
    private void crearCarpetaFotos() {
        File carpeta = new File("fotos_clientes");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // =================================================
        // PANEL CENTRAL: Formulario + Foto
        // =================================================
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // PANEL FORMULARIO (izquierda)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("DATOS DEL CLIENTE"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nombre
       // Fila 0: Nombre
gbc.gridx = 0; gbc.gridy = 0;
formPanel.add(new JLabel("Nombre:"), gbc);
gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
txtNombre = new JTextField(20);
formPanel.add(txtNombre, gbc);

// Fila 1: Teléfono
gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
formPanel.add(new JLabel("Teléfono:"), gbc);
gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
txtTelefono = new JTextField(20);
formPanel.add(txtTelefono, gbc);

// Fila 2: Dirección (CORREGIDO - debe ir antes que Email)
gbc.gridx = 0; gbc.gridy = 2;
formPanel.add(new JLabel("Dirección:"), gbc);  // ← Cambiar "Radicion" a "Dirección"
gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
txtDireccion = new JTextField(20);
formPanel.add(txtDireccion, gbc);

// Fila 3: Email (debajo de Dirección)
gbc.gridx = 0; gbc.gridy = 3;
formPanel.add(new JLabel("Email:"), gbc);
gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
txtEmail = new JTextField(20);
formPanel.add(txtEmail, gbc);
        
        // =================================================
        // PANEL DE FOTO (derecha)
        // =================================================
        JPanel panelFoto = new JPanel(new BorderLayout());
        panelFoto.setBorder(BorderFactory.createTitledBorder("FOTO DEL CLIENTE"));
        panelFoto.setPreferredSize(new Dimension(220, 280));
        
        lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(180, 180));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblFoto.setIcon(crearIconoPorDefecto());
        
        JPanel panelBotonesFoto = new JPanel(new FlowLayout());
        btnSeleccionarFoto = new JButton("📁 Seleccionar");
        btnTomarFoto = new JButton("📷 Cámara");
        btnEliminarFoto = new JButton("🗑️ Eliminar");
        
        btnSeleccionarFoto.setFocusPainted(false);
        btnTomarFoto.setFocusPainted(false);
        btnEliminarFoto.setFocusPainted(false);
        
        panelBotonesFoto.add(btnSeleccionarFoto);
        panelBotonesFoto.add(btnTomarFoto);
        panelBotonesFoto.add(btnEliminarFoto);
        
        panelFoto.add(lblFoto, BorderLayout.CENTER);
        panelFoto.add(panelBotonesFoto, BorderLayout.SOUTH);
        
        // =================================================
        // PANEL BOTONES PRINCIPALES
        // =================================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnGuardar = crearBoton("Guardar", new Color(46, 204, 113));
        btnActualizar = crearBoton("Actualizar", new Color(52, 152, 219));
        btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60));
        btnNuevo = crearBoton("Nuevo", new Color(241, 196, 15));
        btnHistorial = crearBoton("Historial", new Color(155, 89, 182));
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnNuevo);
        panelBotones.add(btnHistorial);
        
        // =================================================
        // PANEL BUSCAR
        // =================================================
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBuscar.add(new JLabel("🔍 Buscar:"));
        txtBuscar = new JTextField(25);
        panelBuscar.add(txtBuscar);
        
        // =================================================
        // ENSAMBLAJE PANEL SUPERIOR
        // =================================================
        JPanel northPanel = new JPanel(new BorderLayout());
        
        JPanel formYFoto = new JPanel(new BorderLayout());
        formYFoto.add(formPanel, BorderLayout.CENTER);
        formYFoto.add(panelFoto, BorderLayout.EAST);
        
        northPanel.add(formYFoto, BorderLayout.CENTER);
        northPanel.add(panelBotones, BorderLayout.SOUTH);
        northPanel.add(panelBuscar, BorderLayout.NORTH);
        
        // =================================================
        // TABLA
        // =================================================
        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[]{"ID", "Nombre", "Teléfono", "Email", "Direccion", "Registro"});
        tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("LISTADO DE CLIENTES"));
        
        // =================================================
        // ENSAMBLAJE FINAL
        // =================================================
        add(northPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
    
   public ImageIcon crearIconoPorDefecto() {
    BufferedImage img = new BufferedImage(180, 180, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = img.createGraphics();
    g2d.setColor(Color.LIGHT_GRAY);
    g2d.fillRect(0, 0, 180, 180);
    g2d.setColor(Color.GRAY);
    g2d.setFont(new Font("Arial", Font.BOLD, 50));
    g2d.drawString("📷", 65, 110);
    g2d.dispose();
    return new ImageIcon(img);
}
    
    public void actualizarFoto(ImageIcon icon) {
    if (lblFoto != null) {
        lblFoto.setIcon(icon);
    }
}
    
    
}