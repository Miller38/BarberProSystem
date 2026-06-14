package com.barberpro.controller;

import com.barberpro.dao.CitaDAO;
import com.barberpro.dao.ClienteDAO;
import com.barberpro.dao.ServicioDAO;
import com.barberpro.model.Cita;
import com.barberpro.model.Cliente;
import com.barberpro.model.Servicio;
import com.barberpro.view.CitasPanel;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CitasController {

    private CitasPanel view;
    private CitaDAO citaDAO;
    private ClienteDAO clienteDAO;
    private ServicioDAO servicioDAO;
    private int idCita = 0;
    //-------Variable para notificaciones app-----//
    private Timer timerNotificaciones;

    public CitasController(CitasPanel view) {
        this.view = view;
        citaDAO = new CitaDAO();
        clienteDAO = new ClienteDAO();
        servicioDAO = new ServicioDAO();

        iniciarEventos();

        cargarClientes();
        cargarServicios();
        listarCitas();
        actualizarResumen();

        verificarRecordatorios();
        iniciarSistemaNotificaciones();
    }

    //--------------------------------------Metodo iniciar eventos------------------------//
    private void iniciarEventos() {
        view.btnGuardar.addActionListener(e -> guardar());
        view.btnActualizar.addActionListener(e -> actualizar());
        view.btnEliminar.addActionListener(e -> eliminar());
        view.btnNuevo.addActionListener(e -> limpiar());
        view.btnCambiarEstado.addActionListener(e -> cambiarEstado());
        view.btnCitasHoy.addActionListener(e -> mostrarCitasHoy());
        view.btnExportar.addActionListener(e -> exportarCitas());
        view.btnEnviarRecordatorios.addActionListener(e -> enviarRecordatoriosManana());

        view.tabla.getSelectionModel().addListSelectionListener(e -> seleccionarCita());
        crearMenuContextual();

        view.datePicker.addDateChangeListener(e -> cargarHorariosDisponibles());
        view.btnWhatsApp.addActionListener(e -> enviarWhatsAppCitaSeleccionada());

        view.txtFiltrarFecha.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrarCitas();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrarCitas();
            }

            public void changedUpdate(DocumentEvent e) {
                filtrarCitas();
            }
        });
        view.cbFiltrarEstado.addActionListener(e -> filtrarCitas());
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

    private void cargarHorariosDisponibles() {
        if (view.datePicker.getDate() == null) {
            return;
        }
        String fecha = view.datePicker.getDate().toString();
        List<String> ocupados = citaDAO.getHorariosOcupados(fecha);
        view.actualizarHorariosDisponibles(ocupados);
    }

    private void guardar() {
        Cliente cliente = (Cliente) view.cbClientes.getSelectedItem();
        Servicio servicio = (Servicio) view.cbServicios.getSelectedItem();

        if (view.datePicker.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Seleccione una fecha");
            return;
        }

        String fecha = view.datePicker.getDate().toString();
        String hora = (String) view.cbHoras.getSelectedItem();
        String estado = view.cbEstado.getSelectedItem().toString();

        if (cliente == null || servicio == null) {
            JOptionPane.showMessageDialog(null, "Seleccione cliente y servicio");
            return;
        }

        if (!citaDAO.fechaValida(fecha)) {
            JOptionPane.showMessageDialog(null, "No se pueden agendar citas en fechas pasadas");
            return;
        }

        if (!citaDAO.horarioDisponible(fecha, hora)) {
            JOptionPane.showMessageDialog(null, "Horario ocupado. Seleccione otra hora.");
            return;
        }

        Cita cita = new Cita();
        cita.setClienteId(cliente.getId());
        cita.setServicioId(servicio.getId());
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setEstado(estado);

        if (citaDAO.insertar(cita)) {
            JOptionPane.showMessageDialog(null, "Cita registrada");
            listarCitas();
            actualizarResumen();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(null, "Error registrando cita");
        }
    }

    private void actualizar() {
        if (idCita == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione una cita");
            return;
        }

        if (view.datePicker.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Seleccione fecha");
            return;
        }

        Cliente cliente = (Cliente) view.cbClientes.getSelectedItem();
        Servicio servicio = (Servicio) view.cbServicios.getSelectedItem();
        String fecha = view.datePicker.getDate().toString();
        String hora = (String) view.cbHoras.getSelectedItem();
        String estado = view.cbEstado.getSelectedItem().toString();

        if (!citaDAO.fechaValida(fecha)) {
            JOptionPane.showMessageDialog(null, "No se pueden agendar citas en fechas pasadas");
            return;
        }

        if (citaDAO.existeCitaEnHorario(fecha, hora, idCita)) {
            JOptionPane.showMessageDialog(null, "Horario ocupado. Seleccione otra hora.");
            return;
        }

        Cita cita = new Cita();
        cita.setId(idCita);
        cita.setClienteId(cliente.getId());
        cita.setServicioId(servicio.getId());
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setEstado(estado);

        if (citaDAO.actualizar(cita)) {
            JOptionPane.showMessageDialog(null, "Cita actualizada");
            listarCitas();
            actualizarResumen();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar");
        }
    }

    private void eliminar() {
        if (idCita == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione una cita");
            return;
        }

        int option = JOptionPane.showConfirmDialog(null, "¿Desea eliminar la cita seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        if (citaDAO.eliminar(idCita)) {
            JOptionPane.showMessageDialog(null, "Cita eliminada");
            listarCitas();
            actualizarResumen();
            limpiar();
        }
    }

    private void listarCitas() {
        DefaultTableModel model = (DefaultTableModel) view.tabla.getModel();
        model.setRowCount(0);

        for (Cita c : citaDAO.listar()) {
            model.addRow(new Object[]{
                c.getId(), c.getCliente(), c.getServicio(),
                c.getFecha(), c.getHora(), c.getEstado()
            });
        }
    }

    private void filtrarCitas() {
        String fecha = view.txtFiltrarFecha.getText().trim();
        String estado = (String) view.cbFiltrarEstado.getSelectedItem();

        DefaultTableModel model = (DefaultTableModel) view.tabla.getModel();
        model.setRowCount(0);

        List<Cita> citas = citaDAO.filtrar(fecha, estado);
        for (Cita c : citas) {
            model.addRow(new Object[]{
                c.getId(), c.getCliente(), c.getServicio(),
                c.getFecha(), c.getHora(), c.getEstado()
            });
        }
    }

    private void actualizarResumen() {
        int totalHoy = citaDAO.totalCitasHoy();
        int pendientes = citaDAO.contarPorEstado("Pendiente");
        int confirmadas = citaDAO.contarPorEstado("Confirmada");
        view.actualizarResumen(totalHoy, pendientes, confirmadas);
    }

    private void verificarRecordatorios() {
        List<Cita> citasHoy = citaDAO.listarCitasHoy();
        if (!citasHoy.isEmpty()) {
            StringBuilder msg = new StringBuilder("CITAS PARA HOY:\n\n");
            for (Cita c : citasHoy) {
                msg.append("• ").append(c.getHora()).append(" - ")
                        .append(c.getCliente()).append(" - ")
                        .append(c.getServicio()).append("\n");
            }
            JOptionPane.showMessageDialog(view, msg.toString(), "Recordatorio", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void mostrarCitasHoy() {
        DefaultTableModel model = (DefaultTableModel) view.tabla.getModel();
        model.setRowCount(0);

        for (Cita c : citaDAO.listarCitasHoy()) {
            model.addRow(new Object[]{
                c.getId(), c.getCliente(), c.getServicio(),
                c.getFecha(), c.getHora(), c.getEstado()
            });
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(view, "No hay citas para hoy");
        }
    }

    private void exportarCitas() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("citas_" + LocalDate.now() + ".csv"));
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(chooser.getSelectedFile())) {
                writer.println("ID,Cliente,Servicio,Fecha,Hora,Estado");
                for (Cita c : citaDAO.listar()) {
                    writer.printf("%d,%s,%s,%s,%s,%s%n",
                            c.getId(), c.getCliente(), c.getServicio(),
                            c.getFecha(), c.getHora(), c.getEstado());
                }
                JOptionPane.showMessageDialog(view, "Exportado correctamente:\n" + chooser.getSelectedFile().getName());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Error al exportar: " + e.getMessage());
            }
        }
    }

    private void seleccionarCita() {
        int fila = view.tabla.getSelectedRow();
        if (fila == -1) {
            return;
        }

        idCita = Integer.parseInt(view.tabla.getValueAt(fila, 0).toString());
        String cliente = view.tabla.getValueAt(fila, 1).toString();
        String servicio = view.tabla.getValueAt(fila, 2).toString();
        String fecha = view.tabla.getValueAt(fila, 3).toString();
        String hora = view.tabla.getValueAt(fila, 4).toString();
        String estado = view.tabla.getValueAt(fila, 5).toString();

        for (int i = 0; i < view.cbClientes.getItemCount(); i++) {
            Cliente c = view.cbClientes.getItemAt(i);
            if (c.getNombre().equals(cliente)) {
                view.cbClientes.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < view.cbServicios.getItemCount(); i++) {
            Servicio s = view.cbServicios.getItemAt(i);
            if (s.getNombre().equals(servicio)) {
                view.cbServicios.setSelectedIndex(i);
                break;
            }
        }

        view.datePicker.setDate(java.time.LocalDate.parse(fecha));
        view.cbHoras.setSelectedItem(hora);
        view.cbEstado.setSelectedItem(estado);
    }

    private void cambiarEstado() {
        if (idCita == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione una cita");
            return;
        }

        String estado = view.cbEstado.getSelectedItem().toString();
        if (citaDAO.actualizarEstado(idCita, estado)) {
            JOptionPane.showMessageDialog(null, "Estado actualizado");
            listarCitas();
            actualizarResumen();
            limpiar();
        }
    }

    private void actualizarEstadoRapido(String estado) {
        if (idCita == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione una cita");
            return;
        }
        if (citaDAO.actualizarEstado(idCita, estado)) {
            listarCitas();
            actualizarResumen();
            limpiar();
            JOptionPane.showMessageDialog(null, "Estado actualizado a: " + estado);
        }
    }

    private void crearMenuContextual() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem confirmar = new JMenuItem("Confirmar");
        JMenuItem finalizar = new JMenuItem("Finalizar");
        JMenuItem cancelar = new JMenuItem("Cancelar");
        JMenuItem pendiente = new JMenuItem("Pendiente");
        JMenuItem enviarWhatsApp = new JMenuItem("Enviar WhatsApp");

        confirmar.addActionListener(e -> actualizarEstadoRapido("Confirmada"));
        finalizar.addActionListener(e -> actualizarEstadoRapido("Finalizada"));
        cancelar.addActionListener(e -> actualizarEstadoRapido("Cancelada"));
        pendiente.addActionListener(e -> actualizarEstadoRapido("Pendiente"));
        enviarWhatsApp.addActionListener(e -> enviarWhatsAppCitaSeleccionada());

        menu.add(confirmar);
        menu.add(finalizar);
        menu.add(cancelar);
        menu.add(pendiente);
        menu.addSeparator();  // Línea separadora
        menu.add(enviarWhatsApp);
        view.tabla.setComponentPopupMenu(menu);
    }

    // ====================INICIO MÉTODOS DE ENVIO CORREO ================//    
    private void enviarRecordatorioEmail(Cita cita, String emailCliente) {
        if (emailCliente == null || emailCliente.trim().isEmpty()) {
            System.out.println("Cliente sin email: " + cita.getCliente());
            return;
        }

        final String usuario = "millergutierrez38@gmail.com";
        final String password = "ykjx shpv fxgt oabs";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(usuario, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailCliente));
            message.setSubject("Recordatorio de Cita - BarberPro");
            message.setText("Hola " + cita.getCliente() + ",\n\nTe recordamos tu cita para mañana:\nFecha: "
                    + cita.getFecha() + "\nHora: " + cita.getHora() + "\nServicio: " + cita.getServicio() + "\n\n¡Te esperamos!");

            Transport.send(message);
            System.out.println("Correo enviado a: " + emailCliente);

        } catch (MessagingException e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }

    //--------------------------Metodo obtener correo ----------------------------------------//
    private String obtenerEmailCliente(String nombreCliente) {
        for (Cliente c : clienteDAO.listar()) {
            if (c.getNombre().equals(nombreCliente)) {
                return c.getEmail();
            }
        }
        return null;
    }

    //--------------------------------------Enviar recordatorios correo-------------------------------//
    private void enviarRecordatoriosManana() {

        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Enviar recordatorios por correo a los clientes con citas para mañana?",
                "Confirmar envío", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        System.out.println("Fecha actual: " + java.time.LocalDate.now());
        System.out.println("Fecha mañana: " + java.time.LocalDate.now().plusDays(1));
        List<Cita> citasManana = citaDAO.listarCitasManana();

        if (citasManana.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No hay citas programadas para mañana");
            return;
        }

        int enviados = 0;
        int sinEmail = 0;

        for (Cita cita : citasManana) {
            String email = obtenerEmailCliente(cita.getCliente());
            if (email != null && !email.isEmpty()) {
                enviarRecordatorioEmail(cita, email);
                enviados++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } else {
                sinEmail++;
                System.out.println("Cliente sin email: " + cita.getCliente());
            }
        }

        JOptionPane.showMessageDialog(view, "Recordatorios enviados:\nEnviados: " + enviados + "\nSin email: " + sinEmail);
    }

    // ===============INICIO SISTEMA NOTIFICACIONES EN APP=================// 
    //----------------------Metodo verificar citas roximas-------------------------------------//
    private void iniciarSistemaNotificaciones() {
        timerNotificaciones = new Timer(60000, e -> verificarCitasProximas()); // Revisa cada 60 segundos
        timerNotificaciones.start();
        System.out.println("✅ Sistema de notificaciones iniciado");
    }
