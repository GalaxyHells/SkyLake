package com.galaxyhells.skylake.features.hud;

import com.galaxyhells.skylake.config.ConfigHandler;
import com.galaxyhells.skylake.utils.ActionbarParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StatOverlay {

    int HP_BAR_HEIGHT = 12;
    int MANA_BAR_HEIGHT = 7;

    int BORDER_COLOR = 0x90000000;
    int BACKGROUND_COLOR = 0x73000000;
    int CONTENT_COLOR = 0xB0000000;

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
                event.type == RenderGameOverlayEvent.ElementType.FOOD ||
                event.type == RenderGameOverlayEvent.ElementType.ARMOR) {
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

        if (!ConfigHandler.fancyHUD) {
            // Posições base (em cima de onde seriam os corações e fome)
            int xHP = screenWidth / 2 - 91;
            int xMana = screenWidth / 2 + 10;
            int y = screenHeight - 41; //39

            // --- DESENHO DA BARRA DE VIDA ---
            drawStatBar(xHP, y, 81, ActionbarParser.currentHP, ActionbarParser.maxHP, 0xFFFF5555, HP_BAR_HEIGHT);
            mc.fontRendererObj.drawStringWithShadow(ActionbarParser.currentHP + "/" + ActionbarParser.maxHP, xHP + 2, y + 1, 0xFFFFFF);

            // --- DESENHO DA BARRA DE MANA ---
            drawStatBar(xMana, y, 81, ActionbarParser.currentMana, ActionbarParser.maxMana, 0xFF0066FF, MANA_BAR_HEIGHT);
            mc.fontRendererObj.drawStringWithShadow(ActionbarParser.currentMana + "/" + ActionbarParser.maxMana, xMana + 2, y + 1, 0xFFFFFF);
        } else {
            int barWidth = 180;
            int xBar = screenWidth / 2 - (barWidth / 2);
            int yHP = screenHeight - 30;//41; //39
            int yMana = yHP + HP_BAR_HEIGHT + 1; //39

            // --- DESENHO DA BARRA DE VIDA ---
            //drawStatBar(xHP, y, (81 * 2 + xMana - xHP), ActionbarParser.currentHP, ActionbarParser.maxHP, 0xFFFF5555);
            //drawStatBar(xBar, yHP, barWidth, ActionbarParser.currentHP, ActionbarParser.maxHP, CONTENT_COLOR, HP_BAR_HEIGHT); // 0xFFAAAAAA
            //mc.fontRendererObj.drawStringWithShadow(ActionbarParser.currentHP + "/" + ActionbarParser.maxHP, xBar + 2, yHP + 2, 0xFFFFFF);

            // --- DESENHO DA BARRA DE MANA ---0xFF0066FF
            //drawStatBar(xBar, yMana, barWidth, ActionbarParser.currentMana, ActionbarParser.maxMana, CONTENT_COLOR, MANA_BAR_HEIGHT);
            //mc.fontRendererObj.drawStringWithShadow(ActionbarParser.currentMana + "/" + ActionbarParser.maxMana, xBar + 2, yMana + 0, 0xFFFFFF);
        }
    }

    private void drawStatBar(int x, int y, int width, int current, int max, int color, int height) {
//        // --- DESENHO DA BORDA CHANFRADA (Quinas para dentro) ---
//        // Borda Superior (Recuada 1px nas pontas)
//        Gui.drawRect(x, y - 1, x + width, y, 0xFF000000);
//        // Borda Inferior (Recuada 1px nas pontas)
//        Gui.drawRect(x, y + 9, x + width, y + 10, 0xFF000000);
//        // Borda Esquerda (Recuada 1px nas pontas)
//        Gui.drawRect(x - 1, y, x, y + 9, 0xFF000000);
//        // Borda Direita (Recuada 1px nas pontas)
//        Gui.drawRect(x + width, y, x + width + 1, y + 9, 0xFF000000);

        int borderTickness = 1;
        //int borderColor = ; //0xFF000000//

        // Borda Superior (Recuada 1px nas pontas)
        Gui.drawRect(x, y - borderTickness, x + width, y, BORDER_COLOR);
        // Borda Inferior (Recuada 1px nas pontas)
        Gui.drawRect(x, y + height - 1, x + width, y + height + borderTickness, BORDER_COLOR);
        // Borda Esquerda (Recuada 1px nas pontas)
        Gui.drawRect(x - borderTickness, y, x, y + height, BORDER_COLOR);
        // Borda Direita (Recuada 1px nas pontas)
        Gui.drawRect(x + width, y, x + width + borderTickness, y + height, BORDER_COLOR);

        // --- CONTEÚDO DA BARRA ---
        // Fundo cinza translúcido
        Gui.drawRect(x, y, x + width, y + height, BACKGROUND_COLOR);

        // Progresso da barra (Vida ou Mana)
        if (max > 0) {
            float percentage = Math.min(1.0F, (float) current / max);
            int barWidth = (int) (width * percentage);
            Gui.drawRect(x, y, x + barWidth, y + height, color); //9
        }
    }
}