package com.galaxyhells.skylake.features.movement;

import com.galaxyhells.skylake.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoSprint {
    
    private static final double SPRINT_THRESHOLD = 0.1;
    private static final double SPRINT_REQUIRED_MOVEMENT = 1.0;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        
        if (!ConfigHandler.autoSprint) return;
        
        // Verifica se o jogador pode correr (não tem fome, não está voando, etc.)
        if (!mc.thePlayer.isSprinting() && canSprint(mc.thePlayer) && isMovingForward(mc.thePlayer)) {
            mc.thePlayer.setSprinting(true);
        }
    }
    
    private boolean canSprint(net.minecraft.entity.player.EntityPlayer player) {
        // Verifica condições para poder correr
        return player.getFoodStats().getFoodLevel() > 6 
               && !player.isSneaking() 
               && !player.isRiding() 
               && !player.isInWater() 
               && !player.capabilities.isFlying;
    }
    
    private boolean isMovingForward(net.minecraft.entity.player.EntityPlayer player) {
        // Verifica se o jogador está se movendo para frente
        double motionX = Math.abs(player.motionX);
        double motionZ = Math.abs(player.motionZ);
        
        // Verifica se está pressionando a tecla para frente
        KeyBinding forwardKey = Minecraft.getMinecraft().gameSettings.keyBindForward;
        
        return forwardKey.isKeyDown() && (motionX > SPRINT_THRESHOLD || motionZ > SPRINT_THRESHOLD);
    }
}
