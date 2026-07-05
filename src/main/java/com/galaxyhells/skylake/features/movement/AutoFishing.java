package com.galaxyhells.skylake.features.movement;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.OptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoFishing {
    
    private static final String WAITING_TAG = "...";
    private static final String BITE_TAG = "[!]";
    private static final String MISSED_TAG = "Não fisgou a tempo";
    
    private EntityFishHook myBobber = null;
    private String lastNametag = "";
    private long lastCheckTime = 0;
    private static final long CHECK_INTERVAL = 100; // Check every 100ms
    
    private long lastRightClickTime = 0;
    private static final long RIGHT_CLICK_COOLDOWN = 500; // 500ms between right-clicks
    
    private boolean waitingForRecast = false;
    private long recastDelay = 0;
    private static final long RECAST_DELAY = 600; // 600ms delay before recasting
    
    private static AutoFishing instance;
    
    public AutoFishing() {
        instance = this;
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_FISHING))) {
            myBobber = null;
            lastNametag = "";
            waitingForRecast = false;
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        
        // Handle recast delay
        if (waitingForRecast) {
            if (currentTime - recastDelay >= RECAST_DELAY) {
                rightClick();
                waitingForRecast = false;
            }
            return;
        }
        
        // Check for bobber nametag changes at intervals
        if (currentTime - lastCheckTime >= CHECK_INTERVAL) {
            lastCheckTime = currentTime;
            checkBobber(mc);
        }
    }
    
    private void checkBobber(Minecraft mc) {
        // Find the player's fishing bobber
        EntityFishHook playerBobber = mc.thePlayer.fishEntity;
        
        if (playerBobber == null) {
            myBobber = null;
            lastNametag = "";
            return;
        }
        
        myBobber = playerBobber;
        
        // Check the nametag of the bobber
        String currentNametag = myBobber.getCustomNameTag();
        
        if (currentNametag == null || currentNametag.isEmpty()) {
            lastNametag = "";
            return;
        }
        
        // Strip Minecraft color codes for comparison
        String cleanNametag = stripColorCodes(currentNametag);
        String cleanLastNametag = stripColorCodes(lastNametag);
        
        // Check if nametag changed from "..." to "[!]"
        if (cleanLastNametag.equals(WAITING_TAG) && cleanNametag.equals(BITE_TAG)) {
            // Fish bit! Reel it in
            rightClick();
            waitingForRecast = true;
            recastDelay = System.currentTimeMillis();
        }
        
        // Check if nametag shows "Não fisgou a tempo"
        if (cleanNametag.equals(MISSED_TAG)) {
            // Missed catch! Reel and recast
            rightClick();
            waitingForRecast = true;
            recastDelay = System.currentTimeMillis();
        }
        
        lastNametag = currentNametag;
    }
    
    private String stripColorCodes(String input) {
        if (input == null) return "";
        // Remove Minecraft color codes (§ followed by any character)
        return input.replaceAll("§[0-9a-fk-or]", "");
    }
    
    private void rightClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRightClickTime < RIGHT_CLICK_COOLDOWN) {
            return;
        }
        
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            // Simulate right-click using KeyBinding
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            lastRightClickTime = currentTime;
        }
    }
    
    public static void triggerReelAndRecast() {
        if (instance != null) {
            instance.rightClick();
            instance.waitingForRecast = true;
            instance.recastDelay = System.currentTimeMillis();
        }
    }
}
