package com.galaxyhells.skylake.utils;

import java.util.Base64;

/**
 * Utilitários simples para criptografia de senhas
 * Usa XOR + Base64 para ofuscação básica (não é criptografia forte, mas melhor que texto plano)
 */
public class CryptoUtils {
    
    private static final String XOR_KEY = "SkyLake2024";
    
    /**
     * Criptografa uma string usando XOR + Base64
     */
    public static String encrypt(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        
        byte[] bytes = input.getBytes();
        byte[] keyBytes = XOR_KEY.getBytes();
        
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] ^= keyBytes[i % keyBytes.length];
        }
        
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    /**
     * Descriptografa uma string criptografada
     */
    public static String decrypt(String encrypted) {
        if (encrypted == null || encrypted.isEmpty()) {
            return "";
        }
        
        try {
            byte[] bytes = Base64.getDecoder().decode(encrypted);
            byte[] keyBytes = XOR_KEY.getBytes();
            
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] ^= keyBytes[i % keyBytes.length];
            }
            
            return new String(bytes);
        } catch (Exception e) {
            System.err.println("[SkyLake] Erro ao descriptografar: " + e.getMessage());
            return "";
        }
    }
}
