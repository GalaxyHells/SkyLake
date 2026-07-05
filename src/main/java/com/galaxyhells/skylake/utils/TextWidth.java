package com.galaxyhells.skylake.utils;

import net.minecraft.client.Minecraft;

public class TextWidth {

    // 1. Para o Minecraft Padrão
    public static int getStringWidth(String texto) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(texto);
    }

    // 2. Para Fontes AWT/System (Caso você use uma classe customizada de fontes .ttf)
    // Armazene o FontMetrics como um campo da classe, não crie um novo a cada chamada!
    private static java.awt.FontMetrics cachedMetrics;

    public static int getSystemFontWidth(String texto, java.awt.Font fonte) {
        // Inicializa o cache apenas uma vez se ainda não existir
        if (cachedMetrics == null || !cachedMetrics.getFont().equals(fonte)) {
            java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics g = img.getGraphics();
            cachedMetrics = g.getFontMetrics(fonte);
            g.dispose();
        }
        return cachedMetrics.stringWidth(texto);
    }
}