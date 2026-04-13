package com.galaxyhells.skylake.features.render.treasure;

import com.galaxyhells.skylake.data.TreasureLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class TreasureClickHandler {
    
    public static final TreasureClickHandler INSTANCE = new TreasureClickHandler();
    
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Apenas no cliente e clique direito
        if (!event.world.isRemote) return;
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
        
        BlockPos clickedPos = event.pos;
        if (clickedPos == null) return;
        
        // Verifica se há tesouros próximos (raio de 2 blocos)
        List<BlockPos> nearbyTreasures = getNearbyTreasures(clickedPos, 2);
        
        if (!nearbyTreasures.isEmpty()) {
            // Marca o tesouro mais próximo como coletado
            BlockPos treasurePos = findClosestTreasure(clickedPos, nearbyTreasures);
            markTreasureAsCollected(treasurePos);
        }
    }
    
    private List<BlockPos> getNearbyTreasures(BlockPos center, int radius) {
        List<BlockPos> nearby = new ArrayList<>();
        
        try {
            // Carrega todos os tesouros do mundo atual
            String world = com.galaxyhells.skylake.utils.WorldUtils.getCurrentArea();
            List<BlockPos> allTreasures = TreasureLogger.getCoordsFromFile(world + ".json");
            
            // Remove os já coletados
            List<BlockPos> collected = TreasureLogger.getCoordsFromFile(world + "_coletados.json");
            allTreasures.removeIf(collected::contains);
            
            for (BlockPos treasure : allTreasures) {
                double distance = Math.sqrt(
                    Math.pow(treasure.getX() - center.getX(), 2) +
                    Math.pow(treasure.getY() - center.getY(), 2) +
                    Math.pow(treasure.getZ() - center.getZ(), 2)
                );
                
                if (distance <= radius) {
                    nearby.add(treasure);
                }
            }
        } catch (Exception e) {
            System.err.println("[SkyLake] Erro ao buscar tesouros próximos: " + e.getMessage());
        }
        
        return nearby;
    }
    
    private BlockPos findClosestTreasure(BlockPos center, List<BlockPos> treasures) {
        BlockPos closest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (BlockPos treasure : treasures) {
            double distance = Math.sqrt(
                Math.pow(treasure.getX() - center.getX(), 2) +
                Math.pow(treasure.getY() - center.getY(), 2) +
                Math.pow(treasure.getZ() - center.getZ(), 2)
            );
            
            if (distance < minDistance) {
                minDistance = distance;
                closest = treasure;
            }
        }
        
        return closest;
    }
    
    private void markTreasureAsCollected(BlockPos pos) {
        try {
            String world = com.galaxyhells.skylake.utils.WorldUtils.getCurrentArea();
            String fileName = world + ".json";
            
            TreasureLogger.markAsCollected(fileName, pos);
            
            if (Minecraft.getMinecraft().thePlayer != null) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    new ChatComponentText("§b[SkyLake] §6Tesouro em §e" + 
                        pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " §6marcado como coletado!")
                );
            }
        } catch (Exception e) {
            System.err.println("[SkyLake] Erro ao marcar tesouro como coletado: " + e.getMessage());
        }
    }
}
