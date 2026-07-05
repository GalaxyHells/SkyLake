package com.galaxyhells.skylake.utils;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderUtils2 {
    private static final float ALPHA_DEFAULT = 0.4f;
    private static final float LINE_WIDTH_DEFAULT = 2.0f; // Transformado em variável padrão

    // Sobrecarga 1: Apenas Cor (Usa Alpha e LineWidth padrões)
    public static void drawCustomBox(double x, double y, double z, float width, float height, int color) {
        drawCustomBox(x, y, z, width, height, color, ALPHA_DEFAULT, LINE_WIDTH_DEFAULT);
    }

    // Sobrecarga 2: Cor + Alpha (Usa LineWidth padrão)
    public static void drawCustomBox(double x, double y, double z, float width, float height, int color, float alpha) {
        drawCustomBox(x, y, z, width, height, color, alpha, LINE_WIDTH_DEFAULT);
    }

    // Sobrecarga 3: Completa (Cor + Alpha + Espessura da Linha)
    public static void drawCustomBox(double x, double y, double z, float width, float height, int color, float alpha, float lineWidth) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        double renderX = x - rm.viewerPosX;
        double renderY = y - rm.viewerPosY;
        double renderZ = z - rm.viewerPosZ;

        GL11.glPushMatrix();
        GL11.glTranslated(renderX, renderY, renderZ);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // 1. Aplica a espessura customizada informada no parâmetro
        GL11.glLineWidth(lineWidth);

        // Define a cor (RGBA) com alpha customizável
        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GL11.glColor4f(r, g, b, Math.min(a, alpha));

        // Desenha a caixa
        AxisAlignedBB box = new AxisAlignedBB(-width/2, 0, -width/2, width/2, height, width/2);
        net.minecraft.client.renderer.RenderGlobal.drawSelectionBoundingBox(box);

        // 2. É crucial redefinir a espessura para 1.0F para não distorcer as hitboxes nativas do jogo
        GL11.glLineWidth(1.0f);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    // Sobrecarga para Linha 1: Cor + Alpha
    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, int color, float alpha) {
        drawLine(x1, y1, z1, x2, y2, z2, color, alpha, LINE_WIDTH_DEFAULT);
    }

    // Sobrecarga para Linha 2: Completa (Cor + Alpha + Espessura da Linha)
    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, int color, float alpha, float lineWidth) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Aplica a espessura
        GL11.glLineWidth(lineWidth);

        // Define a cor
        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GL11.glColor4f(r, g, b, Math.min(a, alpha));

        // Desenha a linha
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(x1 - rm.viewerPosX, y1 - rm.viewerPosY, z1 - rm.viewerPosZ);
        GL11.glVertex3d(x2 - rm.viewerPosX, y2 - rm.viewerPosY, z2 - rm.viewerPosZ);
        GL11.glEnd();

        // Restaura a espessura
        GL11.glLineWidth(1.0f);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    /**
     * Desenha um texto 3D no mundo
     */
    public static void drawText3D(String text, double x, double y, double z, int color) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();

        GL11.glPushMatrix();
        GL11.glTranslated(x - rm.viewerPosX, y - rm.viewerPosY, z - rm.viewerPosZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-rm.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(rm.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-0.025F, -0.025F, 0.025F);

        // Desativa profundidade para texto sempre visível
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Desenha o texto
        Minecraft.getMinecraft().fontRendererObj.drawString(text, 0, 0, color);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}