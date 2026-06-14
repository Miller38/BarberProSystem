package com.barberpro.view;

import com.barberpro.model.Cliente;
import com.barberpro.model.Servicio;
import com.barberpro.model.Producto;
import com.barberpro.model.DetalleVenta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentasPanel extends JPanel {

    // Cliente
    public JComboBox<Cliente> cbClientes;
    
    // Tipo de item (Servicio o Producto)
    public JComboBox<String> cbTipoItem;
    
    // Items disponibles
    public JComboBox<Servicio> cbServicios;
    public JComboBox<Producto> cbProductos;
    
    // Cantidad y agregar
    public JSpinner spinnerCantidad;
    public JButton btnAgregarItem;
    
    // Carrito de compra
    public JTable tablaCarrito;
    public DefaultTableModel modeloCarrito;
    public JButton btnQuitarItem;
    
    // Pago
    public JComboBox<String> cbMetodoPago;
    public JTextField txtDescuento;
    public JTextField txtSubtotal;
    public JTextField txtImpuesto;
    public JTextField txtTotal;
    
    // Botones acción
    public JButton btnCobrar;
    public JButton btnNuevo;
    
    // Historial ventas
    public JTextField txtBuscar;
    public JTable tablaVentas;
    public DefaultTableModel modeloVentas;
    public JButton btnAnular;
    public JButton btnVerDetalle;
    
    // Lista temporal de items
    private List<DetalleVenta> itemsCarrito;

    public VentasPanel() {
        itemsCarrito = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel principal dividido
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        
        // ========== PANEL SUPERIOR (Nueva Venta) ==========
        JPanel panelNuevaVenta = new JPanel(new BorderLayout(10, 10));
        
        // Formulario cliente y items
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Fila 0: Cliente
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 3;
        cbClientes = new JComboBox<>();
        formPanel.add(cbClientes, gbc);
        
        // Fila 1: Tipo
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        cbTipoItem = new JComboBox<>(new String[]{"Servicio", "Producto"});
        formPanel.add(cbTipoItem, gbc);
        
        // Fila 1b: Servicios
        gbc.gridx = 2; gbc.gridy = 1;
        cbServicios = new JComboBox<>();
        cbServicios.setPreferredSize(new Dimension(200, 25));
        formPanel.add(cbServicios, gbc);
        
        // Fila 1c: Productos
        gbc.gridx = 2; gbc.gridy = 1;
        cbProductos = new JComboBox<>();
        cbProductos.setPreferredSize(new Dimension(200, 25));
        cbProductos.setVisible(false);
        formPanel.add(cbProductos, gbc);
        
        // Fila 2: Cantidad
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        formPanel.add(spinnerCantidad, gbc);
        
        // Botón Agregar
        gbc.gridx = 2; gbc.gridy = 2;
        btnAgregarItem = new JButton("+ Agregar");
        btnAgregarItem.setBackground(new Color(52, 152, 219));
        btnAgregarItem.setForeground(Color.WHITE);
        formPanel.add(btnAgregarItem, gbc);
        
        // Tabla del carrito
        modeloCarrito = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modeloCarrito.setColumnIdentifiers(new Object[]{"Tipo", "Item", "Cantidad", "Precio", "Subtotal"});
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setRowHeight(30);
        JScrollPane scrollCarrito = new JScrollPane(tablaCarrito);
        scrollCarrito.setPreferredSize(new Dimension(0, 150));
        
        // Botón quitar
        btnQuitarItem = new JButton("Quitar seleccionado");
        btnQuitarItem.setBackground(new Color(231, 76, 60));
        btnQuitarItem.setForeground(Color.WHITE);
        
        // Totales
        JPanel totalesPanel = new JPanel(new GridLayout(1, 8, 10, 5));
        totalesPanel.add(new JLabel("Subtotal:"));
        txtSubtotal = new JTextField();
        txtSubtotal.setEditable(false);
        totalesPanel.add(txtSubtotal);
        
        totalesPanel.add(new JLabel("Impuesto (19%):"));
        txtImpuesto = new JTextField();
        txtImpuesto.setEditable(false);
        totalesPanel.add(txtImpuesto);
        
        totalesPanel.add(new JLabel("Descuento:"));
        txtDescuento = new JTextField("0");
        totalesPanel.add(txtDescuento);
        
        totalesPanel.add(new JLabel("Total:"));
        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        totalesPanel.add(txtTotal);
        
        // Método pago y botones
        JPanel pagoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pagoPanel.add(new JLabel("Método Pago:"));
        cbMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Nequi", "Daviplata", "Transferencia", "Tarjeta"});
        pagoPanel.add(cbMetodoPago);
        
        btnCobrar = new JButton("Cobrar");
        btnCobrar.setBackground(new Color(46, 204, 113));
        btnCobrar.setForeground(Color.WHITE);
        pagoPanel.add(btnCobrar);
        
        btnNuevo = new JButton("Nueva Venta");
        btnNuevo.setBackground(new Color(241, 196, 15));
        pagoPanel.add(btnNuevo);
        
        // Ensamblar panel superior
        JPanel carritoPanel = new JPanel(new BorderLayout(5, 5));
        carritoPanel.add(scrollCarrito, BorderLayout.CENTER);
        carritoPanel.add(btnQuitarItem, BorderLayout.SOUTH);
        
        panelNuevaVenta.add(formPanel, BorderLayout.NORTH);
        panelNuevaVenta.add(carritoPanel, BorderLayout.CENTER);
        panelNuevaVenta.add(totalesPanel, BorderLayout.SOUTH);
        panelNuevaVenta.add(pagoPanel, BorderLayout.EAST);
        
        // ========== PANEL INFERIOR (Historial) ==========
        JPanel panelHistorial = new JPanel(new BorderLayout(5, 5));
        
        JPanel buscarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscarPanel.add(new JLabel("Buscar venta:"));
        txtBuscar = new JTextField(25);
        buscarPanel.add(txtBuscar);
        
        modeloVentas = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modeloVentas.setColumnIdentifiers(new Object[]{"ID", "Cliente", "Total", "Pago", "Fecha", "Estado"});
        tablaVentas = new JTable(modeloVentas);
        tablaVentas.setRowHeight(30);
        JScrollPane scrollVentas = new JScrollPane(tablaVentas);
        
        JPanel botonesHistorial = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAnular = new JButton("Anular Venta");
        btnAnular.setBackground(new Color(231, 76, 60));
        btnAnular.setForeground(Color.WHITE);
        btnVerDetalle = new JButton("Ver Detalle");
        btnVerDetalle.setBackground(new Color(52, 152, 219));
        btnVerDetalle.setForeground(Color.WHITE);
        botonesHistorial.add(btnAnular);
        botonesHistorial.add(btnVerDetalle);
        
        panelHistorial.add(buscarPanel, BorderLayout.NORTH);
        panelHistorial.add(scrollVentas, BorderLayout.CENTER);
        panelHistorial.add(botonesHistorial, BorderLayout.SOUTH);
        
        // Configurar SplitPane
        splitPane.setTopComponent(panelNuevaVenta);
        splitPane.setBottomComponent(panelHistorial);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    public List<DetalleVenta> getItemsCarrito() {
        return itemsCarrito;
    }
    
    public void limpiarCarrito() {
        itemsCarrito.clear();
        modeloCarrito.setRowCount(0);
        txtSubtotal.setText("");
        txtImpuesto.setText("");
        txtTotal.setText("");
        txtDescuento.setText("0");
    }
    
    public void agregarItemAlCarrito(DetalleVenta item) {
        itemsCarrito.add(item);
        modeloCarrito.addRow(new Object[]{
            item.getTipoItem().equals("SERVICIO") ? "Servicio" : "Producto",
            item.getNombre(),
            item.getCantidad(),
            item.getPrecioUnitario(),
            item.getSubtotal()
        });
    }
    
    public void actualizarTotales(double subtotal, double impuesto, double total) {          
     txtSubtotal.setText(formatearDecimal(subtotal));
    txtImpuesto.setText(formatearDecimal(impuesto));
    txtTotal.setText(formatearDecimal(total));
    }
    
    //------------------------------Metodo auxiliar ---------------------------------------//
    private String formatearDecimal(double valor) {
    return String.format("%.2f", valor).replace(",", ".");
}
}