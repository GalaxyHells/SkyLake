package com.galaxyhells.skylake.features.hud.timer;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.OptionType;
import com.galaxyhells.skylake.utils.ThemeManager;
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
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final int[] SPAWN_HOURS = {6, 14, 22};

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        // Verificar se feature está ativa
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MAGMA_TIMER))) return;
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getMinecraft();

        // Calcular próximo spawn
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int[] spawns = SPAWN_HOURS;
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

        // Cores do timer
        int textColor = 0xFF55FF55; // Verde
        if (diffMillis < TimeUnit.MINUTES.toMillis(30)) textColor = 0xFFFFFF55; // Amarelo (< 30 min)
        if (diffMillis < TimeUnit.MINUTES.toMillis(10)) textColor = 0xFFFF5555; // Vermelho (< 10 min)

        // Renderização
        ScaledResolution sr = new ScaledResolution(mc);

        // Posição abaixo do Mutant Timer

        int width = ThemeManager.scale(75); // Largura para HH:mm:ss
        int height = ThemeManager.scale(20);
        int x = sr.getScaledWidth() - width - ThemeManager.scale(10);
        int y = ThemeManager.scale(35);

        // Fundo
        Gui.drawRect(x, y, x + width, y + height, 0x90000000);

        // Borda laranja
        Gui.drawRect(x, y, x + 2, y + height, 0xFFFFAA00);

        // Ícone Magma Cream
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();

        mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.magma_cream), x + ThemeManager.scale(5), y + ThemeManager.scale(2));

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        // Texto do timer
        mc.fontRendererObj.drawStringWithShadow(timeText, x + ThemeManager.scale(24), y + ThemeManager.scale(6), textColor);
    }

    private String formatTime(long millis) {
        long h = TimeUnit.MILLISECONDS.toHours(millis);
        long m = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long s = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}