package com.galaxyhells.skylake.features.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MagmaTimer {

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        // Verifica se a feature está ligada na sua config
        if (!com.galaxyhells.skylake.config.ConfigHandler.magmaTimer) return;
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getMinecraft();

        // --- LÓGICA DE TEMPO ---
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int[] spawns = {6, 14, 22};
        int nextSpawnHour = -1;

        for (int spawn : spawns) {
            if (currentHour < spawn) {
                nextSpawnHour = spawn;
                break;
            }
        }

        Calendar target = (Calendar) now.clone();
        if (nextSpawnHour == -1) {
            target.set(Calendar.HOUR_OF_DAY, 6);
            target.set(Calendar.MINUTE, 0);
            target.set(Calendar.SECOND, 0);
            target.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            target.set(Calendar.HOUR_OF_DAY, nextSpawnHour);
            target.set(Calendar.MINUTE, 0);
            target.set(Calendar.SECOND, 0);
        }

        long diffMillis = target.getTimeInMillis() - now.getTimeInMillis();
        String timeText = formatTime(diffMillis);

        // --- SISTEMA DE CORES ---
        int textColor = 0xFF55FF55; // Verde
        if (diffMillis < TimeUnit.MINUTES.toMillis(30)) textColor = 0xFFFFFF55; // Amarelo (< 30 min)
        if (diffMillis < TimeUnit.MINUTES.toMillis(10)) textColor = 0xFFFF5555; // Vermelho (< 10 min)

        // --- RENDERIZAÇÃO VISUAL ---
        ScaledResolution sr = new ScaledResolution(mc);

        // Posição: Abaixo do Mutant Timer (se o Mutant está no y=10, este vai para o y=35)
        int x = 10;
        int y = 35;
        int width = 75; // Um pouco mais largo para caber HH:mm:ss
        int height = 20;

        // Fundo escuro transparente
        Gui.drawRect(x, y, x + width, y + height, 0x90000000);

        // Borda lateral cor de Fogo/Lava (Dourado/Laranja)
        Gui.drawRect(x, y, x + 2, y + height, 0xFFFFAA00);

        // --- ÍCONE (Magma Cream) ---
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();

        mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.magma_cream), x + 5, y + 2);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        // Desenha o texto do tempo
        mc.fontRendererObj.drawStringWithShadow(timeText, x + 24, y + 6, textColor);
    }

    private String formatTime(long millis) {
        long h = TimeUnit.MILLISECONDS.toHours(millis);
        long m = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long s = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}