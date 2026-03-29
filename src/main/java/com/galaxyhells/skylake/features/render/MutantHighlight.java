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

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        // Verifica se a feature está ativa na config
        if (!com.galaxyhells.skylake.config.ConfigHandler.mutantHighlight) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return;

        // Percorre as entidades carregadas para achar o Mutante
        for (Entity entity : mc.theWorld.loadedEntityList) {

            // 1. Procuramos pela ArmorStand com o nome correto
            if (entity instanceof EntityArmorStand) {
                String name = entity.getCustomNameTag();

                if (name != null && name.contains("Enderman Mutante")) {

                    // 2. Quando achamos a NameTag, desenhamos o destaque
                    // O Mutante geralmente está logo abaixo ou na mesma posição da ArmorStand
                    renderHighlight(entity, event.partialTicks);
                }
            }
        }
    }

    private void renderHighlight(Entity nameTag, float partialTicks) {
        // Interpolação de movimento para o box não "tremer" enquanto o mob anda
        double x = nameTag.lastTickPosX + (nameTag.posX - nameTag.lastTickPosX) * partialTicks;
        double y = nameTag.lastTickPosY + (nameTag.posY - nameTag.lastTickPosY) * partialTicks;
        double z = nameTag.lastTickPosZ + (nameTag.posZ - nameTag.lastTickPosZ) * partialTicks;

        // O Enderman Mutante é alto, então fazemos um box maior (ex: 3 blocos de altura)
        // Ajustamos o Y para descer um pouco, já que a NameTag fica acima da cabeça
        float boxHeight = 3.0f;
        float boxWidth = 1.0f;

        // Renderiza um box roxo neon (0xFFFF00FF)
        RenderUtils.drawCustomBox(
                x,
                y - 2.5, // Ajuste para o box envolver o corpo do Enderman e não só a tag
                z,
                boxWidth,
                boxHeight,
                0xFFFF00FF
        );
    }
}