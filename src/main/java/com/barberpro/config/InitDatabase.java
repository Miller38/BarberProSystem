    package com.barberpro.config;

 import java.sql.Connection;
 import java.sql.Statement;

 /**
  * @author Miller
  */
 public class InitDatabase {

     public static void crearTablas() {

         // TABLA USUARIOS
         String sqlUsuarios = 
             "CREATE TABLE IF NOT EXISTS usuarios (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "nombre TEXT NOT NULL," +
             "usuario TEXT UNIQUE NOT NULL," +
             "password TEXT NOT NULL," +
             "rol TEXT NOT NULL," +
             "email TEXT," +
             "telefono TEXT," +
             "foto TEXT," +
             "estado INTEGER DEFAULT 1," +
             "intentos INTEGER DEFAULT 0," +
             "bloqueado INTEGER DEFAULT 0," +
             "bloqueado_hasta TEXT," +
             "ultimo_acceso TEXT," +
             "fecha_creacion TEXT DEFAULT CURRENT_TIMESTAMP" +
             ")";

         // TABLA CLIENTES
         String sqlClientes = 
             "CREATE TABLE IF NOT EXISTS clientes (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "nombre TEXT NOT NULL," +
             "telefono TEXT UNIQUE," +
             "email TEXT UNIQUE," +
             "direccion TEXT," +
             "estado INTEGER DEFAULT 1," +
             "fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP," +
             "foto TEXT" +        
             ")";

         // TABLA SERVICIOS
         String sqlServicios = 
             "CREATE TABLE IF NOT EXISTS servicios (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "nombre TEXT NOT NULL," +
             "categoria TEXT NOT NULL," +
             "precio REAL NOT NULL," +
             "estado INTEGER DEFAULT 1" +
             ")";

         // TABLA CITAS
         String sqlCitas = 
             "CREATE TABLE IF NOT EXISTS citas (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "cliente_id INTEGER NOT NULL," +
             "servicio_id INTEGER NOT NULL," +
             "fecha TEXT NOT NULL," +
             "hora TEXT NOT NULL," +
             "estado TEXT NOT NULL," +
             "FOREIGN KEY (cliente_id) REFERENCES clientes(id)," +
             "FOREIGN KEY (servicio_id) REFERENCES servicios(id)" +
             ")";

         // TABLA VENTAS
         String sqlVentas = 
             "CREATE TABLE IF NOT EXISTS ventas (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "cliente_id INTEGER NOT NULL," +            
             "usuario TEXT," +
             "subtotal REAL," +
             "impuesto REAL," +
             "descuento REAL," +
             "total REAL," +
             "metodo_pago TEXT," +
             "fecha TEXT DEFAULT CURRENT_TIMESTAMP," +
             "estado TEXT DEFAULT 'ACTIVA'," +
             "FOREIGN KEY (cliente_id) REFERENCES clientes(id)" +            
             ")";

         // TABLA PRODUCTOS
         String sqlProductos = 
             "CREATE TABLE IF NOT EXISTS productos (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "nombre TEXT NOT NULL," +
             "categoria TEXT," +
             "stock INTEGER DEFAULT 0," +
             "stock_minimo INTEGER DEFAULT 5," +
             "precio_compra REAL," +
             "precio_venta REAL," +
             "fecha_vencimiento TEXT," +
             "estado INTEGER DEFAULT 1" +
             ")";

         // TABLA DETALLE_VENTA
         String sqlDetalle = 
             "CREATE TABLE IF NOT EXISTS detalle_ventas (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "venta_id INTEGER NOT NULL," +
             "tipo_item TEXT NOT NULL," +
             "item_id INTEGER NOT NULL," +
             "nombre TEXT NOT NULL," + 
             "cantidad INTEGER NOT NULL," +
             "precio_unitario REAL NOT NULL," +              
             "subtotal REAL NOT NULL," +            
             "FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE" +           
             ")";

         // TABLA LOGS_LOGIN
         String sqlLogs = 
             "CREATE TABLE IF NOT EXISTS logs_login (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "usuario TEXT NOT NULL," +
             "fecha TEXT NOT NULL," +
             "estado TEXT NOT NULL," +
             "mensaje TEXT" +
             ")";

         // TABLA MOVIMIENTOS
         String sqlMovimientos = 
             "CREATE TABLE IF NOT EXISTS movimientos (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "producto_id INTEGER NOT NULL," +
             "tipo TEXT NOT NULL," +
             "cantidad INTEGER NOT NULL," +
             "motivo TEXT," +
             "fecha TEXT DEFAULT CURRENT_TIMESTAMP," +
             "FOREIGN KEY (producto_id) REFERENCES productos(id)" +
             ")";

         // TABLA CONFIGURACION
         String sqlConfiguracion = 
             "CREATE TABLE IF NOT EXISTS configuracion (" +
             "clave TEXT PRIMARY KEY," +
             "valor TEXT NOT NULL" +
             ")";

         //------------------------Auditoria-----------------------------//
         String sqlAuditoria = "CREATE TABLE IF NOT EXISTS auditoria (" + 
                 "id INTEGER PRIMARY KEY AUTOINCREMENT," + 
                 "usuario TEXT NOT NULL," + 
                 "accion TEXT NOT NULL," + 
                 "fecha TEXT DEFAULT CURRENT_TIMESTAMP" + 
                 ")";

         //------------------------Sesiones----------------------------//
         String sqlSesiones = "CREATE TABLE IF NOT EXISTS sesiones (" + 
                 "id INTEGER PRIMARY KEY AUTOINCREMENT," + 
                 "usuario TEXT NOT NULL," + 
                 "login TEXT," + 
                 "logout TEXT" + 
                 ")";

         //-----------------------Permisos-----------------------------//
         String sqlPermisos = "CREATE TABLE IF NOT EXISTS permisos (" + 
                 "id INTEGER PRIMARY KEY AUTOINCREMENT," + 
                 "usuario_id INTEGER NOT NULL," + 
                 "modulo TEXT NOT NULL," + 
                 "permitido INTEGER DEFAULT 1," + 
                 "FOREIGN KEY(usuario_id) REFERENCES usuarios(id)" + 
                 ")";

         try (Connection conn = ConexionSQLite.conectar(); 
              Statement stmt = conn.createStatement()) {

             // Crear tablas en orden correcto
             stmt.execute(sqlUsuarios);
             stmt.execute(sqlClientes);
             stmt.execute(sqlServicios);
             stmt.execute(sqlProductos);
             stmt.execute(sqlVentas);
             stmt.execute(sqlCitas);
             stmt.execute(sqlDetalle);
             stmt.execute(sqlLogs);
             stmt.execute(sqlMovimientos);
             stmt.execute(sqlConfiguracion);
             stmt.execute(sqlAuditoria);
             stmt.execute(sqlSesiones);
             stmt.execute(sqlPermisos);

             System.out.println("***********************");
             System.out.println("Tablas creadas con éxito.");
             System.out.println("***********************");

         } catch (Exception e) {
             System.out.println("Error al crear las tablas: " + e.getMessage());
             e.printStackTrace();
         }
     }
 }