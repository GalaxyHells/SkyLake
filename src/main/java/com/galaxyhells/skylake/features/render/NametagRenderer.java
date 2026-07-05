package com.galaxyhells.skylake.features.render;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.OptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NametagRenderer {

    public static final NametagRenderer INSTANCE = new NametagRenderer();

    // Regex seguro que ignora o coração e pega vírgulas
    private static final Pattern SKYBLOCK_NAMETAG_PATTERN = Pattern.compile("\\[Lv ?(\\d+)\\] (.*?) ([\\d,]+)/([\\d,]+)");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderNametag(RenderLivingEvent.Specials.Pre event) {

        if (Boolean.FALSE.equals(SkyLake.optionsService.get(OptionType.CUSTOM_NAMETAGS))) {
            return;
        }

        if (event.entity instanceof EntityLiving || event.entity instanceof EntityArmorStand) {

            String rawName = StringUtils.stripControlCodes(event.entity.getDisplayName().getUnformattedText());
            Matcher matcher = SKYBLOCK_NAMETAG_PATTERN.matcher(rawName);

            if (matcher.find()) {
                String level = matcher.group(1);
                String name = matcher.group(2);

                float currentHealth = Float.parseFloat(matcher.group(3).replace(",", ""));
                float maxHealth = Float.parseFloat(matcher.group(4).replace(",", ""));

                drawCustomNametag(event, level, name, currentHealth, maxHealth);
            }

            event.setCanceled(true);
        }
    }

    private void drawCustomNametag(RenderLivingEvent.Specials.Pre event, String level, String name, float currentHealth, float maxHealth) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        FontRenderer font = renderManager.getFontRenderer();

        float healthPercent = maxHealth > 0 ? Math.min(currentHealth / maxHealth, 1.0f) : 0f;

        // Usando o FontRenderer nativo para garantir que a largura nunca seja 0
        String textForWidth = "[Lv " + level + "] " + name + " " + (int)currentHealth + "/" + (int)maxHealth;
        int barWidth = font.getStringWidth(textForWidth) + 10; // +10 para margem
        if (barWidth < 50) barWidth = 50; // Largura mínima de segurança

        int halfWidth = barWidth / 2;
        int barHeight = 8;

        double x = event.x;
        double y = event.y;
        double z = event.z;

        if (event.entity instanceof EntityArmorStand) {
            y += 0.5D;
        } else {
            y += event.entity.height + 0.5D;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        float scale = 0.02666667F;
        GlStateManager.scale(-scale, -scale, scale);

        // =========================================================
        // 1. PREPARAÇÃO DO OPENGL (O SEGREDO PARA A COR NÃO FICAR PRETA)
        // =========================================================
        GlStateManager.disableLighting(); // Tira a sombra do mundo
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();    // Faz aparecer na frente do mob
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        // CRÍTICO: Desliga as texturas para os retângulos poderem ser coloridos livremente!
        GlStateManager.disableTexture2D();

        // =========================================================
        // 2. DEFINIÇÃO DAS CORES
        // =========================================================
        int corFundoNome = 0x60000000;  // Preto Transparente
        int corVerdeVida = 0xFF55FF55;  // Verde Vivo
        int corCinzaFundo = 0xFF404040; // Cinza Escuro

        // =========================================================
        // 3. DESENHANDO AS BARRAS
        // =========================================================
        // Background Principal (Fundo do nome)
        Gui.drawRect(-halfWidth - 2, -20, halfWidth + 2, -10, corFundoNome);

        int fillWidth = (int)(barWidth * healthPercent);

        // Barra Verde (Apenas o espaço preenchido)
        if (fillWidth > 0) {
            Gui.drawRect(-halfWidth, -6, -halfWidth + fillWidth, -6 + barHeight, corVerdeVida);
        }

        // Barra Cinza (Apenas o espaço vazio restante, começando de onde a verde terminou)
        if (fillWidth < barWidth) {
            Gui.drawRect(-halfWidth + fillWidth, -6, halfWidth, -6 + barHeight, corCinzaFundo);
        }

        // =========================================================
        // 4. DESENHANDO OS TEXTOS
        // =========================================================
        // CRÍTICO: Religa a textura. Se não fizer isso, o texto vira blocos sólidos.
        GlStateManager.enableTexture2D();

        // Força a cor base para branco limpo antes de desenhar o texto (segurança)
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        String topText = "§8[§eLv " + level + "§8] §f" + name + " §e" + (int)currentHealth + "§f/§a" + (int)maxHealth + "§c❤";
        font.drawString(topText, -font.getStringWidth(topText) / 2, -18, 0xFFFFFF, true);

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.8, 0.8, 0.8);

        String pctText = (int)(healthPercent * 100) + "%";
        font.drawString(pctText, -font.getStringWidth(pctText) / 2, -6, 0xFFFFFF, true);

        GlStateManager.popMatrix();

        // =========================================================
        // 5. RESTAURAÇÃO DE ESTADOS PARA NÃO QUEBRAR O RESTO DO JOGO
        // =========================================================
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}