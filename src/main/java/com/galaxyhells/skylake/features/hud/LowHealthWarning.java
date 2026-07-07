package com.galaxyhells.skylake.features.hud;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.ActionbarParser;
import com.galaxyhells.skylake.utils.OptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LowHealthWarning {

    private static final float HEALTH_THRESHOLD = 0.2f; // 20%
    private static final int FLASH_COLOR = 0x80FF0000; // Vermelho translúcido

    @SubscribeEvent
    public void onRenderPost(RenderGameOverlayEvent.Post event) {
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.LOW_HEALTH_WARNING))) return;

        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        // Calcula porcentagem de vida
        float healthPercentage = 0;
        if (ActionbarParser.maxHP > 0) {
            healthPercentage = (float) ActionbarParser.currentHP / ActionbarParser.maxHP;
        }

        // Se vida estiver abaixo de 20%, pisca tela em vermelho
        if (healthPercentage < HEALTH_THRESHOLD && healthPercentage > 0) {
            ScaledResolution sr = new ScaledResolution(mc);
            int screenWidth = sr.getScaledWidth();
            int screenHeight = sr.getScaledHeight();

            // Efeito de piscar usando seno baseado no tempo do sistema
            long time = System.currentTimeMillis() / 200; // Controla velocidade do flash
            float flashIntensity = (float) (Math.sin(time) + 1) / 2; // 0 a 1
            int alpha = (int) (flashIntensity * 128); // Alpha de 0 a 128

            // Desenha overlay vermelho em toda a tela
            Gui.drawRect(0, 0, screenWidth, screenHeight, (alpha << 24) | 0xFF0000);
        }
    }
}
