package com.galaxyhells.skylake.utils;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
    public static void drawCustomBox(double x, double y, double z, float width, float height, int color) {
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

        // Define a cor (RGBA)
        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GL11.glColor4f(r, g, b, a);

        // Desenha a caixa
        AxisAlignedBB box = new AxisAlignedBB(-width/2, 0, -width/2, width/2, height, width/2);
        net.minecraft.client.renderer.RenderGlobal.drawSelectionBoundingBox(box);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}