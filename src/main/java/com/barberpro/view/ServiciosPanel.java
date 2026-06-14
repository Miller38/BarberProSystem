package com.barberpro.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServiciosPanel extends JPanel {

    public JTextField txtNombre;
    public JTextField txtPrecio;
    public JTextField txtBuscar;

    public JButton btnGuardar;
    public JButton btnActualizar;
    public JButton btnEliminar;
    public JButton btnNuevo;
    public JComboBox<String> cbCategoria;

    public JTable tabla;
    public DefaultTableModel modelo;

    public ServiciosPanel() {
        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15));

        // ==================================================
        // CAMPOS
        // ==================================================
        txtNombre = new JTextField();
        txtPrecio = new JTextField();

        cbCategoria = new JComboBox<>();

        cbCategoria.addItem("Cabello");
        cbCategoria.addItem("Barba");
        cbCategoria.addItem("Facial");
        cbCategoria.addItem("Combo");
        cbCategoria.addItem("Tratamiento");
        txtBuscar = new JTextField(30);

        // ==================================================
        // BOTONES
        // ==================================================
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnNuevo = new JButton("Nuevo");

        // ==================================================
        // PANEL SUPERIOR
        // ==================================================
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // --------------------------------------------------
        // FILA LABELS
        // --------------------------------------------------
        JPanel filaLabels = new JPanel(
                new GridLayout(1, 3, 10, 5));

        filaLabels.add(new JLabel("Servicio"));
        filaLabels.add(new JLabel("Categoría"));
        filaLabels.add(new JLabel("Precio"));

        // --------------------------------------------------
        // FILA CAMPOS
        // --------------------------------------------------
        JPanel filaCampos = new JPanel(
                new GridLayout(1, 3, 10, 5));

        filaCampos.add(txtNombre);
        filaCampos.add(cbCategoria);
        filaCampos.add(txtPrecio);

        // --------------------------------------------------
        // FILA BOTONES
        // --------------------------------------------------
        JPanel filaBotones = new JPanel(
                new GridLayout(1, 4, 10, 5));

        filaBotones.add(btnGuardar);
        filaBotones.add(btnActualizar);
        filaBotones.add(btnEliminar);
        filaBotones.add(btnNuevo);

        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);

        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);

        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);

        btnNuevo.setBackground(new Color(241, 196, 15));
        btnNuevo.setForeground(Color.BLACK);

        btnGuardar.setFocusPainted(false);
        btnActualizar.setFocusPainted(false);
        btnEliminar.setFocusPainted(false);
        btnNuevo.setFocusPainted(false);

        btnGuardar.setOpaque(true);
        btnActualizar.setOpaque(true);
        btnEliminar.setOpaque(true);
        btnNuevo.setOpaque(true);

        // --------------------------------------------------
        // FILA BUSQUEDA
        // --------------------------------------------------
        JPanel panelBuscar = new JPanel(
                new FlowLayout(FlowLayout.LEFT));

        panelBuscar.add(new JLabel("Buscar:"));
        panelBuscar.add(txtBuscar);

        // --------------------------------------------------
        // AGREGAR COMPONENTES
        // --------------------------------------------------
        topPanel.add(filaLabels);
        topPanel.add(Box.createVerticalStrut(5));

        topPanel.add(filaCampos);
        topPanel.add(Box.createVerticalStrut(10));

        topPanel.add(filaBotones);
        topPanel.add(Box.createVerticalStrut(10));

        topPanel.add(panelBuscar);

        // ==================================================
        // TABLA
        // ==================================================
        modelo = new DefaultTableModel();

        modelo.setColumnIdentifiers(
                new Object[]{
                    "ID",
                    "Servicio",
                    "Categoria",
                    "Precio"
                });

        tabla = new JTable(modelo);

        tabla.setRowHeight(35);

        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll
                = new JScrollPane(tabla);

        // ==================================================
        // AGREGAR AL PANEL
        // ==================================================
        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }
}
