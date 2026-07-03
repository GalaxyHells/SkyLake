package com.galaxyhells.skylake.features.render;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.data.MutantSpawnDataManager;
import com.galaxyhells.skylake.utils.RenderUtils2;
import com.galaxyhells.skylake.utils.OptionType;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class MutantSpawnBoxes {

    public static final MutantSpawnBoxes INSTANCE = new MutantSpawnBoxes();
    
    private volatile boolean shouldRender = true;

    public void setEnabled(boolean enabled) {
        this.shouldRender = enabled;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        // Verifica se a feature está ativa na config
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MUTANT_SPAWN_BOXES))) return;
        
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // Renderiza caixas de 3x3x1 em cada local de spawn
        List<BlockPos> spawnLocations = MutantSpawnDataManager.getSpawnLocations();
        for (BlockPos pos : spawnLocations) {
            double x = pos.getX() + 0.5;
            double y = pos.getY();
            double z = pos.getZ() + 0.5;
            
            // Desenha caixa de 3x3x1 (largura=3, altura=1)
            // Cor verde translúcido para distinguir dos outros waypoints
            // Centraliza a caixa como no TreasureWaypoint
            RenderUtils2.drawCustomBox(
                    x,
                    y,
                    z,
                    3.0f, // largura/comprimento de 3 blocos
                    1.0f, // altura de 1 bloco
                    0x5500FF00 // verde translúcido
            );
        }
    }

    /**
     * Retorna a lista de locais de spawn atuais
     */
    public List<BlockPos> getSpawnLocations() {
        return MutantSpawnDataManager.getSpawnLocations();
    }
}
