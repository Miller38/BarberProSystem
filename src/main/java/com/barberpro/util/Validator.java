package com.barberpro.util;

public class Validator {

    // VALIDAR NOMBRE
    public static boolean nombreValido(
            String nombre) {

        return nombre != null
                && !nombre.trim().isEmpty()
                && nombre.length() >= 3
                && nombre.length() <= 100;
    }

    // VALIDAR TELEFONO
    public static boolean telefonoValido(
            String telefono) {

        return telefono != null
                && telefono.matches("\\d{7,15}");
    }

    // VALIDAR EMAIL
    public static boolean emailValido(
            String email) {

        return email != null
                && email.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}