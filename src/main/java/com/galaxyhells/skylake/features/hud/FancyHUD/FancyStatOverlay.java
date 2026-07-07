package com.galaxyhells.skylake.features.hud.FancyHUD;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.ActionbarParser;
import com.galaxyhells.skylake.utils.NotificationManager;
import com.galaxyhells.skylake.utils.OptionType;
import com.galaxyhells.skylake.utils.PerformanceUtils;
import com.galaxyhells.skylake.utils.RenderOptimizer;
import com.galaxyhells.skylake.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class FancyStatOverlay {
    
    private static final int HUD_X_OFFSET = 10;
    private static final int HUD_Y_OFFSET = 10;
    private static final int HEAD_SIZE = 40;
    private static final int BAR_WIDTH = 100;
    private static final int BAR_HEIGHT = 10;
    private static final int BAR_SPACING = 2;
    private static final int CURRENCY_ICON_SIZE = 8;
    private static final Color fillColor = new Color(20, 20, 20, 255);
        
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.FANCY_STAT_OVERLAY))) return;
        
        if (PerformanceUtils.getMC().gameSettings.showDebugInfo) {
            return;
        }
        
        //if (PerformanceUtils.getMC().currentScreen != null) {
        //    return;
        //}
        
        // Renderizar sempre (HUD precisa ser contínuo)
        renderStatOverlay();
    }
    
    /**
     * Esconde a barra de experiência padrão do Minecraft quando a FancyStatOverlay está ativa
     */
    @SubscribeEvent
    public void hideVanillaExperienceBar(RenderGameOverlayEvent.Pre event) {
        // Verificar se é o evento da barra de experiência
        if (event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }
        
        // Verificar se a FancyStatOverlay está ativa
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.FANCY_STAT_OVERLAY))) {
            return;
        }
        
        // Não esconder se o debug info estiver ativo
        if (PerformanceUtils.getMC().gameSettings.showDebugInfo) {
            return;
        }
        
        // Cancelar a renderização da barra de experiência vanilla
        event.setCanceled(true);
    }
    
    private void renderStatOverlay() {
        if (!PerformanceUtils.isValidPlayer()) {
            return;
        }
        
        ScaledResolution sr = PerformanceUtils.getScaledResolution();
        int baseX = HUD_X_OFFSET;
        int baseY = HUD_Y_OFFSET;
        
        // Renderizar cabeça do jogador
        renderPlayerHead(baseX, baseY);
        
        // Renderizar nível do jogador
        renderPlayerLevel(baseX, baseY);
        
        // Renderizar barras de status
        int barsX = baseX + HEAD_SIZE + 4;
        renderHealthBar(barsX, baseY);
        renderManaBar(barsX, baseY + BAR_HEIGHT + BAR_SPACING);
        renderExperienceBar(barsX, baseY + (BAR_HEIGHT + BAR_SPACING) * 2);
        
        // Renderizar moedas
        int currencyX = baseX + HEAD_SIZE + 5;
        int currencyY = baseY + HEAD_SIZE - 5;
        renderRegularCurrency(currencyX, currencyY);
        renderPremiumCurrency(currencyY + 77, currencyY);
    }

    private void renderPlayerHead(int x, int y) {
        if (PerformanceUtils.getMC().thePlayer == null) {
            return;
        }

        drawRect(x + 1, y + 1, HEAD_SIZE - 2, HEAD_SIZE - 2, new Color(20, 20, 20, 144));

        // Renderizar a cabeça do jogador com estados OpenGL corretos
        GlStateManager.pushMatrix();

        // Limpar estados anteriores que podem causar problemas de cor
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        try {
            // Usar a skin do jogador
            ResourceLocation skinLocation = PerformanceUtils.getMC().thePlayer.getLocationSkin();
            PerformanceUtils.getMC().getTextureManager().bindTexture(skinLocation);

            // Desenhar a base da cabeça (Coordenadas UV: 8, 8) fatiada
            drawRoundedHeadLayer(x, y, 8, 8);

            // Desenhar o chapéu/camada extra (Coordenadas UV: 40, 8) fatiado
            drawRoundedHeadLayer(x, y, 40, 8);

        } catch (Exception e) {
            // Em caso de erro, desenhar um quadrado padrão com os cantos cortados
            GlStateManager.disableTexture2D();

            // Simulação do corte no fallback (opcional, mas mantém a consistência visual)
            int scale = HEAD_SIZE / 8;
            Color fallbackColor = new Color(100, 100, 100, 200);
            Gui.drawRect(x + scale, y, x + HEAD_SIZE - scale, y + scale, fallbackColor.getRGB()); // Topo
            Gui.drawRect(x, y + scale, x + HEAD_SIZE, y + HEAD_SIZE - scale, fallbackColor.getRGB()); // Miolo
            Gui.drawRect(x + scale, y + HEAD_SIZE - scale, x + HEAD_SIZE - scale, y + HEAD_SIZE, fallbackColor.getRGB()); // Base

            GlStateManager.enableTexture2D();
        }

        GlStateManager.popMatrix();
        GlStateManager.disableBlend();

        // Desenhar borda separadamente
        renderHeadBorder(x, y);
    }

    /**
     * Desenha uma camada da cabeça 8x8 fatiada em 3 partes para omitir os 4 cantos.
     */
    private void drawRoundedHeadLayer(int x, int y, int startU, int startV) {
        // Calcula o tamanho proporcional de 1 pixel da skin escalado para a HUD
        int scale = HEAD_SIZE / 8;

        // 1. Linha do topo (Altura de 1 pixel da skin, largura de 6 pixels)
        Gui.drawScaledCustomSizeModalRect(
                x + scale, y,
                startU + 1, startV,
                6, 1,
                HEAD_SIZE - (scale * 2), scale,
                64, 64
        );

        // 2. Miolo (Altura de 6 pixels da skin, largura total de 8 pixels)
        Gui.drawScaledCustomSizeModalRect(
                x, y + scale,
                startU, startV + 1,
                8, 6,
                HEAD_SIZE, HEAD_SIZE - (scale * 2),
                64, 64
        );

        // 3. Linha da base (Altura de 1 pixel da skin, largura de 6 pixels)
        Gui.drawScaledCustomSizeModalRect(
                x + scale, y + HEAD_SIZE - scale,
                startU + 1, startV + 7,
                6, 1,
                HEAD_SIZE - (scale * 2), scale,
                64, 64
        );
    }
    
    private void renderHeadBorder(int x, int y) {
        // Desenhar borda muito sutil e clara
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        
        // Cor muito clara e sutil para a borda
        Color borderColor = new Color(20, 20, 20, 255);
        GlStateManager.color(borderColor.getRed() / 255.0f, borderColor.getGreen() / 255.0f, 
                           borderColor.getBlue() / 255.0f, borderColor.getAlpha() / 255.0f);
        
        // Desenhar borda com 1 pixel de espessura
        // Borda superior
        Gui.drawRect(x + 1, y, x + HEAD_SIZE - 1, y + 1, borderColor.getRGB());
        // Borda inferior
        Gui.drawRect(x + 1, y + HEAD_SIZE - 1, x + HEAD_SIZE - 1, y + HEAD_SIZE, borderColor.getRGB());
        // Borda esquerda
        Gui.drawRect(x, y + 1, x + 1, y + HEAD_SIZE - 1, borderColor.getRGB());
        // Borda direita
        Gui.drawRect(x + HEAD_SIZE - 1, y + 1, x + HEAD_SIZE, y + HEAD_SIZE - 1, borderColor.getRGB());
        
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
    
    private void renderPlayerLevel(int x, int y) {
        if (PerformanceUtils.getMC().thePlayer == null) {
            return;
        }
        
        int level = PerformanceUtils.getMC().thePlayer.experienceLevel;
        String levelText = String.valueOf(level);
        
        // Dimensões do quadrado do nível
        int levelBoxSizeX = 12;
        int levelBoxSizeY = 8;
        int levelBoxX = x + HEAD_SIZE - levelBoxSizeX - 1;
        int levelBoxY = y + HEAD_SIZE - levelBoxSizeY - 1;
        
        // Desenhar quadrado preto sem borda para o nível
        drawRect(levelBoxX, levelBoxY, levelBoxSizeX, levelBoxSizeY, new Color(0, 0, 0, 200));
        
        // Habilitar texturas para texto
        GlStateManager.enableTexture2D();
        
        // Aplicar escala para o nível
        float scale = 0.7f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        
        // Posição do texto (centralizado no quadrado)
        int textWidth = PerformanceUtils.getMC().fontRendererObj.getStringWidth(levelText);
        int textX = (int)((levelBoxX + (levelBoxSizeX - textWidth) / 2) / scale) + 4;
        int textY = (int)((levelBoxY + (levelBoxSizeY - PerformanceUtils.getMC().fontRendererObj.FONT_HEIGHT + 1) / 2) / scale) + 3;
        
        // Cor do nível (branco)
        PerformanceUtils.getMC().fontRendererObj.drawString(levelText, textX, textY, 0xFFFFFFFF);
        
        // Restaurar escala
        GlStateManager.popMatrix();
        GlStateManager.disableTexture2D();
    }

    private void renderHealthBar(int x, int y) {
        if (PerformanceUtils.getMC().thePlayer == null) {
            return;
        }

        float health = ActionbarParser.currentHP; //mc.thePlayer.getHealth();
        float maxHealth = ActionbarParser.maxHP; //mc.thePlayer.getMaxHealth();
        float healthPercentage = Math.min(health / maxHealth, 1.0f);

        // Fundo da barra
        drawRectWithBorder(x, y, BAR_WIDTH, BAR_HEIGHT,
                new Color(0, 0, 0, 144), new Color(20, 20, 20, 255), 1);

        // Barra de vida
        int filledWidth = (int)(BAR_WIDTH * healthPercentage);
        Color healthColor = new Color(172, 53, 55, 255);
        drawRect(x + 1, y + 1, filledWidth - 2, BAR_HEIGHT - 2, healthColor);

        // Segmento amarelo (estilo visual)
        if (filledWidth > 15) {
            // Desativado temporariamente
        }

        // Texto HP
        String healthText = String.format("%.0f/%.0f", health, maxHealth);

        // 1. Calcula dinamicamente a largura do texto na tela em pixels
        int textWidth = PerformanceUtils.getMC().fontRendererObj.getStringWidth(healthText);

        // 2. Calcula a nova posição X:
        // Começa no fim da barra (x + BAR_WIDTH), volta o tamanho do texto (textWidth)
        // e subtrai um pequeno valor (ex: 3 pixels) para deixar uma margem/padding elegante da borda externa.
        int textX = x + BAR_WIDTH - textWidth - 3;

        // 3. Renderiza o texto na posição X corrigida
        PerformanceUtils.getMC().fontRendererObj.drawStringWithShadow(healthText, textX + 1, y + 1, 0xFFFFFFFF);
    }
    
    private void renderManaBar(int x, int y) {
        if (PerformanceUtils.getMC().thePlayer == null) {
            return;
        }
        
        // Simulação de mana (não existe nativamente em Minecraft)
        float mana = ActionbarParser.currentMana;
        float maxMana = ActionbarParser.maxMana;
        float manaPercentage = mana / maxMana;
        
        // Desenhar fundo da barra com contorno escuro
        drawRectWithBorder(x, y, BAR_WIDTH, BAR_HEIGHT, 
                          new Color(0, 0, 0, 144), new Color(20, 20, 20, 255), 1);
        
        // Desenhar barra de mana (azul ciano como na imagem)
        int filledWidth = (int)(BAR_WIDTH * manaPercentage);
        Color manaColor = new Color(0, 255, 255, 255); // Azul ciano
        drawRect(x + 1, y + 1, filledWidth - 2, BAR_HEIGHT - 2, manaColor);
        
        // Texto Mana
        String manaText = String.format("%.0f/%.0f", mana, maxMana);

        // 1. Calcula dinamicamente a largura do texto na tela em pixels
        int textWidth = PerformanceUtils.getMC().fontRendererObj.getStringWidth(manaText);

        // 2. Calcula a nova posição X:
        // Começa no fim da barra (x + BAR_WIDTH), volta o tamanho do texto (textWidth)
        // e subtrai um pequeno valor (ex: 3 pixels) para deixar uma margem/padding elegante da borda externa.
        int textX = x + BAR_WIDTH - textWidth - 3;

        // 3. Renderiza o texto na posição X corrigida
        PerformanceUtils.getMC().fontRendererObj.drawStringWithShadow(manaText, textX + 1, y + 1, 0xFFFFFFFF);
    }
    
    private void renderExperienceBar(int x, int y) {
        if (PerformanceUtils.getMC().thePlayer == null) {
            return;
        }

        // Usar experiência vanilla do jogador
        float experience = PerformanceUtils.getMC().thePlayer.experience; // 0.0 a 1.0 (progresso para o próximo nível)
        int experiencePercentage = (int)(experience * 100);

        // Desenhar fundo da barra com contorno escuro
        drawRectWithBorder(x, y, BAR_WIDTH, BAR_HEIGHT,
                          new Color(0, 0, 0, 144), new Color(20, 20, 20, 255), 1);

        // Desenhar barra de experiência (verde como barra vanilla)
        int filledWidth = (int)(BAR_WIDTH * experience);
        Color expColor = new Color(85, 255, 85, 255); // Verde claro como barra de experiência vanilla
        drawRect(x + 1, y + 1, filledWidth - 2, BAR_HEIGHT - 2, expColor);

        // Texto EXP
        String expText = experiencePercentage + "%";

        // 1. Calcula dinamicamente a largura do texto na tela em pixels
        int textWidth = PerformanceUtils.getMC().fontRendererObj.getStringWidth(expText);

        // 2. Calcula a nova posição X:
        // Começa no fim da barra (x + BAR_WIDTH), volta o tamanho do texto (textWidth)
        // e subtrai um pequeno valor (ex: 3 pixels) para deixar uma margem/padding elegante da borda externa.
        int textX = x + BAR_WIDTH - textWidth - 3;

        // 3. Renderiza o texto na posição X corrigida
        PerformanceUtils.getMC().fontRendererObj.drawStringWithShadow(expText, textX + 1, y + 1, 0xFFFFFFFF);
    }

    private void renderRegularCurrency(int x, int y) {
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.HIDE_CURRENCY))) return;

        // Obter valor real de coins do scoreboard
        String currencyAmount = WorldUtils.getCoinsFromScoreboard();
        
        // Desenhar ícone de moeda dourada com contorno
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        
        // Círculo dourado com contorno escuro
        Color goldColor = new Color(255, 215, 0, 255); // Dourado brilhante
        Color goldBorder = new Color(180, 150, 0, 255); // Dourado escuro para borda
        drawCircleWithBorder(x, y + 2, CURRENCY_ICON_SIZE / 2, goldColor, goldBorder, 2);
        
        // Adicionar detalhe interno (círculo menor)
        Color innerGold = new Color(255, 235, 100, 255); // Dourado mais claro
        drawCircle(x + 3, y + 5, 3, innerGold);
        
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        
        // Desenhar valor da moeda
        PerformanceUtils.getMC().fontRendererObj.drawStringWithShadow(currencyAmount, x + CURRENCY_ICON_SIZE + 3, y + 2, 0xFFFFFFFF);
    }
    
    private void renderPremiumCurrency(int x, int y) {
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.HIDE_CURRENCY))) return;

        // Obter valor real de cubos do scoreboard
        String currencyAmount = WorldUtils.getCubosFromScoreboard();
        
        // Desenhar ícone de diamante azul com contorno
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        
        // Diamante azul com contorno escuro
        Color diamondColor = new Color(100, 200, 255, 255); // Azul claro
        Color diamondBorder = new Color(0, 100, 200, 255); // Azul escuro para borda
        drawDiamondWithBorder(x, y + 2, CURRENCY_ICON_SIZE / 2, diamondColor, diamondBorder, 2);
        
        // Adicionar brilho interno (losango menor)
        Color innerDiamond = new Color(200, 230, 255, 255); // Azul muito claro
        drawDiamond(x + 3, y + 5, 3, innerDiamond);
        
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        
        // Desenhar valor da moeda premium
        PerformanceUtils.getMC().fontRendererObj.drawStringWithShadow(currencyAmount, x + CURRENCY_ICON_SIZE + 3, y + 2, 0xFFFFFFFF);
    }
    
    private void drawRect(int x, int y, int width, int height, Color color) {
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, 
                           color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        Gui.drawRect(x, y, x + width, y + height, color.getRGB());
    }
    
    private void drawCircle(int x, int y, int radius, Color color) {
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, 
                           color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        
        // Desenhar círculo usando pontos
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2i(x + radius, y + radius);
        
        for (int i = 0; i <= 360; i += 10) {
            double angle = Math.toRadians(i);
            int px = (int)(x + radius + radius * Math.cos(angle));
            int py = (int)(y + radius + radius * Math.sin(angle));
            GL11.glVertex2i(px, py);
        }
        
        GL11.glEnd();
    }
    
    private void drawDiamond(int x, int y, int size, Color color) {
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, 
                           color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        
        // Desenhar diamante (losango)
        GL11.glBegin(GL11.GL_QUADS);
        
        // Topo
        GL11.glVertex2i(x + size, y);
        // Direita
        GL11.glVertex2i(x + size * 2, y + size);
        // Base
        GL11.glVertex2i(x + size, y + size * 2);
        // Esquerda
        GL11.glVertex2i(x, y + size);
        
        GL11.glEnd();
    }
    
    private void drawRectWithBorder(int x, int y, int width, int height, Color fillColor, Color borderColor, int borderWidth) {
        // Desenhar o preenchimento
        //drawRect(x, y, width, height, fillColor);
        drawRect(x + 1, y +1, width - 2, height - 1, fillColor);
        
        // Desenhar a borda
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.color(borderColor.getRed() / 255.0f, borderColor.getGreen() / 255.0f, 
                           borderColor.getBlue() / 255.0f, borderColor.getAlpha() / 255.0f);
        
        // Borda superior
        Gui.drawRect(x + 1, y, x + width - 1, y + borderWidth, borderColor.getRGB());
        // Borda inferior
        Gui.drawRect(x + 1, y + height - borderWidth, x + width - 1, y + height, borderColor.getRGB());
        // Borda esquerda
        Gui.drawRect(x, y + 1, x + borderWidth, y + height - 1, borderColor.getRGB());
        // Borda direita
        Gui.drawRect(x + width - borderWidth, y + 1, x + width, y + height - 1, borderColor.getRGB());

//        // Borda superior
//        Gui.drawRect(x, y, x + width, y + borderWidth, borderColor.getRGB());
//        // Borda inferior
//        Gui.drawRect(x, y + height - borderWidth, x + width, y + height, borderColor.getRGB());
//        // Borda esquerda
//        Gui.drawRect(x, y, x + borderWidth, y + height, borderColor.getRGB());
//        // Borda direita
//        Gui.drawRect(x + width - borderWidth, y, x + width, y + height, borderColor.getRGB());
        
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
    
    private void drawCircleWithBorder(int x, int y, int radius, Color fillColor, Color borderColor, int borderWidth) {
        // Desenhar círculo preenchido
        drawCircle(x, y, radius, fillColor);
        
        // Desenhar borda do círculo
        GlStateManager.color(borderColor.getRed() / 255.0f, borderColor.getGreen() / 255.0f, 
                           borderColor.getBlue() / 255.0f, borderColor.getAlpha() / 255.0f);
        
        GL11.glLineWidth(borderWidth);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        
        for (int i = 0; i <= 360; i += 5) {
            double angle = Math.toRadians(i);
            int px = (int)(x + radius + radius * Math.cos(angle));
            int py = (int)(y + radius + radius * Math.sin(angle));
            GL11.glVertex2i(px, py);
        }
        
        GL11.glEnd();
        GL11.glLineWidth(1.0f);
    }
    
    private void drawDiamondWithBorder(int x, int y, int size, Color fillColor, Color borderColor, int borderWidth) {
        // Desenhar diamante preenchido
        drawDiamond(x, y, size, fillColor);
        
        // Desenhar borda do diamante
        GlStateManager.color(borderColor.getRed() / 255.0f, borderColor.getGreen() / 255.0f, 
                           borderColor.getBlue() / 255.0f, borderColor.getAlpha() / 255.0f);
        
        GL11.glLineWidth(borderWidth);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        
        // Topo
        GL11.glVertex2i(x + size, y);
        // Direita
        GL11.glVertex2i(x + size * 2, y + size);
        // Base
        GL11.glVertex2i(x + size, y + size * 2);
        // Esquerda
        GL11.glVertex2i(x, y + size);
        
        GL11.glEnd();
        GL11.glLineWidth(1.0f);
    }
}
