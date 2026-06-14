package com.barberpro.util;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupManager {

    private static final String DB =
            "barberia.db";

    // -------------------------------------CREAR BACKUP------------------------------------//
    public static void crearBackup() {

        try {
            File carpeta =  new File("backups");

            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            String fecha = LocalDateTime.now()
                            .format(DateTimeFormatter
                                            .ofPattern("yyyyMMdd_HHmmss"));

            File origen = new File(DB);

            File destino =  new File("backups/backup_"
                            + fecha
                            + ".db");

            Files.copy(
                    origen.toPath(),
                    destino.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            System.out.println(
                    "Backup creado");

        } catch (Exception e) {

            System.out.println(
                    e.getMessage());
        }
    }
    
    //-------------------------------------------------------------------------------------------//
    public static void restaurarBackup(
        File backup) {

    try {
        File destino =  new File(DB);

        Files.copy(
                backup.toPath(),
                destino.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        System.out.println(
                "Backup restaurado");

    } catch (Exception e) {

        System.out.println(
                e.getMessage());
    }
}
}