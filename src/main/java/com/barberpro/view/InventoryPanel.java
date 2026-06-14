package com.barberpro.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Miller
 */
public class InventoryPanel extends JPanel {

    public JTextField txtNombre;
    public JTextField txtCategoria;
    public JTextField txtStock;
    public JTextField txtStockMin;
    public JTextField txtCompra;
    public JTextField txtVenta;
    public JTextField txtVencimiento;
    public JTextField txtBuscar;
    public JButton btnGuardar;
    public JButton btnActualizar;
    public JButton btnEliminar;
    public JButton btnNuevo;

    public JTable tabla;
    public DefaultTableModel modelo;

    public InventoryPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(2, 7, 10, 10));

        txtNombre = new JTextField();
        txtCategoria = new JTextField();
        txtStock = new JTextField();
        txtStockMin = new JTextField();
        txtCompra = new JTextField();
        txtVenta = new JTextField();
        txtVencimiento = new JTextField();

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnNuevo = new JButton("Nuevo");
        
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

        form.add(new JLabel("Nombre"));
        form.add(new JLabel("Categoria"));
        form.add(new JLabel("Stock"));
        form.add(new JLabel("Stock minimo"));
        form.add(new JLabel("Compra"));
        form.add(new JLabel("Venta"));
        form.add(new JLabel("Vencimiento"));

        form.add(txtNombre);
        form.add(txtCategoria);
        form.add(txtStock);
        form.add(txtStockMin);
        form.add(txtCompra);
        form.add(txtVenta);
        form.add(txtVencimiento);

        JPanel topPanel = new JPanel();

        topPanel.setLayout(
                new BorderLayout(
                        0,
                        10));

        topPanel.add(
                form,
                BorderLayout.NORTH);

        JPanel panelBuscar
                = new JPanel(
                        new java.awt.FlowLayout(
                                java.awt.FlowLayout.LEFT));

        panelBuscar.add(
                new JLabel("Buscar:"));

        txtBuscar
                = new JTextField(25);

        panelBuscar.add(txtBuscar);

        topPanel.add(
                panelBuscar,
                BorderLayout.SOUTH);
        
        add(
        topPanel,
        BorderLayout.NORTH);

        modelo = new DefaultTableModel();

        modelo.setColumnIdentifiers(
                new Object[]{
                    "ID",
                    "Producto",
                    "Categoria",
                    "Stock",
                    "Minimo",
                    "Compra",
                    "Venta",
                    "Fecha Vencimiento",
                    "Estado"

                });

        tabla = new JTable(modelo);
        tabla.setRowHeight(35);

        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel();

        bottom.add(btnGuardar);
        bottom.add(btnActualizar);
        bottom.add(btnEliminar);
        bottom.add(btnNuevo);

        add(bottom, BorderLayout.SOUTH);
    }

}
