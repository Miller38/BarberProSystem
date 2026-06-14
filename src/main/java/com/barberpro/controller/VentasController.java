package com.barberpro.controller;

import com.barberpro.dao.*;
import com.barberpro.model.*;
import com.barberpro.util.PDFGenerator;
import com.barberpro.view.VentasPanel;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class VentasController {

    private VentasPanel view;
    private ClienteDAO clienteDAO;
    private ServicioDAO servicioDAO;
    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;
    
    private int idVentaSeleccionada = -1;

    public VentasController(VentasPanel view) {
        this.view = view;
        clienteDAO = new ClienteDAO();
        servicioDAO = new ServicioDAO();
        productoDAO = new ProductoDAO();
        ventaDAO = new VentaDAO();
        
        iniciarEventos();
        cargarClientes();
        cargarServicios();
        cargarProductos();
        listarVentas();
    }

    private void iniciarEventos() {
        // Tipo de item (Servicio/Producto)
        view.cbTipoItem.addActionListener(e -> toggleTipoItem());
        
        // Agregar item al carrito
        view.btnAgregarItem.addActionListener(e -> agregarItem());
        
        // Quitar item del carrito
        view.btnQuitarItem.addActionListener(e -> quitarItem());
        
        // Calcular totales cuando cambia descuento
        view.txtDescuento.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { recalcularTotales(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { recalcularTotales(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { recalcularTotales(); }
        });
        
        // Cobrar venta
        view.btnCobrar.addActionListener(e -> cobrar());
        
        // Nueva venta (limpiar)
        view.btnNuevo.addActionListener(e -> limpiarFormulario());
        
        // Anular venta
        view.btnAnular.addActionListener(e -> anularVenta());
        
        // Ver detalle de venta
        view.btnVerDetalle.addActionListener(e -> verDetalleVenta());
        
        // Buscar
        view.txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { buscarVentas(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { buscarVentas(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { buscarVentas(); }
        });
        
        // Seleccionar fila en historial
        view.tablaVentas.getSelectionModel().addListSelectionListener(e -> seleccionarVenta());
    }
    
    private void toggleTipoItem() {
        String tipo = (String) view.cbTipoItem.getSelectedItem();
        boolean esProducto = "Producto".equals(tipo);
        view.cbServicios.setVisible(!esProducto);
        view.cbProductos.setVisible(esProducto);
    }
    
    private void cargarClientes() {
        view.cbClientes.removeAllItems();
        for (Cliente c : clienteDAO.listar()) {
            view.cbClientes.addItem(c);
        }
    }
    
    private void cargarServicios() {
        view.cbServicios.removeAllItems();
        for (Servicio s : servicioDAO.listar()) {
            view.cbServicios.addItem(s);
        }
    }
    
    private void cargarProductos() {
        view.cbProductos.removeAllItems();
        for (Producto p : productoDAO.listar()) {
            if (p.getStock() > 0) {  // Solo productos con stock
                view.cbProductos.addItem(p);
            }
        }
    }
    
    private void agregarItem() {
        String tipo = (String) view.cbTipoItem.getSelectedItem();
        int cantidad = (Integer) view.spinnerCantidad.getValue();
        
        if (cantidad < 1) {
            JOptionPane.showMessageDialog(null, "Cantidad inválida");
            return;
        }
        
        if ("Servicio".equals(tipo)) {
            Servicio servicio = (Servicio) view.cbServicios.getSelectedItem();
            if (servicio == null) {
                JOptionPane.showMessageDialog(null, "Seleccione un servicio");
                return;
            }
            
            DetalleVenta detalle = new DetalleVenta();
            detalle.setTipoItem("SERVICIO");
            detalle.setItemId(servicio.getId());
            detalle.setNombre(servicio.getNombre());
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(servicio.getPrecio());
            detalle.setSubtotal(servicio.getPrecio() * cantidad);
            
            view.agregarItemAlCarrito(detalle);
            
        } else { // Producto
            Producto producto = (Producto) view.cbProductos.getSelectedItem();
            if (producto == null) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto");
                return;
            }
            
            // Validar stock
            if (producto.getStock() < cantidad) {
                JOptionPane.showMessageDialog(null, "Stock insuficiente. Disponible: " + producto.getStock());
                return;
            }
            
            DetalleVenta detalle = new DetalleVenta();
            detalle.setTipoItem("PRODUCTO");
            detalle.setItemId(producto.getId());
            detalle.setNombre(producto.getNombre());
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalle.setSubtotal(producto.getPrecioVenta() * cantidad);
            
            view.agregarItemAlCarrito(detalle);
        }
        
        recalcularTotales();
        view.spinnerCantidad.setValue(1);
    }
    
    private void quitarItem() {
        int fila = view.tablaCarrito.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un item del carrito");
            return;
        }
        
        List<DetalleVenta> items = view.getItemsCarrito();
        items.remove(fila);
        ((DefaultTableModel) view.tablaCarrito.getModel()).removeRow(fila);
        recalcularTotales();
    }
    
    private void recalcularTotales() {
        double subtotal = 0;
        for (DetalleVenta d : view.getItemsCarrito()) {
            subtotal += d.getSubtotal();
        }
        
        double impuesto = subtotal * 0.19;
        double descuento = 0;
        try {
            descuento = Double.parseDouble(view.txtDescuento.getText().trim());
        } catch (Exception e) {
            descuento = 0;
        }
        
        double total = subtotal + impuesto - descuento;
        view.actualizarTotales(subtotal, impuesto, total);
    }
    
    private void cobrar() {
        Cliente cliente = (Cliente) view.cbClientes.getSelectedItem();
        List<DetalleVenta> items = view.getItemsCarrito();
        
        if (cliente == null) {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente");
            return;
        }
        
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Agregue al menos un item a la venta");
            return;
        }
        
        try {
            double subtotal = Double.parseDouble(view.txtSubtotal.getText());
            double impuesto = Double.parseDouble(view.txtImpuesto.getText());
            double descuento = Double.parseDouble(view.txtDescuento.getText());
            double total = Double.parseDouble(view.txtTotal.getText());
            
            Venta venta = new Venta();
            venta.setClienteId(cliente.getId());
            venta.setCliente(cliente.getNombre());
            venta.setUsuario("Administrador");
            venta.setSubtotal(subtotal);
            venta.setImpuesto(impuesto);
            venta.setDescuento(descuento);
            venta.setTotal(total);
            venta.setMetodoPago(view.cbMetodoPago.getSelectedItem().toString());
            venta.setFecha(LocalDate.now().toString());
            venta.setEstado("ACTIVA");
            
            // Agregar todos los items del carrito
            for (DetalleVenta d : items) {
                venta.agregarDetalle(d);
            }
            
            boolean ok = ventaDAO.insertar(venta);
            probarStockDirecto();
            if (ok) {
                JOptionPane.showMessageDialog(null, "Venta registrada exitosamente");
                // Generar factura
                generarFactura(venta);
                listarVentas();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar la venta");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void generarFactura(Venta venta) {
        StringBuilder factura = new StringBuilder();
        factura.append("========== FACTURA ==========\n");
        factura.append("Venta #: ").append(venta.getId()).append("\n");
        factura.append("Cliente: ").append(venta.getCliente()).append("\n");
        factura.append("Fecha: ").append(venta.getFecha()).append("\n");
        factura.append("------------------------------\n");
        factura.append("ITEMS:\n");
        
        for (DetalleVenta d : venta.getDetalles()) {
            factura.append(d.getCantidad()).append(" x ")
                   .append(d.getNombre()).append(" - $")
                   .append(d.getPrecioUnitario()).append(" = $")
                   .append(d.getSubtotal()).append("\n");
        }
        
        factura.append("------------------------------\n");
        factura.append("Subtotal: $").append(venta.getSubtotal()).append("\n");
        factura.append("Impuesto (19%): $").append(venta.getImpuesto()).append("\n");
        factura.append("Descuento: $").append(venta.getDescuento()).append("\n");
        factura.append("TOTAL: $").append(venta.getTotal()).append("\n");
        factura.append("Método Pago: ").append(venta.getMetodoPago()).append("\n");
        factura.append("==============================\n");
        
        System.out.println(factura.toString());
        // llamar a PDFGenerator para imprimir factura
        PDFGenerator.generarFactura(venta);
        
    }
    
    private void listarVentas() {
        DefaultTableModel model = (DefaultTableModel) view.tablaVentas.getModel();
        model.setRowCount(0);
        
        for (Venta v : ventaDAO.listar()) {
            model.addRow(new Object[]{
                v.getId(),
                v.getCliente(),
                String.format("%.2f", v.getTotal()),
                v.getMetodoPago(),
                v.getFecha(),
                v.getEstado()
            });
        }
    }
    
    private void buscarVentas() {
        String texto = view.txtBuscar.getText().trim();
        DefaultTableModel model = (DefaultTableModel) view.tablaVentas.getModel();
        model.setRowCount(0);
        
        for (Venta v : ventaDAO.buscar(texto)) {
            model.addRow(new Object[]{
                v.getId(),
                v.getCliente(),
                String.format("%.2f", v.getTotal()),
                v.getMetodoPago(),
                v.getFecha(),
                v.getEstado()
            });
        }
    }
    
    private void seleccionarVenta() {
        int fila = view.tablaVentas.getSelectedRow();
        if (fila == -1) {
            idVentaSeleccionada = -1;
            return;
        }
        idVentaSeleccionada = Integer.parseInt(view.tablaVentas.getValueAt(fila, 0).toString());
    }
    
    private void anularVenta() {
        if (idVentaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una venta");
            return;
        }
        
        int opcion = JOptionPane.showConfirmDialog(null, "¿Anular esta venta?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;
        
        boolean ok = ventaDAO.anular(idVentaSeleccionada);
        if (ok) {
            JOptionPane.showMessageDialog(null, "Venta anulada");
            listarVentas();
            idVentaSeleccionada = -1;
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo anular la venta");
        }
    }
    
    private void verDetalleVenta() {
        if (idVentaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una venta");
            return;
        }
        
        // Buscar la venta completa con detalles
        for (Venta v : ventaDAO.listar()) {
            if (v.getId() == idVentaSeleccionada) {
                mostrarDetalleVenta(v);
                return;
            }
        }
    }
    
    private void mostrarDetalleVenta(Venta venta) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DETALLE VENTA #").append(venta.getId()).append(" ===\n");
        sb.append("Cliente: ").append(venta.getCliente()).append("\n");
        sb.append("Fecha: ").append(venta.getFecha()).append("\n");
        sb.append("Estado: ").append(venta.getEstado()).append("\n");
        sb.append("------------------------------\n");
        sb.append("ITEMS:\n");
        
        for (DetalleVenta d : venta.getDetalles()) {
            sb.append("  [").append(d.getTipoItem()).append("] ")
              .append(d.getNombre())
              .append(" x").append(d.getCantidad())
              .append(" = $").append(d.getSubtotal()).append("\n");
        }
        
        sb.append("------------------------------\n");
        sb.append("Subtotal: $").append(venta.getSubtotal()).append("\n");
        sb.append("Impuesto: $").append(venta.getImpuesto()).append("\n");
        sb.append("Descuento: $").append(venta.getDescuento()).append("\n");
        sb.append("TOTAL: $").append(venta.getTotal()).append("\n");
        sb.append("Método Pago: ").append(venta.getMetodoPago()).append("\n");
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(null, scrollPane, "Detalle de Venta", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void limpiarFormulario() {
        idVentaSeleccionada = -1;
        view.cbClientes.setSelectedIndex(0);
        view.cbTipoItem.setSelectedIndex(0);
        view.limpiarCarrito();
        view.txtDescuento.setText("0");
        view.spinnerCantidad.setValue(1);
        view.cbMetodoPago.setSelectedIndex(0);
        view.tablaVentas.clearSelection();
        cargarProductos(); // Recargar stocks actualizados
    }
    //-------------------------------Metodo de prueba actualizar stock inventario---------------//
    private void probarStockDirecto() {
    try {
        // Buscar el primer producto disponible
        List<Producto> productos = productoDAO.listar();
        if (productos.isEmpty()) {
            System.out.println("❌ No hay productos en inventario");
            return;
        }
        
        Producto p = productos.get(0);
        System.out.println("=== PRUEBA DIRECTA DE STOCK ===");
        System.out.println("Producto: " + p.getNombre());
        System.out.println("Stock actual: " + p.getStock());
        
        // Intentar descontar 1 unidad
        boolean ok = productoDAO.descontarStock(p.getId(), 1);
        System.out.println("Resultado descuento: " + ok);
        
        // Verificar nuevo stock
        List<Producto> productosActualizados = productoDAO.listar();
        for (Producto prod : productosActualizados) {
            if (prod.getId() == p.getId()) {
                System.out.println("Nuevo stock: " + prod.getStock());
                break;
            }
        }
    } catch (Exception e) {
        System.out.println("Error en prueba: " + e.getMessage());
        e.printStackTrace();
    }
}
}