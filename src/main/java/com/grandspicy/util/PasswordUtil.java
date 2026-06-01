package com.grandspicy.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    private static final int COSTE = 12;

    public static String cifrar(String contrasena) {
        return BCrypt.withDefaults().hashToString(COSTE, contrasena.toCharArray());
    }

    public static boolean verificar(String contrasena, String hashAlmacenado) {
        return BCrypt.verifyer().verify(contrasena.toCharArray(), hashAlmacenado).verified;
    }
}
