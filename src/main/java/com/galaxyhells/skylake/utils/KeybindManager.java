package com.galaxyhells.skylake.utils; // Ajuste para o pacote que você criar

import com.galaxyhells.skylake.features.hud.SkyTabMenuGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeybindManager {

    // Declara a nossa tecla
    public static KeyBinding openMenuKey;

    // Método para registrar a tecla nas configurações do jogo
    public static void register() {
        // Parâmetros: Nome da ação, Tecla padrão (P), Categoria nas opções do Minecraft
        openMenuKey = new KeyBinding("Abrir Menu", Keyboard.KEY_P, "SkyLake");
        ClientRegistry.registerKeyBinding(openMenuKey);
    }

    // Fica ouvindo os inputs do teclado
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // Verifica se a nossa tecla mapeada foi pressionada
        if (openMenuKey.isPressed()) {
            Minecraft mc = Minecraft.getMinecraft();

            // Só abre o menu se o jogador estiver de fato jogando (sem chat ou pause aberto)
            if (mc.inGameHasFocus && mc.currentScreen == null) {
                mc.displayGuiScreen(new SkyTabMenuGui());
            }
        }
    }
}