package com.barberpro.controller;

import com.barberpro.dao.ClienteDAO;
import com.barberpro.model.Cliente;
import com.barberpro.view.ClientesPanel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.barberpro.util.Validator;
import java.awt.Graphics2D;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 * @author Miller
 */
public class ClientesController {

    private ClientesPanel view;
    private ClienteDAO dao;
    private int idCliente = 0;
    private String fotoActual = null;  // Guarda el nombre del archivo de la foto
    // Cache de imágenes para evitar leer del disco constantemente
private Map<Integer, ImageIcon> cacheFotos = new WeakHashMap<>();

    public ClientesController(ClientesPanel view) {
        this.view = view;
        dao = new ClienteDAO();

        dao.verificarColumnaFoto();
        iniciarEventos();
        configurarValidaciones();
        listarClientes();
    }

    private void iniciarEventos() {
        view.btnGuardar.addActionListener(e -> guardar());
        view.btnActualizar.addActionListener(e -> actualizar());
        view.btnEliminar.addActionListener(e -> eliminar());
        view.btnNuevo.addActionListener(e -> limpiar());
        view.tabla.getSelectionModel().addListSelectionListener(e -> seleccionarCliente());
        view.btnHistorial.addActionListener(e -> mostrarHistorial());

        // Eventos para manejo de fotos
        view.btnSeleccionarFoto.addActionListener(e -> seleccionarFoto());
        view.btnTomarFoto.addActionListener(e -> tomarFoto());
        view.btnEliminarFoto.addActionListener(e -> eliminarFoto());

        // Configura validaciones y búsqueda
        view.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buscar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscar();
            }
        });
    }

    // ==================== MÉTODOS CRUD ====================
    private void guardar() {
        
         System.out.println("=== GUARDAR CLIENTE ===");
    System.out.println("fotoActual antes de guardar: " + fotoActual);
        String nombre = view.txtNombre.getText().trim();
        String telefono = view.txtTelefono.getText().trim();
        String email = view.txtEmail.getText().trim();
        String direccion = view.txtDireccion.getText().trim();
        

        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe llenar todos los espacios.");
            return;
        }

        if (!Validator.nombreValido(nombre)) {
            JOptionPane.showMessageDialog(null, "Nombre inválido.\nMínimo 3 caracteres.");
            view.txtNombre.requestFocus();
            return;
        }

        if (!Validator.telefonoValido(telefono)) {
            JOptionPane.showMessageDialog(null, "Teléfono inválido.");
            view.txtTelefono.requestFocus();
            return;
        }

        if (!Validator.emailValido(email)) {
            JOptionPane.showMessageDialog(null, "Email inválido.");
            view.txtEmail.requestFocus();
            return;
        }

        if (dao.existeCliente(telefono, email)) {
            JOptionPane.showMessageDialog(null, "Ya existe un cliente con ese teléfono o email.");
            return;
        }

        Cliente cliente = new Cliente(nombre, telefono, email, direccion);
        cliente.setFoto(fotoActual);  // Guardar la foto si existe

        boolean ok = dao.insertar(cliente);

        if (ok) {
            JOptionPane.showMessageDialog(null, "Cliente guardado con éxito.");
            view.txtNombre.requestFocus();
            listarClientes();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(null, "Error al guardar cliente.");
        }
    }

    private void listarClientes() {
        DefaultTableModel model = (DefaultTableModel) view.tabla.getModel();
        model.setRowCount(0);

        for (Cliente c : dao.listar()) {
            model.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getTelefono(),
                c.getEmail(),
                c.getDireccion() != null ? c.getDireccion() : "",  // ← Evitar null
            c.getFechaRegistro() != null ? c.getFechaRegistro() : ""  // ← Evitar null
            });
        }
    }

    private void seleccionarCliente() {
        int fila = view.tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }

        idCliente = Integer.parseInt(view.tabla.getValueAt(fila, 0).toString());
        view.txtNombre.setText(view.tabla.getValueAt(fila, 1).toString());
        view.txtTelefono.setText(view.tabla.getValueAt(fila, 2).toString());
        view.txtEmail.setText(view.tabla.getValueAt(fila, 3).toString());
         // ✅ Manejar null para dirección (columna 4)
    Object direccionObj = view.tabla.getValueAt(fila, 4);
    view.txtDireccion.setText(direccionObj != null ? direccionObj.toString() : "");
        // Cargar la foto del cliente seleccionado
        Cliente cliente = obtenerClientePorId(idCliente);
        if (cliente != null) {
            cargarFotoCliente(cliente);
        }
    }

    private void actualizar() {
        if (idCliente == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione cliente.");
            return;
        }

        String nombre = view.txtNombre.getText().trim();
        String telefono = view.txtTelefono.getText().trim();
        String email = view.txtEmail.getText().trim();
        String direccion = view.txtDireccion.getText().trim();

        if (!Validator.nombreValido(nombre)) {
            JOptionPane.showMessageDialog(null, "Nombre inválido.");
            view.txtNombre.requestFocus();
            return;
        }

        if (!Validator.telefonoValido(telefono)) {
            JOptionPane.showMessageDialog(null, "Teléfono inválido.");
            view.txtTelefono.requestFocus();
            return;
        }

        if (!Validator.emailValido(email)) {
            JOptionPane.showMessageDialog(null, "Email inválido.");
            view.txtEmail.requestFocus();
            return;
        }

        if (dao.existeClienteActualizar(idCliente, telefono, email)) {
            JOptionPane.showMessageDialog(null, "Otro cliente ya utiliza ese teléfono o email.");
            return;
        }

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        cliente.setDireccion(direccion);
        cliente.setFoto(fotoActual);  // Guardar la foto actual

        boolean ok = dao.actualizar(cliente);

        if (ok) {
            JOptionPane.showMessageDialog(null, "Cliente actualizado con éxito.");
            listarClientes();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar.");
        }
    }

    private void eliminar() {
        if (idCliente == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione cliente.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar el cliente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Eliminar también el archivo de foto
            if (fotoActual != null && !fotoActual.isEmpty()) {
                File archivoFoto = new File("fotos_clientes/" + fotoActual);
                if (archivoFoto.exists()) {
                    archivoFoto.delete();
                }
            }
            dao.eliminar(idCliente);
            listarClientes();
            limpiar();
        }
    }

    private void buscar() {
        String texto = view.txtBuscar.getText().trim();
        DefaultTableModel model = (DefaultTableModel) view.tabla.getModel();
        model.setRowCount(0);

        List<Cliente> lista = texto.isEmpty() ? dao.listar() : dao.buscar(texto);
        
        //---------------feedback si no hay resultados -----------------------------//
        if (lista.isEmpty() && !texto.isEmpty())  {
            JOptionPane.showMessageDialog(view, "Nose encontraron clientes con : "
            + texto, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }

        for (Cliente c : lista) {
            model.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getTelefono(),
                c.getEmail(),
                c.getDireccion() != null ? c.getDireccion()  : "",
                c.getFechaRegistro() != null ? c.getFechaRegistro() : ""
            });
        }
    }

    private void mostrarHistorial() {
        if (idCliente == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente");
            return;
        }

        int citas = dao.contarCitas(idCliente);
        double total = dao.totalGastado(idCliente);
        String ultima = dao.ultimaVisita(idCliente);
        String servicios = dao.serviciosFrecuentes(idCliente);
        String nombre = view.txtNombre.getText();

        String mensaje = """
                     CLIENTE: %s
                             
                     Total citas: %d
                     Total gastado: $%.2f
                     Última visita: %s
                             
                     Servicios frecuentes:
                     %s
                     """.formatted(nombre, citas, total, ultima, servicios);

        JOptionPane.showMessageDialog(null, mensaje, "Historial Cliente", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== MÉTODOS PARA MANEJO DE FOTOS ====================
    private void seleccionarFoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar foto del cliente");
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));

        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File archivoOrigen = chooser.getSelectedFile();

            try {
                // Generar nombre único
                String extension = obtenerExtension(archivoOrigen.getName());
                String nombreArchivo = UUID.randomUUID().toString() + "." + extension;
                String rutaDestino = "fotos_clientes/" + nombreArchivo;

                // Crear carpeta si no existe
                File carpetaFotos = new File("fotos_clientes");
                if (!carpetaFotos.exists()) {
                    carpetaFotos.mkdirs();
                }

                // Redimensionar imagen
                BufferedImage img = ImageIO.read(archivoOrigen);
                BufferedImage imgRedimensionada = redimensionarImagen(img, 180, 180);
                ImageIO.write(imgRedimensionada, extension, new File(rutaDestino));

                // Mostrar en la UI
                view.actualizarFoto(new ImageIcon(imgRedimensionada));

                // Eliminar foto anterior si existe (al actualizar un cliente existente)
                if (idCliente > 0) {
                    eliminarFotoAnteriorSiExiste();
                }

                // Guardar nombre del archivo
                fotoActual = nombreArchivo;
                // Después de fotoActual = nombreArchivo;
                System.out.println("=== SELECCIONAR FOTO ===");
                System.out.println("nombreArchivo: " + nombreArchivo);
                System.out.println("fotoActual: " + fotoActual);
                System.out.println("Ruta destino: " + rutaDestino);
                JOptionPane.showMessageDialog(view, "Foto guardada correctamente");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Error al guardar la foto: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void tomarFoto() {
        JOptionPane.showMessageDialog(view,
                "Para tomar una foto:\n\n"
                + "1. Abra la cámara de Windows desde el menú Inicio\n"
                + "2. Tome la foto del cliente\n"
                + "3. Guárdela en su computadora\n"
                + "4. Luego use la opción 'Seleccionar foto' para cargarla",
                "Tomar foto", JOptionPane.INFORMATION_MESSAGE);
        seleccionarFoto();
    }

    private void eliminarFoto() {
        if (fotoActual != null && !fotoActual.isEmpty()) {
            File archivo = new File("fotos_clientes/" + fotoActual);
            if (archivo.exists()) {
                archivo.delete();
            }
            fotoActual = null;
            view.actualizarFoto(view.crearIconoPorDefecto());
            JOptionPane.showMessageDialog(view, "Foto eliminada");
        } else {
            JOptionPane.showMessageDialog(view, "No hay foto para eliminar");
        }
    }

    private void eliminarFotoAnteriorSiExiste() {
        if (idCliente > 0) {
            Cliente cliente = obtenerClientePorId(idCliente);
            if (cliente != null && cliente.getFoto() != null && !cliente.getFoto().isEmpty()) {
                File archivoAnterior = new File("fotos_clientes/" + cliente.getFoto());
                if (archivoAnterior.exists()) {
                    archivoAnterior.delete();
                }
            }
        }
    }

    private void cargarFotoCliente(Cliente cliente) {
        // ---------------------verificar cache primero -------------------------------//
        if (cacheFotos.containsKey(cliente.getId())) {
            view.actualizarFoto(cacheFotos.get(cliente.getId()));
            fotoActual = cliente.getFoto(); 
            return;
        }
        if (cliente.getFoto() != null && !cliente.getFoto().isEmpty()) {
            File archivoFoto = new File("fotos_clientes/" + cliente.getFoto());
            if (archivoFoto.exists()) {
                try {
                    BufferedImage img = ImageIO.read(archivoFoto);
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(180, 180, Image.SCALE_SMOOTH));
                    ImageIcon iconRedimensionado = new ImageIcon(img);                    
                    view.actualizarFoto(iconRedimensionado);
                    cacheFotos.put(cliente.getId(), iconRedimensionado);
                    fotoActual = cliente.getFoto();
                    return;
                } catch (Exception e) {
                    System.out.println("Error cargando foto: " + e.getMessage());
                }
            }
        }
        view.actualizarFoto(view.crearIconoPorDefecto());
        fotoActual = null;
    }

    private Cliente obtenerClientePorId(int id) {
        for (Cliente c : dao.listar()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    private BufferedImage redimensionarImagen(BufferedImage original, int ancho, int alto) {
        Image imagenRedimensionada = original.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        BufferedImage bufferedRedimensionada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedRedimensionada.createGraphics();
        g2d.drawImage(imagenRedimensionada, 0, 0, null);
        g2d.dispose();
        return bufferedRedimensionada;
    }

    private String obtenerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf(".");
        if (ultimoPunto > 0) {
            return nombreArchivo.substring(ultimoPunto + 1).toLowerCase();
        }
        return "jpg";
    }

    // ==================== VALIDACIONES ====================
    private void configurarValidaciones() {
        view.txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)
                        && c != 'á' && c != 'é' && c != 'í' && c != 'ó' && c != 'ú'
                        && c != 'Á' && c != 'É' && c != 'Í' && c != 'Ó' && c != 'Ú') {
                    e.consume();
                }
                if (view.txtNombre.getText().length() >= 100) {
                    e.consume();
                }
            }
        });

        view.txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
                if (view.txtTelefono.getText().length() >= 15) {
                    e.consume();
                }
            }
        });

        view.txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (view.txtEmail.getText().length() >= 100) {
                    e.consume();
                }
            }
        });
    }

     private void limpiar() {
        idCliente = 0;
        view.txtNombre.setText("");
        view.txtTelefono.setText("");
        view.txtEmail.setText("");
        view.txtDireccion.setText("");
        view.txtBuscar.setText("");
        view.tabla.clearSelection();
        view.actualizarFoto(view.crearIconoPorDefecto());
        fotoActual = null;        
    }
}