//----------------------------------Metodo verificar ctas proximas--------------------------//

    private void verificarCitasProximas() {
        String horaActual = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
        String fechaActual = java.time.LocalDate.now().toString();

        List<Cita> citasHoy = citaDAO.listarCitasHoy();

        for (Cita cita : citasHoy) {
            if (cita.getHora().equals(horaActual)) {
                mostrarNotificacion("⏰ CITA AHORA: " + cita.getCliente() + " - " + cita.getServicio() + " a las " + cita.getHora());
            } else if (esCitaProxima(cita.getHora(), horaActual, 30)) {
                mostrarNotificacion("⏰ CITA EN 30 MINUTOS: " + cita.getCliente() + " - " + cita.getServicio() + " a las " + cita.getHora());
            }
        }
    }
//---------------------------- Metodo cita proxima--------------------------------------//

    private boolean esCitaProxima(String horaCita, String horaActual, int minutosAntes) {
        try {
            java.time.LocalTime citaTime = java.time.LocalTime.parse(horaCita);
            java.time.LocalTime ahora = java.time.LocalTime.parse(horaActual);
            java.time.LocalTime limite = citaTime.minusMinutes(minutosAntes);

            return ahora.equals(limite) || (ahora.isAfter(limite) && ahora.isBefore(citaTime));
        } catch (Exception e) {
            return false;
        }
    }
