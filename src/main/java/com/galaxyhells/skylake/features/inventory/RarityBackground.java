package com.galaxyhells.skylake.features.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.util.List;

public class RarityBackground {

    @SubscribeEvent
    public void onDrawSlot(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!com.galaxyhells.skylake.config.ConfigHandler.rarityBackground) return;

        if (!(event.gui instanceof GuiContainer)) return;

        GuiContainer gui = (GuiContainer) event.gui;

        // Pegamos as bordas do inventário via Reflection
        int guiLeft, guiTop;
        try {
            guiLeft = ReflectionHelper.getPrivateValue(GuiContainer.class, gui, "guiLeft", "field_147003_i");
            guiTop = ReflectionHelper.getPrivateValue(GuiContainer.class, gui, "guiTop", "field_147009_r");
        } catch (Exception e) { return; }

        for (Slot slot : gui.inventorySlots.inventorySlots) {
            ItemStack item = slot.getStack();
            if (item == null) continue;

            int color = getRarityColor(item);
            if (color != 0) {
                int x = guiLeft + slot.xDisplayPosition;
                int y = guiTop + slot.yDisplayPosition;

                // Desenha o fundo colorido (com transparência 0x60 para não ficar opaco)
                Gui.drawRect(x, y, x + 16, y + 16, color);
            }
        }
    }

    @SubscribeEvent
    public void onRenderHotbar(RenderGameOverlayEvent.Post event) {
        // Verifica se a config está ativa e se estamos na fase de desenhar a Hotbar
        if (!com.galaxyhells.skylake.config.ConfigHandler.rarityBackground ||
                event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        ScaledResolution sr = new ScaledResolution(mc);

        // O início da Hotbar (o primeiro slot) é calculado assim:
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
                    Gui.drawRect(x + 1, y + 1, x + 20, y + 20, color);
                }
            }
        }
    }

    private int getRarityColor(ItemStack item) {
        // Pegamos o Lore do item
        List<String> tooltip = item.getTooltip(Minecraft.getMinecraft().thePlayer, false);
        if (tooltip.isEmpty()) return 0;

        // Geralmente a raridade é uma das últimas linhas, mas vamos varrer o texto
        for (String line : tooltip) {
            String cleanLine = line.toUpperCase();

            if (cleanLine.contains("ESPECIAL")) return 0x60FF55FF; // Rosa/Especial

            if (cleanLine.contains("LENDÁRIO")) return 0x60FFAA00; // Laranja/Gold
            if (cleanLine.contains("LENDÁRIA")) return 0x60FFAA00; // Laranja/Gold

            if (cleanLine.contains("ÉPICO"))    return 0x60AA00AA; // Roxo
            if (cleanLine.contains("ÉPICA"))    return 0x60AA00AA; // Roxo

            if (cleanLine.contains("RARO"))     return 0x605555FF; // Azul
            if (cleanLine.contains("RARA"))     return 0x605555FF; // Azul

            if (cleanLine.contains("INCOMUM"))  return 0x6055FF55; // Verde

            if (cleanLine.contains("COMUM"))    return 0x60FFFFFF; // Branco (Opcional)
        }

        return 0;
    }
}