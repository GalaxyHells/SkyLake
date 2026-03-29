package com.galaxyhells.skylake.features.hud;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class TabMenuHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        // Verifica se é a tecla TAB e se o SHIFT está pressionado
        if (Keyboard.getEventKey() == Keyboard.KEY_TAB && Keyboard.getEventKeyState()) {
//            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
//
//                // Abre o menu se não houver nenhuma outra tela aberta
//                if (mc.currentScreen == null) {
//                    mc.displayGuiScreen(new SkyTabMenuGui());
//                }
//            }
        }
    }
}