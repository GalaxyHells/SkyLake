package com.galaxyhells.skylake.features.render;

import com.galaxyhells.skylake.data.TreasureLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TreasureRadar {

    private int scanTicks = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().theWorld == null) return;

        scanTicks++;
        // Escaneia a cada 20 ticks (1 segundo) para não pesar o PC
        if (scanTicks % 20 == 0) {
            scan();
        }
    }

    private void scan() {
        Minecraft mc = Minecraft.getMinecraft();

        // Percorre todas as entidades carregadas ao redor do player
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityArmorStand) {
                String name = entity.getCustomNameTag();
                if (name == null) continue;

                BlockPos pos = entity.getPosition();

                // 1. Detecta Baús de Tesouro (Piratas)
                if (name.contains("TREASURE_CHEST")) {
                    //TreasureLogger.log(pos, "skylake_treasures.json");
                    //TreasureLogger.log(pos, "tesouros");
                }

                // 2. Detecta Presentes do Canárion
                // Nota: Verifique se o nome interno é "CANARION" ou similar no servidor
                else if (name.contains("GIFT_CHEST")) {
                    TreasureLogger.log(pos, "skylake_canarion.json");
                }
            }
        }
    }
}