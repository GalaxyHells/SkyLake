package com.galaxyhells.skylake.features.hud;

import com.galaxyhells.skylake.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoAFK {
    
    private static long lastActivityTime = System.currentTimeMillis();
    private static boolean isAFK = false;
    private static double lastPosX = 0;
    private static double lastPosY = 0;
    private static double lastPosZ = 0;
    private static float lastYaw = 0;
    private static float lastPitch = 0;
    
    // Configurações
    private static final long AFK_THRESHOLD = 60000; // 1 minuto
    private static final double MOVEMENT_THRESHOLD = 0.1;
    private static final float ROTATION_THRESHOLD = 1.0f;
    private static final long COMMAND_COOLDOWN = 5000; // 5 segundos entre comandos AFK
    
    private static long lastAFKCommandTime = 0;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        
        if (!ConfigHandler.afkFeature) return;
        
        EntityPlayer player = mc.thePlayer;
        long currentTime = System.currentTimeMillis();
        
        // Verifica se houve movimento
        boolean hasMovement = checkMovement(player);
        
        if (hasMovement) {
            lastActivityTime = currentTime;
            
            // Se estava AFK e voltou a se mover, envia /afk para sair do status AFK
            if (isAFK) {
                isAFK = false;
                sendAFKCommand();
            }
        } else {
            // Verifica se passou o tempo limite de inatividade
            if (!isAFK && (currentTime - lastActivityTime) >= AFK_THRESHOLD) {
                isAFK = true;
                sendAFKCommand();
            }
        }
    }
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!ConfigHandler.afkFeature) return;
        
        // Qualquer tecla pressionada conta como atividade
        lastActivityTime = System.currentTimeMillis();
        
        // Se estava AFK, sai do modo
        if (isAFK) {
            isAFK = false;
            sendAFKCommand();
        }
    }
    
    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (!ConfigHandler.afkFeature) return;
        
        // Qualquer clique ou movimento do mouse conta como atividade
        lastActivityTime = System.currentTimeMillis();
        
        // Se estava AFK, sai do modo
        if (isAFK) {
            isAFK = false;
            sendAFKCommand();
        }
    }
    
    private boolean checkMovement(EntityPlayer player) {
        double currentPosX = player.posX;
        double currentPosY = player.posY;
        double currentPosZ = player.posZ;
        float currentYaw = player.rotationYaw;
        float currentPitch = player.rotationPitch;
        
        // Verifica se houve movimento significativo
        boolean movedX = Math.abs(currentPosX - lastPosX) > MOVEMENT_THRESHOLD;
        boolean movedY = Math.abs(currentPosY - lastPosY) > MOVEMENT_THRESHOLD;
        boolean movedZ = Math.abs(currentPosZ - lastPosZ) > MOVEMENT_THRESHOLD;
        boolean rotatedYaw = Math.abs(currentYaw - lastYaw) > ROTATION_THRESHOLD;
        boolean rotatedPitch = Math.abs(currentPitch - lastPitch) > ROTATION_THRESHOLD;
        
        // Atualiza últimas posições
        lastPosX = currentPosX;
        lastPosY = currentPosY;
        lastPosZ = currentPosZ;
        lastYaw = currentYaw;
        lastPitch = currentPitch;
        
        return movedX || movedY || movedZ || rotatedYaw || rotatedPitch;
    }
    
    private void sendAFKCommand() {
        // Evita spam de comandos AFK
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAFKCommandTime < COMMAND_COOLDOWN) {
            return;
        }
        
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            mc.thePlayer.sendChatMessage("/afk");
            lastAFKCommandTime = currentTime;
        }
    }
    
    public static boolean isAFK() {
        return isAFK;
    }
    
    public static long getTimeUntilAFK() {
        long timeSinceActivity = System.currentTimeMillis() - lastActivityTime;
        return Math.max(0, AFK_THRESHOLD - timeSinceActivity);
    }
}
