package com.galaxyhells.skylake.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Utilitários para otimização de performance
 */
public class PerformanceUtils {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static ScaledResolution cachedResolution;
    private static long lastResolutionUpdate = 0;
    
    /**
     * Obtém instância do Minecraft sem criar nova a cada chamada
     */
    public static Minecraft getMC() {
        return mc;
    }
    
    /**
     * Obtém ScaledResolution com cache para evitar criação excessiva
     */
    public static ScaledResolution getScaledResolution() {
        long now = System.currentTimeMillis();
        if (now - lastResolutionUpdate > 1000) { // Atualiza a cada 1s
            cachedResolution = new ScaledResolution(mc);
            lastResolutionUpdate = now;
        }
        return cachedResolution;
    }
    
    /**
     * Verifica se deve renderizar baseado no tempo (limita FPS)
     */
    public static boolean shouldRender(long lastRenderTime, int targetFPS) {
        long now = System.currentTimeMillis();
        long targetInterval = 1000 / targetFPS;
        return now - lastRenderTime >= targetInterval;
    }
    
    /**
     * Validações seguras para evitar NPE
     */
    public static boolean isValidPlayer() {
        return mc != null && mc.thePlayer != null && mc.theWorld != null;
    }
    
    /**
     * Validações seguras para inventário
     */
    public static boolean hasValidInventory() {
        return isValidPlayer() && mc.thePlayer.inventory != null;
    }
}
