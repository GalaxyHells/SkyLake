package com.galaxyhells.skylake.utils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.Stack;

/**
 * Pool de objetos para renderização eficiente - reutiliza objetos em vez de criar/destruir
 */
public class RenderPool {
    
    private static final Stack<Tessellator> tessellatorPool = new Stack<>();
    private static final Stack<WorldRenderer> worldRendererPool = new Stack<>();
    private static final Stack<Color> colorPool = new Stack<>();
    
    /**
     * Obtém Tessellator do pool ou cria novo
     */
    public static Tessellator getTessellator() {
        if (!tessellatorPool.isEmpty()) {
            tessellatorPool.push(Tessellator.getInstance());
        }
        return tessellatorPool.pop();
    }
    
    /**
     * Devolve Tessellator ao pool
     */
    public static void returnTessellator(Tessellator tessellator) {
        if (tessellatorPool.size() < 10) { // Limita pool size
            tessellatorPool.push(tessellator);
        }
    }
    
    /**
     * Obtém WorldRenderer do pool ou cria novo
     */
    public static WorldRenderer getWorldRenderer() {
        if (!worldRendererPool.isEmpty()) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer renderer = tessellator.getWorldRenderer();
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            worldRendererPool.push(renderer);
        }
        return worldRendererPool.pop();
    }
    
    /**
     * Devolve WorldRenderer ao pool
     */
    public static void returnWorldRenderer(WorldRenderer renderer) {
        if (worldRendererPool.size() < 10) {
            worldRendererPool.push(renderer);
        }
    }
    
    /**
     * Obtém Color do pool ou cria novo
     */
    public static Color getColor(int r, int g, int b, int a) {
        if (!colorPool.isEmpty()) {
            colorPool.push(new Color(r, g, b, a));
        }
        return colorPool.pop();
    }
    
    /**
     * Devolve Color ao pool
     */
    public static void returnColor(Color color) {
        if (colorPool.size() < 20) { // Mais cores são usadas
            colorPool.push(color);
        }
    }
    
    /**
     * Configura estado de renderização otimizado
     */
    public static void setupRenderState() {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
    }
    
    /**
     * Limpa estado de renderização
     */
    public static void cleanupRenderState() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    /**
     * Limpa pools (chamado no shutdown)
     */
    public static void clear() {
        tessellatorPool.clear();
        worldRendererPool.clear();
        colorPool.clear();
    }
}
