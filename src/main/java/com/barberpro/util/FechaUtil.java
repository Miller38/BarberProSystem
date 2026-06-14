package com.barberpro.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FechaUtil {

    private static final ZoneId ZONA =
            ZoneId.of("America/Bogota");

    public static String ahora() {

        return ZonedDateTime.now(ZONA)
                .format(
                        DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss"));
    }
}