package com.galaxyhells.skylake.features.hud.itemlog;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Sistema de log de itens coletados
 * Exibe notificações na tela quando o jogador coleta itens
 */
public class ItemLogFeature {
    private final List<ItemLogEntry> entries = new ArrayList<>();
    private static final int MAX_ENTRIES = 5;
    private static final int DISPLAY_DURATION = 3000; // 3 segundos
    private static final float START_Y_OFFSET = 50;
    private static final float ENTRY_SPACING = 20;

    public ItemLogFeature() {
    }

    /**
     * Adiciona uma entrada ao log
     */
    public void addEntry(String itemName, int quantity) {
        ItemLogEntry entry = new ItemLogEntry(itemName, quantity);
        
        // Define posição inicial baseada no número de entradas
        entry.setDisplayY(START_Y_OFFSET + (entries.size() * ENTRY_SPACING));
        
        entries.add(entry);
        
        // Limita o número de entradas
        if (entries.size() > MAX_ENTRIES) {
            entries.remove(0);
        }
    }

    /**
     * Adiciona uma entrada baseada em um ItemStack
     */
    public void addEntry(ItemStack stack) {
        if (stack == null) return;
        
        String itemName = stack.getDisplayName();
        int quantity = stack.stackSize;
        
        addEntry(itemName, quantity);
    }

    /**
     * Renderiza as notificações na tela
     */
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        // Só renderiza na fase de TEXT
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);

        // Remove entradas expiradas
        entries.removeIf(ItemLogEntry::isExpired);

        // Atualiza posições e alpha das entradas
        updateEntries();

        // Renderiza cada entrada
        for (int i = 0; i < entries.size(); i++) {
            ItemLogEntry entry = entries.get(i);
            String text = entry.getDisplayText();
            
            int x = 10;///sr.getScaledWidth() / 2;
            int y = (int) entry.getDisplayY();
            
            // Centraliza o texto
            int textWidth = mc.fontRendererObj.getStringWidth(text);
            //x = x - (textWidth / 2);

            // Desenha o texto com alpha
            int color = 0xFFFFFF;
            mc.fontRendererObj.drawStringWithShadow(text, x, y + 50, color);
        }
    }

    /**
     * Atualiza posições e alpha das entradas para animação
     */
    private void updateEntries() {
        long currentTime = System.currentTimeMillis();
        
        for (int i = 0; i < entries.size(); i++) {
            ItemLogEntry entry = entries.get(i);
            long age = currentTime - entry.getTimestamp();
            
            // Calcula alpha baseado na idade (fade out nos últimos 500ms)
            if (age > DISPLAY_DURATION - 500) {
                float remaining = DISPLAY_DURATION - age;
                entry.setAlpha(remaining / 500.0F);
            } else {
                entry.setAlpha(1.0F);
            }
            
            // Atualiza posição (slide up)
            float targetY = START_Y_OFFSET + (i * ENTRY_SPACING);
            entry.setDisplayY(targetY);
        }
    }

    /**
     * Limpa todas as entradas
     */
    public void clear() {
        entries.clear();
    }

    /**
     * Retorna o número de entradas ativas
     */
    public int getEntryCount() {
        return entries.size();
    }
}
