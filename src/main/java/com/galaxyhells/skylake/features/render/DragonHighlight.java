package com.galaxyhells.skylake.features.render;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.RenderUtils2;
import com.galaxyhells.skylake.utils.OptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DragonHighlight {

    private static final float HIGHLIGHT_HEIGHT = 8.0f;
    private static final float HIGHLIGHT_WIDTH = 5.0f;
    private static final float HIGHLIGHT_Y_OFFSET = 2.0f;
    private static final int HIGHLIGHT_COLOR = 0xFFFFAA00;
    
    private Entity currentDragon = null;
    private long lastCheckTime = 0;
    private static final long CHECK_INTERVAL = 100;
    
    private static DragonHighlight instance;
    
    public DragonHighlight() {
        instance = this;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.DRAGON_HIGHLIGHT))) {
            currentDragon = null;
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return;

        // Atualiza busca a cada 100ms
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckTime >= CHECK_INTERVAL) {
            lastCheckTime = currentTime;
            updateDragonLocation(mc);
        }
        
        // Renderiza a cada frame (não pisca!)
        if (currentDragon != null && !currentDragon.isDead) {
            renderHighlight(currentDragon, event.partialTicks);
        }
    }
    
    private void updateDragonLocation(Minecraft mc) {
        currentDragon = null;
        
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityDragon) {
                currentDragon = entity;
                return;
            }
        }
    }

    private void renderHighlight(Entity entity, float partialTicks) {
        // Interpolação de movimento para o box não "tremer" enquanto o mob anda
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;

        // O Dragão é grande, então fazemos um box maior
        // Ajustamos o Y para centralizar melhor no corpo do dragão
        
        // Renderiza o destaque do dragão
        RenderUtils2.drawCustomBox(
                x,
                y - HIGHLIGHT_Y_OFFSET,
                z,
                HIGHLIGHT_WIDTH,
                HIGHLIGHT_HEIGHT,
                HIGHLIGHT_COLOR,
                1.0f,
                4.0f
        );
    }
    
    /**
     * Força uma verificação imediata do dragão.
     * Chamado quando detectado spawn no chat para highlight instantâneo.
     */
    public static void forceCheck() {
        if (instance != null) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.theWorld != null) {
                instance.updateDragonLocation(mc);
                instance.lastCheckTime = System.currentTimeMillis();
            }
        }
    }
}
