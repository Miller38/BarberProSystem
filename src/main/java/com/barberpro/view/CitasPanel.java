package com.barberpro.view;

import com.barberpro.model.Cliente;
import com.barberpro.model.Servicio;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.DefaultComboBoxModel;

public class CitasPanel extends JPanel {

    // Componentes del formulario
    public JComboBox<Cliente> cbClientes;
    public JComboBox<Servicio> cbServicios;
    public DatePicker datePicker;
    public JComboBox<String> cbHoras;  // Reemplaza TimePicker
    public JComboBox<String> cbEstado;
    
    // Botones
    public JButton btnGuardar;
    public JButton btnActualizar;
    public JButton btnEliminar;
    public JButton btnNuevo;
    public JButton btnCambiarEstado;
    public JButton btnCitasHoy;
    public JButton btnExportar;
    public JButton btnEnviarRecordatorios;
    public JButton btnWhatsApp;
    
    // Filtros
    public JTextField txtFiltrarFecha;
    public JComboBox<String> cbFiltrarEstado;
    
    // Panel de resumen
    public JLabel lblCitasHoy;
    public JLabel lblCitasPendientes;
    public JLabel lblCitasConfirmadas;
    
    // Tabla
    public JTable tabla;
    public DefaultTableModel modelo;
    
    // Horarios disponibles
    private String[] horarios = {
        "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
        "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
        "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
        "18:00", "18:30", "19:00"
    };

