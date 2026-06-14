
package com.barberpro.controller;

import com.barberpro.dao.UsuarioDAO;
import com.barberpro.model.Usuario;
import com.barberpro.view.UsuariosPanel;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Miller
 */
public class UsuariosController {
    private UsuariosPanel view;
private UsuarioDAO usuarioDAO;

private int idSeleccionado = -1;

public UsuariosController(
        UsuariosPanel view) {

    this.view = view;

    usuarioDAO = new UsuarioDAO();

    iniciarEventos();

    listarUsuarios();
}

private void iniciarEventos() {

    view.btnGuardar.addActionListener(
            e -> guardar());

    view.btnActualizar.addActionListener(
            e -> actualizar());

    view.btnEliminar.addActionListener(
            e -> eliminar());

    view.btnNuevo.addActionListener(
            e -> limpiarFormulario());

    view.btnBloquear.addActionListener(
            e -> bloquear());

    view.btnDesbloquear.addActionListener(
            e -> desbloquear());

    view.btnActivar.addActionListener(
            e -> activar());

//    view.btnDesactivar.addActionListener(
//            e -> desactivar());

    view.tabla.getSelectionModel()
            .addListSelectionListener(
                    e -> seleccionar());

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

private void guardar() {

    String nombre =
            view.txtNombre.getText().trim();

    String usuario =
            view.txtUsuario.getText().trim();

    String password =
            String.valueOf(
                    view.txtPassword.getPassword());

    String rol =
            view.cbRol.getSelectedItem()
                    .toString();

    String email =
            view.txtEmail.getText().trim();

    String telefono =
            view.txtTelefono.getText().trim();

    if(nombre.isEmpty()
            || usuario.isEmpty()
            || password.isEmpty()) {

        JOptionPane.showMessageDialog(
                null,
                "Complete los datos");

        return;
    }

    if(usuarioDAO.existeUsuario(usuario)) {

        JOptionPane.showMessageDialog(
                null,
                "Usuario ya existe");

        return;
    }

    Usuario u =
            new Usuario();

    u.setNombre(nombre);
    u.setUsuario(usuario);
    u.setPassword(password);
    u.setRol(rol);

    u.setEmail(email);
    u.setTelefono(telefono);

    boolean ok =
            usuarioDAO.insertar(u);

    if(ok) {

        JOptionPane.showMessageDialog(
                null,
                "Usuario guardado");

        listarUsuarios();

        limpiarFormulario();
    }
}

private void actualizar() {

    if(idSeleccionado == -1) {

        JOptionPane.showMessageDialog(
                null,
                "Seleccione usuario");

        return;
    }

    Usuario u =
            new Usuario();

    u.setId(idSeleccionado);

    u.setNombre(
            view.txtNombre.getText());

    u.setUsuario(
            view.txtUsuario.getText());

    u.setRol(
            view.cbRol.getSelectedItem()
                    .toString());

    u.setEmail(
            view.txtEmail.getText());

    u.setTelefono(
            view.txtTelefono.getText());

    u.setEstado(1);

    boolean ok =
            usuarioDAO.actualizar(u);

    if(ok) {

        JOptionPane.showMessageDialog(
                null,
                "Usuario actualizado");

        listarUsuarios();
    }
}

private void eliminar() {

    if(idSeleccionado == -1) {

        JOptionPane.showMessageDialog(
                null,
                "Seleccione usuario");

        return;
    }

    int r =
            JOptionPane.showConfirmDialog(
                    null,
                    "Eliminar usuario ?");

    if(r == JOptionPane.YES_OPTION) {

        usuarioDAO.eliminar(
                idSeleccionado);

        listarUsuarios();

        limpiarFormulario();
    }
}

private void seleccionar() {

    int fila =
            view.tabla.getSelectedRow();

    if(fila == -1) {
        return;
    }

    idSeleccionado =
            Integer.parseInt(
                    view.tabla.getValueAt(
                            fila,
                            0).toString());

    view.txtNombre.setText(
            view.tabla.getValueAt(
                    fila,
                    1).toString());

    view.txtUsuario.setText(
            view.tabla.getValueAt(
                    fila,
                    2).toString());

    view.cbRol.setSelectedItem(
            view.tabla.getValueAt(
                    fila,
                    3).toString());

    view.txtEmail.setText(
            view.tabla.getValueAt(
                    fila,
                    4).toString());

    view.txtTelefono.setText(
            view.tabla.getValueAt(
                    fila,
                    5).toString());
}

private void buscar() {

    String texto =
            view.txtBuscar.getText();

    cargarTabla(
            usuarioDAO.buscar(texto));
}

private void listarUsuarios() {

    cargarTabla(
            usuarioDAO.listar());
}

private void cargarTabla(
        List<Usuario> lista) {

    DefaultTableModel model =
            (DefaultTableModel)
                    view.tabla.getModel();

    model.setRowCount(0);

    for(Usuario u : lista) {

        model.addRow(new Object[]{

            u.getId(),
            u.getNombre(),
            u.getUsuario(),
            u.getRol(),
            u.getEmail(),
            u.getTelefono(),

            u.getEstado() == 1
                    ? "ACTIVO"
                    : "INACTIVO",

            u.getBloqueado() == 1
                    ? "SI"
                    : "NO",

            u.getUltimoAcceso()
        });
    }
}

private void bloquear() {

    if(idSeleccionado == -1) {
        return;
    }

    usuarioDAO.bloquear(
            idSeleccionado);

    listarUsuarios();
}

private void desbloquear() {

    if(idSeleccionado == -1) {
        return;
    }

    usuarioDAO.desbloquear(
            idSeleccionado);

    listarUsuarios();
}

private void activar() {

    if(idSeleccionado == -1) {
        return;
    }

    usuarioDAO.activar(
            idSeleccionado);

    listarUsuarios();
}

private void desactivar() {

    if(idSeleccionado == -1) {
        return;
    }

    usuarioDAO.desactivar(
            idSeleccionado);

    listarUsuarios();
}

private void limpiarFormulario() {

    idSeleccionado = -1;

    view.tabla.clearSelection();

    view.txtNombre.setText("");
    view.txtUsuario.setText("");
    view.txtPassword.setText("");

    view.txtEmail.setText("");
    view.txtTelefono.setText("");

    view.cbRol.setSelectedIndex(0);
}
}
