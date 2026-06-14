package com.barberpro.controller;


import com.barberpro.dao.ProductoDAO;
import com.barberpro.model.Producto;
import com.barberpro.view.InventoryPanel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Miller
 */
public class InventoryController {

    private InventoryPanel view;
    private ProductoDAO dao;
    private int idSeleccionado = -1;

    public InventoryController(InventoryPanel view) {
        this.view = view;
        dao = new ProductoDAO();

        iniciarEventos();
        
        view.btnActualizar.setEnabled(false);
        view.btnEliminar.setEnabled(false);
        
        listar();
        mostrarAlertas();
        mostrarVencidos();
    }

    //--------------------------------- Metodo para iniciar eventos -------------------------------//
    private void iniciarEventos() {
        
        view.btnGuardar.addActionListener(e -> guardar());    
        view.btnActualizar.addActionListener(e -> actualizar());
        view.btnEliminar.addActionListener(e  -> eliminar());
        view.btnNuevo.addActionListener(e -> nuevo() );
        
        view.txtBuscar.getDocument().addDocumentListener( new DocumentListener() {

    @Override
    public void insertUpdate(DocumentEvent e) { buscar();
    }

    @Override
    public void removeUpdate( DocumentEvent e) {  buscar();
    }

    @Override
    public void changedUpdate( DocumentEvent e) { buscar();
    }
});
        view.tabla.getSelectionModel().addListSelectionListener(e -> seleccionarFila());

    }

