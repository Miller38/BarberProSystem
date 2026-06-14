
package com.barberpro.controller;

import com.barberpro.config.Session;
import com.barberpro.dao.UsuarioDAO;
import com.barberpro.model.Usuario;
import com.barberpro.view.Dashboard;
import com.barberpro.view.LoginView;
import javax.swing.JOptionPane;
import com.barberpro.controller.DashboardController;

/**
 *
 * @author Miller
 */
public class LoginController {
    
    private LoginView view;
    private UsuarioDAO dao;
    private int intentos = 0;
    private final int MAX_INTENTOS = 3;
    
    public LoginController(LoginView view) {
        this.view = view;
        dao = new UsuarioDAO();
        
        iniciarEventos();
    }
    
    private void iniciarEventos() {
         // BOTON LOGIN
         view.btnLogin.addActionListener(e -> login());
         
          // MOSTRAR PASSWORD
          view.chekMostrarPassword.addItemListener(e -> mostrarPassword());
          
    }
    
    //-----------------------------------------metodo para loguearse ------------------------------------//
    private void login() {
         String usuario = view.txtUsuario.getText();
         String password = String.valueOf(view.txtPassword.getText());
         
          // VALIDACIONES
          if(usuario.isEmpty() || password.isEmpty()) {
              JOptionPane.showMessageDialog(null,"Complete todos los campos.");
              
              return;
          }
          
          Usuario usuarioLogin = dao.login(usuario, password);
          
          if(usuarioLogin != null) {
              //JOptionPane.showMessageDialog(null,"Bienvenido." + usuarioLogin.getNombre());
              
              Session.setUsuarioActual(usuarioLogin);
              
              Dashboard dashboard = new Dashboard();
              
              new  DashboardController(dashboard);
              
              dashboard.setVisible(true);
              view.dispose();
          } else  {
              
              intentos++;
              
              int restantes = MAX_INTENTOS - intentos;
              
              JOptionPane.showMessageDialog(
                      null,
                      "Credenciales incorrectas\n"
                      + "Intentos restantes : "
                      + restantes
              );
              
              if(intentos >= MAX_INTENTOS) {
                  JOptionPane.showMessageDialog(null, "Sistema bloqueado");
                  //System.exit(0);
                  view.dispose();
              }
          }
    }
    
    //-------------------------------------------- metodo mostrar password------------------------------//
    private void mostrarPassword() {
        if(view.chekMostrarPassword.getState()) {
            view.txtPassword.setEchoChar((char) 0);
        } else {
            view.txtPassword.setEchoChar('*');
        }
    }
    
}
