package com.barberpro.controller;

import com.barberpro.config.Session;
import com.barberpro.view.Dashboard;
import com.barberpro.view.LoginView;
import com.barberpro.view.ClientesPanel;
import com.barberpro.view.CitasPanel;
import java.awt.BorderLayout;
import com.barberpro.view.ServiciosPanel;
import com.barberpro.view.VentasPanel;
import com.barberpro.view.HomePanel;
import com.barberpro.view.SettingsPanel;
import com.barberpro.controller.SettingsController;
import com.barberpro.controller.VentasController;
import com.barberpro.controller.ServiciosController;
import com.barberpro.view.InventoryPanel;
import com.barberpro.controller.InventoryController;
import com.barberpro.view.UsuariosPanel;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import com.barberpro.config.ActualizadorApp;

/**
 *
 * @author Miller
 */
public class DashboardController {

    private Dashboard view;
    private boolean sideBarColapsado;

    public DashboardController(Dashboard view) {
        this.view = view;

        iniciarEventos();
        abrirHome();

        // Verificar actualizaciones automáticamente al cargar el Dashboard
        javax.swing.Timer timer = new javax.swing.Timer(3000, e -> {
            ActualizadorApp.verificarActualizacion(view);
        });
        timer.setRepeats(false);
        timer.start();
    }

    //---------------------------------   Metodo para iniciar eventos --------------------------//
    private void iniciarEventos() {

        view.btnLogout.addActionListener(e -> logout());
        view.btnClientes.addActionListener(e -> abrirClientes());
        view.btnCitas.addActionListener(e -> abrirCitas());
        view.btnServicios.addActionListener(e -> abrirServicios());
        view.btnVentas.addActionListener(e -> abrirVentas());
        view.btnDashboard.addActionListener(e -> abrirHome());
        view.btnSettings.addActionListener(e -> abrirSettings());
        view.btnInventario.addActionListener(e -> abrirInventario());
        view.btnUsuarios.addActionListener(e -> abrirUsuarios());

    }

    //-----------------------------------   Metodo para cerrar session --------------------------//
    private void logout() {
        Session.cerrarSession();

        LoginView login = new LoginView();
        new LoginController(login);
        login.setVisible(true);
        view.dispose();
    }

    //-----------------------------------   Metodo para cerrar session --------------------------//
    private void abrirClientes() {
        ClientesPanel panel = new ClientesPanel();
        new ClientesController(panel);
        mostrarPanel(panel);
    }

    //-----------------------------------   Metodo para abrir citas --------------------------//
    private void abrirCitas() {
        CitasPanel panel = new CitasPanel();
        new CitasController(panel);
        mostrarPanel(panel);
    }

    //-----------------------------------   Metodo para abrir servicios --------------------------//
    private void abrirServicios() {
        ServiciosPanel panel = new ServiciosPanel();
        new ServiciosController(panel);
        mostrarPanel(panel);

    }

    //-----------------------------------   Metodo para abrir ventas --------------------------//
    private void abrirVentas() {
        VentasPanel panel = new VentasPanel();
        new VentasController(panel);
        mostrarPanel(panel);
    }

    //-----------------------------------   Metodo para abrir home --------------------------//
    private void abrirHome() {
        HomePanel panel = new HomePanel();
        new HomeController(panel);
        mostrarPanel(panel);
    }

    //-----------------------------------   Metodo para abrir settings --------------------------//
    private void abrirSettings() {
        SettingsPanel panel = new SettingsPanel();
        new SettingsController(panel);
        mostrarPanel(panel);
    }

    //-----------------------------------   Metodo para abrir citas --------------------------//
    private void abrirInventario() {
        InventoryPanel panel = new InventoryPanel();
        new InventoryController(panel);
        mostrarPanel(panel);
    }

    //-----------------------------------   Metodo para abrir usuarios --------------------------//
    private void abrirUsuarios() {
        UsuariosPanel panel = new UsuariosPanel();
        new UsuariosController(panel);
        mostrarPanel(panel);
    }

    //---------------------------------Metodo configuracion panel------------------------//
    private void mostrarPanel(JPanel panel) {

        view.jPanel2.removeAll();
        view.jPanel2.setLayout(new BorderLayout());
        view.jPanel2.add(panel, BorderLayout.CENTER);
        view.jPanel2.revalidate();
        view.jPanel2.repaint();
    }
}
