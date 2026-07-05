package com.galaxyhells.skylake.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Sistema unificado de notificações com animações suaves
 */
public class NotificationManager {
    
    private static final ConcurrentLinkedQueue<Notification> queue = new ConcurrentLinkedQueue<>();
    private static Notification currentNotification = null;
    private static long notificationStartTime = 0;
    private static final float ANIMATION_DURATION = 0.5f; // 0.5 segundos
    
    private static class Notification {
        final String message;
        final Type type;
        final long timestamp;
        
        Notification(String message, Type type) {
            this.message = message;
            this.type = type;
            this.timestamp = System.currentTimeMillis();
        }
    }
    
    public enum Type {
        SUCCESS(EnumChatFormatting.GREEN, "✓"),
        ERROR(EnumChatFormatting.RED, "✗"),
        WARNING(EnumChatFormatting.YELLOW, "⚠"),
        INFO(EnumChatFormatting.AQUA, "ℹ");
        
        final EnumChatFormatting color;
        final String icon;
        
        Type(EnumChatFormatting color, String icon) {
            this.color = color;
            this.icon = icon;
        }
    }
    
    /**
     * Adiciona notificação na fila
     */
    public static void show(String message, Type type) {
        queue.offer(new Notification(message, type));
    }
    
    /**
     * Atalhos para tipos comuns
     */
    public static void success(String message) {
        show(message, Type.SUCCESS);
    }
    
    public static void error(String message) {
        show(message, Type.ERROR);
    }
    
    public static void warning(String message) {
        show(message, Type.WARNING);
    }
    
    public static void info(String message) {
        show(message, Type.INFO);
    }
    
    /**
     * Processa animações de notificações (chamado no tick)
     */
    public static void update() {
        long now = System.currentTimeMillis();
        
        // Se não há notificação atual, tenta pegar da fila
        if (currentNotification == null && !queue.isEmpty()) {
            currentNotification = queue.poll();
            notificationStartTime = now;
        }
        
        // Se a animação atual terminou, limpa
        if (currentNotification != null) {
            float elapsed = (now - notificationStartTime) / 1000f;
            if (elapsed >= ANIMATION_DURATION) {
                currentNotification = null;
            }
        }
    }
    
    /**
     * Renderiza notificações ativas (chamado no render)
     */
    public static void render() {
        if (currentNotification == null) return;
        
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;
        
        long now = System.currentTimeMillis();
        float elapsed = (now - notificationStartTime) / 1000f;
        float progress = Math.min(elapsed / ANIMATION_DURATION, 1.0f);
        
        // Animação de fade-in
        int alpha = (int) (progress * 255);
        
        String prefix = currentNotification.type.color + 
                      currentNotification.type.icon + " [SkyLake]§r ";
        String fullMessage = prefix + currentNotification.message;
        
        // Efeito de "shake" para erros
        int yOffset = 0;
        if (currentNotification.type == Type.ERROR && progress < 0.3f) {
            yOffset = (int) (Math.sin(now / 50.0) * 3);
        }
        
        // Renderiza mensagem
        mc.fontRendererObj.drawStringWithShadow(
            fullMessage, 
            10, 
            10 + yOffset, 
            (alpha << 24) | 0xFFFFFF // Alpha + cor branca
        );
    }
}
