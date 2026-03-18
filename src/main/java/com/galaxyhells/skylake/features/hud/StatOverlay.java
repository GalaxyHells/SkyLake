package com.galaxyhells.skylake.features.hud;

import com.galaxyhells.skylake.utils.ActionbarParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StatOverlay {

    @SubscribeEvent
    public void onActionbar(ClientChatReceivedEvent event) {
        // Tipo 2 é a Actionbar no Minecraft 1.8.9
        if (event.type == 2) {
            ActionbarParser.parse(event.message.getUnformattedText());
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre event) {
        if (!com.galaxyhells.skylake.config.ConfigHandler.statOverlay) return;

        // Esconde os corações e a barra de comida originais
        if (event.type == RenderGameOverlayEvent.ElementType.HEALTH ||
                event.type == RenderGameOverlayEvent.ElementType.FOOD) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderPost(RenderGameOverlayEvent.Post event) {
        if (!com.galaxyhells.skylake.config.ConfigHandler.statOverlay) return;

        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();

        // Posições base (em cima de onde seriam os corações e fome)
        int xHP = screenWidth / 2 - 91;
        int xMana = screenWidth / 2 + 10;
        int y = screenHeight - 39;

        // --- DESENHO DA BARRA DE VIDA ---
        drawStatBar(xHP, y, 81, ActionbarParser.currentHP, ActionbarParser.maxHP, 0xFFFF5555);
        mc.fontRendererObj.drawStringWithShadow(ActionbarParser.currentHP + "/" + ActionbarParser.maxHP, xHP + 2, y + 1, 0xFFFFFF);

        // --- DESENHO DA BARRA DE MANA ---
        drawStatBar(xMana, y, 81, ActionbarParser.currentMana, ActionbarParser.maxMana, 0xFF5555FF);
        mc.fontRendererObj.drawStringWithShadow(ActionbarParser.currentMana + "/" + ActionbarParser.maxMana, xMana + 2, y + 1, 0xFFFFFF);
    }

    private void drawStatBar(int x, int y, int width, int current, int max, int color) {
        // Fundo cinza da barra
        Gui.drawRect(x, y, x + width, y + 9, 0x90000000);

        // Progresso da barra
        float percentage = (float) current / max;
        int barWidth = (int) (width * percentage);
        Gui.drawRect(x, y, x + barWidth, y + 9, color);
    }
}