package com.galaxyhells.skylake.features.combat;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.OptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoAttack {
    
    private static final int CLICK_INTERVAL_TICKS = 2;
    private int cooldownTicks = 0;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_ATTACK))) return;
        
        // Decrementa cooldown
        if (cooldownTicks > 0) {
            cooldownTicks--;
            return;
        }
        
        // Simula clique do botão esquerdo (ataque) usando KeyBinding
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
        KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
        
        cooldownTicks = CLICK_INTERVAL_TICKS;
    }
}
