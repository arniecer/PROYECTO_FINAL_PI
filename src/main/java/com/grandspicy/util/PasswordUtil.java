package com.grandspicy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    private static final String ALGORITMO = "SHA-256";

    public static String cifrar(String contrasena) {
        String sal = generarSal();
        String hash = calcularHash(contrasena, sal);
        return sal + ":" + hash;
    }

    public static boolean verificar(String contrasena, String hashAlmacenado) {
        if (hashAlmacenado == null) return false;
        String[] partes = hashAlmacenado.split(":");
        if (partes.length != 2) return false;
        String sal = partes[0];
        String hashEsperado = partes[1];
        String hashCalculado = calcularHash(contrasena, sal);
        return MessageDigest.isEqual(
            hashEsperado.getBytes(),
            hashCalculado.getBytes()
        );
    }

    private static String generarSal() {
        byte[] salBytes = new byte[16];
        new SecureRandom().nextBytes(salBytes);
        return Base64.getEncoder().encodeToString(salBytes);
    }

    private static String calcularHash(String contrasena, String sal) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITMO);
            md.update(sal.getBytes());
            byte[] hashBytes = md.digest(contrasena.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: " + ALGORITMO + " no disponible", e);
        }
    }
}