    public CitasPanel() {
        initComponents();
        configurarTablaPorTema();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // =================================================
        // PANEL DE RESUMEN (Mejora #4)
        // =================================================
        JPanel panelResumen = new JPanel(new GridLayout(1, 4, 15, 5));
        panelResumen.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            "RESUMEN DEL DÍA",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 11)
        ));
        
        lblCitasHoy = new JLabel("Citas hoy: 0");
        lblCitasPendientes = new JLabel("Pendientes: 0");
        lblCitasConfirmadas = new JLabel("Confirmadas: 0");
        lblCitasHoy.setFont(new Font("Arial", Font.BOLD, 12));
        lblCitasPendientes.setFont(new Font("Arial", Font.BOLD, 12));
        lblCitasConfirmadas.setFont(new Font("Arial", Font.BOLD, 12));
        
        panelResumen.add(lblCitasHoy);
        panelResumen.add(lblCitasPendientes);
        panelResumen.add(lblCitasConfirmadas);

        // =================================================
        // PANEL DE FORMULARIO
        // =================================================
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            "DATOS DE LA CITA",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12)
        ));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Cliente
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        cbClientes = new JComboBox<>();
        cbClientes.setPreferredSize(new Dimension(200, 30));
        formPanel.add(cbClientes, gbc);

        // Fila 1: Servicio
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Servicio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        cbServicios = new JComboBox<>();
        cbServicios.setPreferredSize(new Dimension(200, 30));
        formPanel.add(cbServicios, gbc);

        // Fila 2: Fecha y Hora (Mejora #2 - ComboBox de horas)
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        datePicker = new DatePicker();
        datePicker.setPreferredSize(new Dimension(150, 30));
        formPanel.add(datePicker, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        formPanel.add(new JLabel("Hora:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2;
        cbHoras = new JComboBox<>(horarios);
        cbHoras.setPreferredSize(new Dimension(100, 30));
        formPanel.add(cbHoras, gbc);

        // Fila 3: Estado
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        cbEstado = new JComboBox<>(new String[]{"Pendiente", "Confirmada", "Finalizada", "Cancelada"});
        cbEstado.setPreferredSize(new Dimension(150, 30));
        formPanel.add(cbEstado, gbc);

        formContainer.add(formPanel, BorderLayout.CENTER);

        // =================================================
        // PANEL DE BOTONES
        // =================================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelBotones.setOpaque(false);

        btnGuardar = crearBoton("Guardar", new Color(46, 204, 113));
        btnActualizar = crearBoton("Actualizar", new Color(52, 152, 219));
        btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60));
        btnNuevo = crearBoton("Nuevo", new Color(241, 196, 15));
        btnCambiarEstado = crearBoton("Cambiar Estado", new Color(155, 89, 182));
        btnCitasHoy = crearBoton("Citas de Hoy", new Color(52, 152, 219));
        btnExportar = crearBoton("Exportar", new Color(52, 152, 219));
        btnEnviarRecordatorios = crearBoton("Enviar Recordatorios", new Color(46, 204, 113));
        btnWhatsApp = crearBoton("WhatsApp", new Color(37, 211, 102));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnNuevo);
        panelBotones.add(btnCambiarEstado);
        panelBotones.add(btnCitasHoy);
        panelBotones.add(btnExportar);
        panelBotones.add(btnEnviarRecordatorios);
        panelBotones.add(btnWhatsApp);

        // =================================================
        // PANEL DE FILTROS (Mejora #3)
        // =================================================
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("FILTROS"));
        
        panelFiltros.add(new JLabel("Fecha:"));
        txtFiltrarFecha = new JTextField(12);
        panelFiltros.add(txtFiltrarFecha);
        
        panelFiltros.add(new JLabel("Estado:"));
        cbFiltrarEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "Confirmada", "Finalizada", "Cancelada"});
        panelFiltros.add(cbFiltrarEstado);

        // =================================================
        // TABLA
        // =================================================
        modelo = new DefaultTableModel(
            new Object[]{"ID", "Cliente", "Servicio", "Fecha", "Hora", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setShowGrid(true);

        // Ajustar ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("LISTADO DE CITAS"));

        // =================================================
        // PANEL SUPERIOR (Resumen + Formulario + Botones)
        // =================================================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JPanel formularioYBotones = new JPanel(new BorderLayout());
        formularioYBotones.add(formContainer, BorderLayout.CENTER);
        formularioYBotones.add(panelBotones, BorderLayout.SOUTH);
        
        topPanel.add(panelResumen, BorderLayout.NORTH);
        topPanel.add(formularioYBotones, BorderLayout.CENTER);
        topPanel.add(panelFiltros, BorderLayout.SOUTH);

        // =================================================
        // ENSAMBLAJE FINAL
        // =================================================
        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void configurarTablaPorTema() {
        boolean esTemaOscuro = esTemaOscuro();
        
        if (esTemaOscuro) {
            tabla.setBackground(new Color(43, 43, 43));
            tabla.setForeground(Color.WHITE);
            tabla.setGridColor(new Color(80, 80, 80));
            tabla.getTableHeader().setBackground(new Color(60, 60, 60));
            tabla.getTableHeader().setForeground(Color.WHITE);
        } else {
            tabla.setBackground(Color.WHITE);
            tabla.setForeground(Color.BLACK);
            tabla.setGridColor(new Color(200, 200, 200));
            tabla.getTableHeader().setBackground(new Color(220, 220, 220));
            tabla.getTableHeader().setForeground(Color.BLACK);
        }
        
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setSelectionBackground(new Color(52, 152, 219));
        tabla.setSelectionForeground(Color.WHITE);
    }

    private boolean esTemaOscuro() {
        Color fondo = UIManager.getColor("Panel.background");
        if (fondo == null) return false;
        double luminosidad = (0.299 * fondo.getRed() + 0.587 * fondo.getGreen() + 0.114 * fondo.getBlue()) / 255;
        return luminosidad < 0.5;
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
    
    public void refrescarPorTema() {
        configurarTablaPorTema();
        tabla.repaint();
    }
    
    // Método para actualizar el panel de resumen
    public void actualizarResumen(int totalHoy, int pendientes, int confirmadas) {
        lblCitasHoy.setText("Citas hoy: " + totalHoy);
        lblCitasPendientes.setText("Pendientes: " + pendientes);
        lblCitasConfirmadas.setText("Confirmadas: " + confirmadas);
    }
    
  

// Método corregido para actualizar horarios disponibles
public void actualizarHorariosDisponibles(java.util.List<String> ocupados) {
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
    
    // Usar la variable de instancia horarios (que ya existe en la clase)
    for (String hora : horarios) {
        if (!ocupados.contains(hora)) {
            model.addElement(hora);
        }
    }
    
    // Si no hay horarios disponibles, mostrar mensaje
    if (model.getSize() == 0) {
        model.addElement("No hay horarios disponibles");
    }
    
    cbHoras.setModel(model);
}
}   