package com.barberpro.view;

import com.barberpro.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsuariosPanel extends JPanel {

    // FORMULARIO
    public JTextField txtNombre;
    public JTextField txtUsuario;
    public JPasswordField txtPassword;
    public JTextField txtEmail;
    public JTextField txtTelefono;

    public JComboBox<String> cbRol;

    // BUSQUEDA
    public JTextField txtBuscar;

    // BOTONES
    public JButton btnNuevo;
    public JButton btnGuardar;
    public JButton btnActualizar;
    public JButton btnEliminar;

    public JButton btnBloquear;
    public JButton btnDesbloquear;

    public JButton btnActivar;
    public JButton btnDesactivar;

    // TABLA
    public JTable tabla;
    public DefaultTableModel modelo;

    public UsuariosPanel() {

        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10,10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        15,15,15,15));

        //=================================================
        // FORMULARIO
        //=================================================

        txtNombre = new JTextField();
        txtUsuario = new JTextField();
        txtPassword = new JPasswordField();

        txtEmail = new JTextField();
        txtTelefono = new JTextField();

        cbRol = new JComboBox<>();

        cbRol.addItem("ADMIN");
        cbRol.addItem("EMPLEADO");
        cbRol.addItem("CAJERO");

        JPanel panelLabels =
                new JPanel(
                        new GridLayout(
                                1,6,10,5));

        panelLabels.add(
                new JLabel("Nombre"));

        panelLabels.add(
                new JLabel("Usuario"));

        panelLabels.add(
                new JLabel("Contraseña"));

        panelLabels.add(
                new JLabel("Rol"));

        panelLabels.add(
                new JLabel("Email"));

        panelLabels.add(
                new JLabel("Teléfono"));

        JPanel panelCampos =
                new JPanel(
                        new GridLayout(
                                1,6,10,5));

        panelCampos.add(txtNombre);
        panelCampos.add(txtUsuario);
        panelCampos.add(txtPassword);
        panelCampos.add(cbRol);
        panelCampos.add(txtEmail);
        panelCampos.add(txtTelefono);

        //=================================================
        // BOTONES
        //=================================================

        btnNuevo =
                new JButton("Nuevo");

        btnGuardar =
                new JButton("Guardar");

        btnActualizar =
                new JButton("Actualizar");

        btnEliminar =
                new JButton("Eliminar");

        btnBloquear =
                new JButton("Bloquear");

        btnDesbloquear =
                new JButton("Desbloquear");

        btnActivar =
                new JButton("Activar");

        btnDesactivar =
                new JButton("Desactivar");

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT));

        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        panelBotones.add(btnBloquear);
        panelBotones.add(btnDesbloquear);

        panelBotones.add(btnActivar);
        panelBotones.add(btnDesactivar);

        //=================================================
        // COLORES
        //=================================================

        btnGuardar.setBackground(
                new Color(46,204,113));

        btnGuardar.setForeground(
                Color.WHITE);

        btnActualizar.setBackground(
                new Color(52,152,219));

        btnActualizar.setForeground(
                Color.WHITE);

        btnEliminar.setBackground(
                new Color(231,76,60));

        btnEliminar.setForeground(
                Color.WHITE);

        btnBloquear.setBackground(
                new Color(230,126,34));

        btnBloquear.setForeground(
                Color.WHITE);

        btnDesbloquear.setBackground(
                new Color(39,174,96));

        btnDesbloquear.setForeground(
                Color.WHITE);

        //=================================================
        // BUSCAR
        //=================================================

        txtBuscar =
                new JTextField(30);

        JPanel panelBuscar =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT));

        panelBuscar.add(
                new JLabel("Buscar"));

        panelBuscar.add(txtBuscar);

        //=================================================
        // PANEL SUPERIOR
        //=================================================

        JPanel topPanel =
                new JPanel();

        topPanel.setLayout(
                new BoxLayout(
                        topPanel,
                        BoxLayout.Y_AXIS));

        topPanel.add(panelLabels);
        topPanel.add(Box.createVerticalStrut(5));

        topPanel.add(panelCampos);
        topPanel.add(Box.createVerticalStrut(10));

        topPanel.add(panelBotones);
        topPanel.add(Box.createVerticalStrut(10));

        topPanel.add(panelBuscar);

        //=================================================
        // TABLA
        //=================================================

        modelo =
                new DefaultTableModel();

        modelo.setColumnIdentifiers(
                new Object[]{

                        "ID",
                        "Nombre",
                        "Usuario",
                        "Rol",
                        "Email",
                        "Teléfono",
                        "Estado",
                        "Bloqueado",
                        "Último Acceso"
                });

        tabla =
                new JTable(modelo);

        tabla.setRowHeight(30);

        tabla.getTableHeader()
                .setReorderingAllowed(false);

        JScrollPane scroll =
                new JScrollPane(tabla);

        add(topPanel,
                BorderLayout.NORTH);

        add(scroll,
                BorderLayout.CENTER);
    }
}