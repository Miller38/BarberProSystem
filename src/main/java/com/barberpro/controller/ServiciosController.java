package com.barberpro.controller;

import com.barberpro.dao.ServicioDAO;
import com.barberpro.model.Servicio;
import com.barberpro.view.ServiciosPanel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ServiciosController {

    private ServiciosPanel view;
    private ServicioDAO dao;
    private int idSeleccionado = -1;

    public ServiciosController(
            ServiciosPanel view) {

        this.view = view;

        dao = new ServicioDAO();

        iniciarEventos();

        listar();
    }

    //---------------------------------- metodo para iniciar evento --------------------------//
    private void iniciarEventos() {

        view.btnGuardar.addActionListener( e -> guardar());
        view.btnActualizar.addActionListener(e -> actualizar());
        view.btnEliminar.addActionListener(e -> eliminar());
        view.btnNuevo.addActionListener(e -> limpiar());
        view.tabla.getSelectionModel().addListSelectionListener(e -> seleccionarFila());
        
        view.txtBuscar.getDocument()
        .addDocumentListener(
                new javax.swing.event.DocumentListener() {

            @Override
            public void insertUpdate(
                    javax.swing.event.DocumentEvent e) {

                buscar();
            }

            @Override
            public void removeUpdate(
                    javax.swing.event.DocumentEvent e) {

                buscar();
            }

            @Override
            public void changedUpdate(
                    javax.swing.event.DocumentEvent e) {

                buscar();
            }
        });
    }

     //---------------------------------- metodo para guardar --------------------------//
    private void guardar() {

    try {

        String nombre =
                view.txtNombre.getText().trim();

        String categoria =
                view.cbCategoria.getSelectedItem().toString();

        if(nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Ingrese nombre");

            return;
        }

        double precio =
                Double.parseDouble(
                        view.txtPrecio.getText());

        Servicio servicio =
                new Servicio(
                        nombre,
                        categoria,
                        precio);

        if(dao.insertar(servicio)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Servicio guardado");

            listar();
            limpiar();
        }

    } catch(Exception e) {

        JOptionPane.showMessageDialog(
                null,
                "Precio inválido");
    }
}
    
     //---------------------------------- metodo para seleccionar fila ------------------//
   private void seleccionarFila() {

    int fila = view.tabla.getSelectedRow();

    if (fila < 0) {
        return;
    }

    Object idObj = view.tabla.getValueAt(fila, 0);

    if (idObj == null) {
        return;
    }

    idSeleccionado = Integer.parseInt(idObj.toString());

    view.txtNombre.setText(
            String.valueOf(
                    view.tabla.getValueAt(fila, 1)));

    view.cbCategoria.setSelectedItem(
            String.valueOf(
                    view.tabla.getValueAt(fila, 2)));

    view.txtPrecio.setText(
            String.valueOf(
                    view.tabla.getValueAt(fila, 3)));
}

    //----------------------------------Metodo listar ------------------------------------//
    private void listar() {

        DefaultTableModel model =
                (DefaultTableModel)
                        view.tabla.getModel();

        model.setRowCount(0);

        for (Servicio s :
                dao.listar()) {

            model.addRow(new Object[]{
                    s.getId(),
                    s.getNombre(),
                    s.getCategoria(),
                    s.getPrecio()
            });
        }
    }
    
    //----------------------------------Metodo listar ------------------------------------//
    private void actualizar() {
        if (idSeleccionado == -1){
            JOptionPane.showMessageDialog(null,"Seleccione un servicio.");
            return;
        }
        
        if(view.txtNombre.getText().trim().isEmpty()) {
    JOptionPane.showMessageDialog(null,
            "Ingrese nombre");
    return;
}
        
        Servicio servicio = new Servicio();
        
        servicio.setId(idSeleccionado);
        servicio.setNombre(view.txtNombre.getText());
        servicio.setCategoria(view.cbCategoria.getSelectedItem().toString());
        servicio.setPrecio(Double.parseDouble(view.txtPrecio.getText()));
        
        if (dao.actualizar(servicio)) {
            JOptionPane.showMessageDialog(null,"Servicio actualizado.");
            
            listar();
            limpiar();
        }
    }
    
     //------------------------------Metodo eliminar ------------------------------------//
     private void eliminar() {
         if (idSeleccionado == -1) {
             JOptionPane.showMessageDialog(null, "Seleccione servicio.");
             return;
         }
         
         if (dao.eliminar(idSeleccionado)) {
             JOptionPane.showMessageDialog(null,"Servicio eliminado.");
             listar();
             limpiar();
         }
     }

      //------------------------------Metodo buscar ------------------------------------//
     private void buscar()  {
         String texto = view.txtBuscar.getText().trim();
         
         DefaultTableModel model = (DefaultTableModel)view.tabla.getModel();
         
         model.setRowCount(0);
         
         for(Servicio s : dao.buscar(texto)) {
             model.addRow(new Object[] {
                 s.getId(),
                 s.getNombre(),
                 s.getCategoria(),
                 s.getPrecio()
             });
         }
     }
     
    //--------------------------Metodo limpiar ----------------------------------------//
    private void limpiar() {
        
        idSeleccionado =  -1;
        view.txtNombre.setText("");
        view.txtPrecio.setText("");
        view.cbCategoria.setSelectedIndex(0);
        view.tabla.clearSelection();
    }
}