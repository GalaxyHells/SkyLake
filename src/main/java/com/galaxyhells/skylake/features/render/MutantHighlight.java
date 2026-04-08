package com.galaxyhells.skylake.features.render;

import com.galaxyhells.skylake.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MutantHighlight {

    private static final String MUTANT_NAME_TAG = "Mutante";
    private static final String MUTANT_NAME_ALT = "MUTANTE";
    private static final float HIGHLIGHT_HEIGHT = 3.0f;
    private static final float HIGHLIGHT_WIDTH = 1.0f;
    private static final float HIGHLIGHT_Y_OFFSET = 0.0f;
    private static final int HIGHLIGHT_COLOR = 0xFFFF00FF;
    
    private Entity currentMutant = null;
    private long lastCheckTime = 0;
    private static final long CHECK_INTERVAL = 100;

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!com.galaxyhells.skylake.config.ConfigHandler.mutantHighlight) {
            currentMutant = null;
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return;

        // Atualiza busca a cada 100ms
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckTime >= CHECK_INTERVAL) {
            lastCheckTime = currentTime;
            updateMutantLocation(mc);
        }
        
        // Renderiza a cada frame (não pisca!)
        if (currentMutant != null && !currentMutant.isDead) {
            renderHighlight(currentMutant, event.partialTicks);
        }
    }
    
    private void updateMutantLocation(Minecraft mc) {
        currentMutant = null;
        
        for (Entity entity : mc.theWorld.loadedEntityList) {
            String name = entity.getCustomNameTag();
            if (name == null) continue;
            
            boolean isMutant = name.contains(MUTANT_NAME_TAG) || name.contains(MUTANT_NAME_ALT);
            
            if (isMutant && (entity instanceof EntityArmorStand || entity instanceof EntityEnderman)) {
                currentMutant = entity;
                return;
            }
        }
    }

    private void renderHighlight(Entity nameTag, float partialTicks) {
        // Interpolação de movimento para o box não "tremer" enquanto o mob anda
        double x = nameTag.lastTickPosX + (nameTag.posX - nameTag.lastTickPosX) * partialTicks;
        double y = nameTag.lastTickPosY + (nameTag.posY - nameTag.lastTickPosY) * partialTicks;
        double z = nameTag.lastTickPosZ + (nameTag.posZ - nameTag.lastTickPosZ) * partialTicks;

        // O Enderman Mutante é alto, então fazemos um box maior
        // Ajustamos o Y para descer um pouco, já que a NameTag fica acima da cabeça
        
        // Renderiza o destaque do mutante
        RenderUtils.drawCustomBox(
                x,
                y - HIGHLIGHT_Y_OFFSET,
                z,
                HIGHLIGHT_WIDTH,
                HIGHLIGHT_HEIGHT,
                HIGHLIGHT_COLOR
        );
    }
}
