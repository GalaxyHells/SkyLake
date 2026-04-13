package com.galaxyhells.skylake.utils;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderUtils2 {
    private static final float ALPHA_DEFAULT = 0.4f;
    private static final float LINE_WIDTH = 2.0f;
    
    public static void drawCustomBox(double x, double y, double z, float width, float height, int color) {
        drawCustomBox(x, y, z, width, height, color, ALPHA_DEFAULT);
    }
    
    public static void drawCustomBox(double x, double y, double z, float width, float height, int color, float alpha) {
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
        GL11.glLineWidth(LINE_WIDTH);

        // Define a cor (RGBA) com alpha customizável
        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GL11.glColor4f(r, g, b, Math.min(a, alpha));

        // Desenha a caixa
        AxisAlignedBB box = new AxisAlignedBB(-width/2, 0, -width/2, width/2, height, width/2);
        net.minecraft.client.renderer.RenderGlobal.drawSelectionBoundingBox(box);

        GL11.glLineWidth(1.0f); // Restaura largura padrão
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
    
    /**
     * Desenha uma linha entre dois pontos 3D
     */
    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, int color, float alpha) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(LINE_WIDTH);
        
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
