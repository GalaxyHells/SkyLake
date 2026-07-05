package com.galaxyhells.skylake.utils;

/**
 * Sistema de otimização de renderização com dirty flags
 */
public class RenderOptimizer {
    
    private static long lastRenderTime = 0;
    private static boolean needsRedraw = true;
    private static final int TARGET_FPS = 60;
    private static final long FRAME_INTERVAL = 1000 / TARGET_FPS;
    
    /**
     * Verifica se deve renderizar baseado no tempo
     */
    public static boolean shouldRender() {
        long now = System.currentTimeMillis();
        if (now - lastRenderTime >= FRAME_INTERVAL) {
            lastRenderTime = now;
            return true;
        }
        return false;
    }
    
    /**
     * Força redraw na próxima renderização
     */
    public static void markDirty() {
        needsRedraw = true;
    }
    
    /**
     * Marca que renderização foi concluída
     */
    public static void markClean() {
        needsRedraw = false;
    }
    
    /**
     * Verifica se precisa redesenhar
     */
    public static boolean isDirty() {
        return needsRedraw;
    }
    
    /**
     * Limita renderização para FPS específico
     */
    public static boolean shouldRenderAtFPS(int targetFPS) {
        long now = System.currentTimeMillis();
        long targetInterval = 1000 / targetFPS;
        if (now - lastRenderTime >= targetInterval) {
            lastRenderTime = now;
            return true;
        }
        return false;
    }
}