//-----------------------------------Metodo mostrar notificaciones-------------------//

    private void mostrarNotificacion(String mensaje) {
        // Verificar si el sistema soporta notificaciones de bandeja
        if (SystemTray.isSupported()) {
            try {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
                TrayIcon trayIcon = new TrayIcon(image, "BarberPro");
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);
                trayIcon.displayMessage("BarberPro - Recordatorio", mensaje, TrayIcon.MessageType.INFO);
                tray.remove(trayIcon);
            } catch (Exception e) {
                // Si falla, mostrar JOptionPane
                JOptionPane.showMessageDialog(view, mensaje, "Recordatorio", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Si no soporta bandeja, mostrar JOptionPane
            JOptionPane.showMessageDialog(view, mensaje, "Recordatorio", JOptionPane.INFORMATION_MESSAGE);
        }
    }
//---------------------------------Metodo para detener notificaciones -------------------------//

    public void detenerSistemaNotificaciones() {
        if (timerNotificaciones != null) {
            timerNotificaciones.stop();
            System.out.println("Sistema de notificaciones detenido");
        }
    }

    // ===============INICIO SISTEMA ENVIAR MENSAJE WHATSAPP=============// 
    private void enviarWhatsApp(Cita cita, String telefono) {
    if (telefono == null || telefono.trim().isEmpty()) {
        JOptionPane.showMessageDialog(view, "El cliente no tiene número de teléfono registrado");
        return;
    }
    
    // Limpiar el número (solo dígitos)
    String numeroLimpio = telefono.replaceAll("[^0-9]", "");
    
    // Agregar código de país si no tiene (ejemplo: Colombia 57)
    if (!numeroLimpio.startsWith("57") && numeroLimpio.length() == 10) {
        numeroLimpio = "57" + numeroLimpio;
    }
    
    String mensaje = "Hola " + cita.getCliente() + 
                     ", te recordamos tu cita en BarberPro para el día " + 
                     cita.getFecha() + " a las " + cita.getHora() + 
                     " para el servicio: " + cita.getServicio() +
                     ". ¡Te esperamos!";
    
    try {
        String url = "https://wa.me/" + numeroLimpio + "?text=" + java.net.URLEncoder.encode(mensaje, "UTF-8");
        java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
    } catch (Exception e) {
        JOptionPane.showMessageDialog(view, "Error al abrir WhatsApp: " + e.getMessage());
    }
}

private void enviarWhatsAppCitaSeleccionada() {
    if (idCita == 0) {
        JOptionPane.showMessageDialog(view, "Seleccione una cita");
        return;
    }
    
    // Obtener la cita completa
    Cita citaSeleccionada = null;
    for (Cita c : citaDAO.listar()) {
        if (c.getId() == idCita) {
            citaSeleccionada = c;
            break;
        }
    }
    
    if (citaSeleccionada == null) {
        JOptionPane.showMessageDialog(view, "No se encontró la cita");
        return;
    }
    
    // Obtener teléfono del cliente
    String telefono = obtenerTelefonoCliente(citaSeleccionada.getCliente());
    enviarWhatsApp(citaSeleccionada, telefono);
}

private String obtenerTelefonoCliente(String nombreCliente) {
    for (Cliente c : clienteDAO.listar()) {
        if (c.getNombre().equals(nombreCliente)) {
            return c.getTelefono();
        }
    }
    return null;
}
    
    //----------------------------Metodo limpiar--------------------------------//
    private void limpiar() {
        idCita = 0;
        view.cbClientes.setSelectedIndex(-1);
        view.cbServicios.setSelectedIndex(-1);
        view.datePicker.clear();
        view.cbHoras.setSelectedIndex(0);
        view.cbEstado.setSelectedIndex(0);
        view.tabla.clearSelection();
    }
}
