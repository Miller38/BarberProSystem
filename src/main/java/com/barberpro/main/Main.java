package com.barberpro.main;

import com.barberpro.config.ActualizadorApp;
import com.barberpro.config.ConexionSQLite;
import com.barberpro.config.DataSeeder;
import com.barberpro.config.FontManager;
import com.barberpro.config.InitDatabase;
import com.barberpro.controller.LoginController;
import com.barberpro.dao.ConfiguracionDAO;
import com.barberpro.util.LicenseManager;
import com.barberpro.view.ActivationDialog;
import com.barberpro.view.LoginView;
import com.barberpro.view.SplashScreen;
import com.formdev.flatlaf.FlatDarkLaf;
import java.io.File;
import javax.swing.UIManager;

/**
 *
 * @author Miller
 */
public class Main {

    public static void main(String[] args) {
    
        // --- CÓDIGO DE PRUEBA TEMPORAL ---
    System.out.println("=== INICIO DE PRUEBA ===");
    System.out.println("Directorio actual: " + System.getProperty("user.dir"));
    
    File testDir = new File("data");
    if (testDir.exists()) {
        System.out.println("✅ La carpeta 'data' EXISTE");
    } else {
        System.out.println("❌ La carpeta 'data' NO EXISTE, creándola...");
        boolean creado = testDir.mkdirs();
        System.out.println("Creación exitosa: " + creado);
    }
    System.out.println("=== FIN DE PRUEBA ===");
    // --- FIN CÓDIGO DE PRUEBA ---

        try {
            //-------------------------------crear carpeta para añlojar bd---------------------------//
            // Crear carpeta data/ si no existe
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            // ------------------------------------Tema Moderno-------------------------------------//
            FlatDarkLaf.setup();

            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("ProgressBar.arc", 15);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("ScrollBar.width", 12);
            UIManager.put("Button.focusWidth", 0);
            // ------------------------------------Probar Conexion------------------------------------//
            ConexionSQLite.conectar();
            // ---------------------------------- Creamos las tablas----------------------------------//
            InitDatabase.crearTablas();
            //-----------------------------------creamos el usuario inicial----------------------------//
            DataSeeder.crearAdmin();

            //---------------------------------------------valida licencia -------------------------------//
            if (!LicenseManager.licenciaValida()) {
                ActivationDialog dialog = new ActivationDialog(null);  // ← CAMBIADO
                dialog.setVisible(true);

                if (!LicenseManager.licenciaValida()) {
                    System.exit(0);
                }
            }

            //---------------------------------------- abre splashScreen--------------------------------//
            SplashScreen splash = new SplashScreen();

            splash.setVisible(true);
            Thread.sleep(2500);
            splash.dispose();

            //--------------------------------cargamos la configuarcion de la fuente----------------//
            ConfiguracionDAO confiDAO = new ConfiguracionDAO();

            String fontSize
                    = confiDAO.obtener("font_size");

            if (fontSize == null
                    || fontSize.isEmpty()) {

                fontSize = "14";
            }

            FontManager.aplicarFuente(
                    Integer.parseInt(fontSize));
            System.out.println("Sistema Iniciado.");

            //-------------------------------------Abrimos el login----------------------------------//
            LoginView loginView = new LoginView();

            new LoginController(loginView);
            loginView.setVisible(true);
            

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

}