    //-------------------------------------Metodo para guardar-----------------------------------//
    private void guardar() {
       
        try {
            if (view.txtNombre.getText().trim().isEmpty()
                    || view.txtCategoria.getText().trim().isEmpty()
                    || view.txtStock.getText().trim().isEmpty()
                    || view.txtStockMin.getText().trim().isEmpty()
                    || view.txtCompra.getText().trim().isEmpty()
                    || view.txtVenta.getText().trim().isEmpty()
                    || view.txtVencimiento.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                return;
            }
            
            int stock = Integer.parseInt(view.txtStock.getText().trim());
            int stockMin = Integer.parseInt(view.txtStockMin.getText().trim());
            Double compra = Double.parseDouble(view.txtCompra.getText().trim());
            Double venta = Double.parseDouble(view.txtVenta.getText().trim());
            
            if (stock < 0 || stockMin < 0 || compra < 0 ||  venta < 0) {
                JOptionPane.showMessageDialog(null,"Los valores numéricos no pueden ser negativos.");
                return;
            }
            
            Producto p = new Producto();
            
            p.setNombre(view.txtNombre.getText().trim());
            p.setCategoria(view.txtCategoria.getText().trim());
            p.setStock(stock);
            p.setStockMinimo(stockMin);
            p.setPrecioCompra(compra);
            p.setPrecioVenta(venta);
            p.setFechaVencimiento(view.txtVencimiento.getText().trim());
            
            boolean ok = dao.insertar(p);
            
            if (ok) {
                JOptionPane.showMessageDialog(null, "Producto guardado correctamente.");
                 listar();
                limpiar();
               
            } else  {
                JOptionPane.showMessageDialog(null,"No fue posible guardar el producto.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Verifique los valores numéricos.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error : " + e.getMessage());
            e.printStackTrace();
        }
    }

     //-------------------------------------Metodo para actualizar------------------------------//
    private void actualizar()  {
         
        try {
            if (idSeleccionado == -1)  {
                JOptionPane.showMessageDialog(null,"Seleccione un producto.");
                return;
            }
            
            Producto p = new Producto();
            
            p.setId(idSeleccionado);
            p.setNombre(view.txtNombre.getText().trim());
            p.setCategoria(view.txtCategoria.getText().trim());
            p.setStock(Integer.parseInt(view.txtStock.getText().trim()));
            p.setStockMinimo(Integer.parseInt(view.txtStockMin.getText().trim()));
            p.setPrecioCompra(Double.parseDouble(view.txtCompra.getText().trim()));
            p.setPrecioVenta(Double.parseDouble(view.txtVenta.getText().trim()));
            p.setFechaVencimiento(view.txtVencimiento.getText().trim());
            
            boolean ok = dao.actualizar(p);
            
            if (ok)  {
                JOptionPane.showMessageDialog(null,"Producto actualizado.");
                
                listar();
                limpiar();
                idSeleccionado = -1;
            } else  {
                JOptionPane.showMessageDialog(null, "No fue posible actualizar.");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error : " + e.getMessage());
        }
    }
    
    //-------------------------------------Metodo para eliminar--------------------------------//
    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(null,"Seleccione un producto.");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (respuesta != JOptionPane.YES_OPTION ) {
            return;
        }
        
        boolean ok = dao.eliminar(idSeleccionado);
        
        if (ok) {
            JOptionPane.showMessageDialog(null,"Producto eliminado.");
            
            listar();
            limpiar();
        } else  {
            JOptionPane.showMessageDialog(null,"No fue posible eliminar.");
        }
    }
    
    //-------------------------------------Metodo para nuevo-----------------------------------//
    private void nuevo() {
        limpiar();
        idSeleccionado = -1;
        view.tabla.clearSelection();
        view.txtNombre.requestFocus();
        
        System.out.println("Modo nuevo producto.");
    }
    
    //-------------------------------- Buscar --------------------------------//
private void buscar() {

    String texto =
            view.txtBuscar
                    .getText()
                    .trim();

    DefaultTableModel model =
            (DefaultTableModel)
                    view.tabla.getModel();

    model.setRowCount(0);

    for (Producto p : dao.buscar(texto)) {

        model.addRow(new Object[]{
            p.getId(),
            p.getNombre(),
            p.getCategoria(),
            p.getStock(),
            p.getStockMinimo(),
            p.getPrecioCompra(),
            p.getPrecioVenta(),
            p.getFechaVencimiento(),
            p.getEstado()
        });
    }
}
    
    //-------------------------------------Metodo para listar-----------------------------------//
    private void listar() {

         System.out.println("Listando productos...");
        
        DefaultTableModel model = (DefaultTableModel) view.tabla.getModel();

        model.setRowCount(0);
        
         var productos = dao.listar();

    System.out.println(
            "Cantidad productos: "
            + productos.size());

        for (Producto p : dao.listar()) {
            
            System.out.println(
                p.getId() + " - "
                + p.getNombre());
            
            model.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getCategoria(),
                p.getStock(),
                p.getStockMinimo(),
                p.getPrecioCompra(),
                p.getPrecioVenta(),
                p.getFechaVencimiento(),
                p.getEstado() == 1
                            ? "Activo"
                           : "Inactivo" 
                    
            });
        }
        System.out.println(
            "Filas tabla: "
            + model.getRowCount());
    }

    //--------------------------------Metodo para mostrar alertas-------------------------------//
    private void mostrarAlertas() {
        
        System.out.println(
            "Productos vencidos encontrados: "
            + dao.vencidos().size());

        StringBuilder alerta = new StringBuilder();

        for (Producto p : dao.stockBajo()) {
            alerta.append(
                    "• ")
                    .append(
                            p.getNombre())
                    .append(
                            " stock: ")
                    .append(
                            p.getStock())
                    .append("\n");
        }

        if (!alerta.isEmpty()) {
            JOptionPane.showMessageDialog(null, "PRODUCTOS CON STOCK BAJO\\n\\n" + alerta);
        }
    }

    //---------------------------------Metodo para mostrar vencidos-----------------------------//
    public void mostrarVencidos() {
        StringBuilder alerta = new StringBuilder();

        for (Producto p : dao.vencidos()) {

            alerta.append(
                    "• ")
                    .append(
                            p.getNombre())
                    .append(
                            " vencido el ")
                    .append(
                            p.getFechaVencimiento())
                    .append("\n");
        }

        if (!alerta.isEmpty()) {
                          JOptionPane.showMessageDialog(
                        null,
                        "PRODUCTOS VENCIDOS\n\n"
                        + alerta);
           
        }
    }

    //-------------------------------------Metodo seleccionar fila---------------------------------//
    public void seleccionarFila() {
        int fila = view.tabla.getSelectedRow();
        
        if ( fila == -1)  {
            return;
        }
        
        idSeleccionado = Integer.parseInt(view.tabla.getValueAt(fila,0).toString());
        view.txtNombre.setText(view.tabla.getValueAt(fila,1).toString());
        view.txtCategoria.setText(view.tabla.getValueAt(fila,2).toString());
        view.txtStock.setText(view.tabla.getValueAt(fila,3).toString());
        view.txtStockMin.setText(view.tabla.getValueAt(fila,4).toString());
        view.txtCompra.setText(view.tabla.getValueAt(fila,5).toString());
        view.txtVenta.setText(view.tabla.getValueAt(fila,6).toString());
        view.txtVencimiento.setText(    view.tabla.getValueAt(fila, 7).toString());
        
        view.btnGuardar.setEnabled(false);
        view.btnActualizar.setEnabled(true);
        view.btnEliminar.setEnabled(true);
        
        System.out.println("Producto seleccionado: " + idSeleccionado);
    }
    
    //-------------------------------------Metodo para limpiar-----------------------------------//
    private void limpiar() {
        view.txtNombre.setText("");
        view.txtCategoria.setText("");
        view.txtStock.setText("");
        view.txtStockMin.setText("");
        view.txtCompra.setText("");
        view.txtVenta.setText("");
        view.txtVencimiento.setText("");
        
        idSeleccionado = -1;
        view.tabla.clearSelection();
        
        view.btnGuardar.setEnabled(true);
        view.btnActualizar.setEnabled(false);
        view.btnEliminar.setEnabled(false);
    }

}
