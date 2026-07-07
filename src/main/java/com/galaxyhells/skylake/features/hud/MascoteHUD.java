package com.galaxyhells.skylake.features.hud;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.OptionType;
import com.galaxyhells.skylake.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;

public class MascoteHUD {

    private static final int HUD_X_OFFSET = 10;
    private static final int HUD_Y_OFFSET = 60;
    private static final int LINE_SPACING = 12;
    private static final int BACKGROUND_PADDING = 4;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        // CORREÇÃO MANTIDA: ElementType.TEXT evita bugs visuais no evento
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MASCOTE_HUD))) {
            return;
        }

        if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            return;
        }

        renderMascoteOverlay();
    }

    private void renderMascoteOverlay() {
        if (!WorldUtils.hasMascoteData()) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);

        // Agora pegamos apenas Nível+Nome (linha 1) e Experiência (linha 2)
        String mascotLevel = WorldUtils.getMascoteLevel();
        String mascotExp = WorldUtils.getMascoteExp();

        // Calcula as dimensões
        int levelWidth = mc.fontRendererObj.getStringWidth(mascotLevel);
        int expWidth = mc.fontRendererObj.getStringWidth(mascotExp);

        int maxWidth = Math.max(levelWidth, expWidth);
        // Calcula a altura exata para apenas 2 linhas
        int totalHeight = (LINE_SPACING * 2) + (BACKGROUND_PADDING * 2);

        int x = HUD_X_OFFSET;
        int y = HUD_Y_OFFSET;

        // Desenha o fundo
        //drawBackground(x, y, maxWidth + (BACKGROUND_PADDING * 2), totalHeight - 2);

        // CORREÇÃO CRÍTICA DO OPENGL MANTIDA: Reseta para Branco para os textos não ficarem pretos
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();

        int textX = x;// + BACKGROUND_PADDING;
        int textY = y + BACKGROUND_PADDING;

        // Desenha a linha 1: [Nvl 12] Nome do Mascote (Com cor!)
        mc.fontRendererObj.drawStringWithShadow(mascotLevel, textX, textY, 0xFFFFFF);
        textY += LINE_SPACING;

        // Desenha a linha 2: Experiência (Com cor!)
        mc.fontRendererObj.drawStringWithShadow(mascotExp, textX, textY, 0xFFFFFF);

        GlStateManager.disableBlend();
    }

    private void drawBackground(int x, int y, int width, int height) {
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 144).getRGB());

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();

        // CORREÇÃO MANTIDA: Usando tryBlendFuncSeparate padrão da 1.8.9
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        int borderColor = new Color(20, 20, 20, 255).getRGB();
        int borderThickness = 1;

        Gui.drawRect(x, y, x + width, y + borderThickness, borderColor);
        Gui.drawRect(x, y + height - borderThickness, x + width, y + height, borderColor);
        Gui.drawRect(x, y, x + borderThickness, y + height, borderColor);
        Gui.drawRect(x + width - borderThickness, y, x + width, y + height, borderColor);

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
}