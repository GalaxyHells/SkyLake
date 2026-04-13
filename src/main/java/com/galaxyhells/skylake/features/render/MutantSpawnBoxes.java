package com.galaxyhells.skylake.features.render;

import com.galaxyhells.skylake.utils.RenderUtils2;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class MutantSpawnBoxes {

    public static final MutantSpawnBoxes INSTANCE = new MutantSpawnBoxes();
    
    private volatile boolean shouldRender = true;
    
    // Locais de spawn do mutante (coordenadas X, Y, Z)
    // Você precisa fornecer os outros 7 locais
    private static final double[][] SPAWN_LOCATIONS = {
            {423.5, 66, 469.5},
            {441.5, 66, 434.5},
            {385.5, 64, 452.5},
            {489.5, 64, 455.5},
            {369.5, 70, 365.5},
            {507.5, 64, 381.5},
            {389.5, 64, 408.5},
            {520.5, 63, 427.5}
    };

    public void setEnabled(boolean enabled) {
        this.shouldRender = enabled;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        // Verifica se a feature está ativa na config
        if (!com.galaxyhells.skylake.config.ConfigHandler.mutantSpawnBoxes) return;
        
        // Verifica se a coordenada Z do jogador é > 315
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;
        double playerZ = mc.thePlayer.posZ;
        if (playerZ <= 315) return;

        // Renderiza caixas de 3x3x1 em cada local de spawn
        for (double[] location : SPAWN_LOCATIONS) {
            double x = location[0];
            double y = location[1];
            double z = location[2];
            
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
     * Adiciona um novo local de spawn dinamicamente
     */
    public void addSpawnLocation(double x, double y, double z) {
        // Esta função permite adicionar locais via comando se necessário
        // Por enquanto, os locais são hardcoded
    }

    /**
     * Retorna a lista de locais de spawn atuais
     */
    public List<BlockPos> getSpawnLocations() {
        List<BlockPos> locations = new ArrayList<>();
        for (double[] loc : SPAWN_LOCATIONS) {
            locations.add(new BlockPos(loc[0], loc[1], loc[2]));
        }
        return locations;
    }
}
