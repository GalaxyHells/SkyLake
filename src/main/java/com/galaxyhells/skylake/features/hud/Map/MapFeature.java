package com.galaxyhells.skylake.features.hud.Map;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class MapFeature {
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_M) {
//            if (Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MAP_FEATURE))) {
//                Minecraft mc = Minecraft.getMinecraft();
//                if (mc.currentScreen == null) {
//                    mc.displayGuiScreen(new MapGui());
//                }
//            }
        }
    }
}
