package com.galaxyhells.skylake.features.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.List;

public class RarityBackground {
    
    // Modos de renderização de raridade
    public static int RENDER_MODE = 1; // 0 = Fundo completo, 1 = 2 pixels inferior
    
    public static final int RENDER_MODE_FULL = 0;
    public static final int RENDER_MODE_BOTTOM_PIXELS = 1;

    /**
     * Este evento roda especificamente antes de cada slot ser desenhado.
     * É perfeito porque o que desenharmos aqui fica naturalmente ATRÁS do item.
     */
    @SubscribeEvent
    public void onDrawSlot(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!com.galaxyhells.skylake.config.ConfigHandler.rarityBackground) return;
        if (!(event.gui instanceof GuiContainer)) return;

        GuiContainer gui = (GuiContainer) event.gui;

        // Pegamos as bordas do inventário via Reflection
        int guiLeft, guiTop;
        try {
            guiLeft = ReflectionHelper.getPrivateValue(GuiContainer.class, gui, "guiLeft", "field_147003_i");
            guiTop = ReflectionHelper.getPrivateValue(GuiContainer.class, gui, "guiTop", "field_147009_r");
        } catch (Exception e) { return; }

        GlStateManager.pushMatrix();

        // Habilita o que é necessário para a transparência e profundidade
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        //GlStateManager.disableDepth(); // Desativa depth test para renderização 2D
        GlStateManager.enableDepth(); // Crucial para não "vazar" para o fundo do mundo

        // O PULO DO GATO:
        // O fundo do inventário está em Z=0.
        // As tooltips e itens começam do Z=100 para cima.
        // Usar Z=1.0F coloca sua cor exatamente COLADA na frente do fundo cinza,
        // mas garante que QUALQUER coisa do inventário fique por cima.
        GlStateManager.translate(0, 0, 1.0F);

        for (Slot slot : gui.inventorySlots.inventorySlots) {
            ItemStack item = slot.getStack();
            if (item == null) continue;

            int color = getRarityColor(item);
            if (color != 0) {
                int x = guiLeft + slot.xDisplayPosition;
                int y = guiTop + slot.yDisplayPosition;

                // Desenha o fundo colorido
                drawRarityBackground(x, y, color, RENDER_MODE);
            }
        }

        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onRenderHotbar(RenderGameOverlayEvent.Post event) {
        if (!com.galaxyhells.skylake.config.ConfigHandler.rarityBackground || event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int xBase = sr.getScaledWidth() / 2 - 90;
        int y = sr.getScaledHeight() - 22;

        for (int i = 0; i < 9; i++) {
            ItemStack item = mc.thePlayer.inventory.mainInventory[i];
            if (item != null) {
                int color = getRarityColor(item);
                if (color != 0) {
                    int x = xBase + (i * 20); // Cada slot tem 20 pixels de distância

                    // Desenha o fundo atrás do item da hotbar

                    // Usamos x+1 e y+1 para centralizar perfeitamente no quadradinho
                    drawRarityBackground(x + 1, y + 1, color, RENDER_MODE);
                }
            }
        }
    }

    private int getRarityColor(ItemStack item) {
        if (item == null) return 0;
        List<String> tooltip = item.getTooltip(Minecraft.getMinecraft().thePlayer, false);
        if (tooltip.isEmpty()) return 0;

        for (String line : tooltip) {
            String cleanLine = line.toUpperCase();
            if (cleanLine.contains("ESPECIAL")) return 0x70FF003F;
            if (cleanLine.contains("MÍSTICA") || cleanLine.contains("MÍSTICO")) return 0x70FF55FF;
            if (cleanLine.contains("LENDÁRIO") || cleanLine.contains("LENDÁRIA")) return 0x70FFAA00;
            if (cleanLine.contains("ÉPICO") || cleanLine.contains("ÉPICA"))       return 0x70AA00AA;
            if (cleanLine.contains("RARO") || cleanLine.contains("RARA"))         return 0x705555FF;
            if (cleanLine.contains("INCOMUM"))                                    return 0x7055FF55;
            if (cleanLine.contains("COMUM"))                                      return 0x70FFFFFF;
        }
        return 0;
    }
    
    /**
     * Desenha o fundo de raridade conforme o modo especificado
     * @param x Posi��o X inicial
     * @param y Posi��o Y inicial
     * @param color Cor do fundo
     * @param renderMode Modo de renderiza��o (RENDER_MODE_FULL ou RENDER_MODE_BOTTOM_PIXELS)
     */
    private void drawRarityBackground(int x, int y, int color, int renderMode) {
        switch (renderMode) {
            case RENDER_MODE_FULL:
                // Modo original: fundo completo do slot
                Gui.drawRect(x, y, x + 16, y + 16, color);
                break;
                
            case RENDER_MODE_BOTTOM_PIXELS:
                // Novo modo: apenas 2 pixels na parte inferior
                Gui.drawRect(x, y + 14, x + 16, y + 16, color);
                break;
                
            default:
                // Fallback para modo completo
                Gui.drawRect(x, y, x + 16, y + 16, color);
                break;
        }
    }
}
