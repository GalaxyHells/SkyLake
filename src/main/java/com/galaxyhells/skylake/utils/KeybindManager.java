package com.galaxyhells.skylake.utils; // Ajuste para o pacote que você criar

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.features.hud.SkyTabMenuGui;
import com.galaxyhells.skylake.hud.ConfigUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeybindManager {

    // Declara a nossa tecla
    public static KeyBinding openMenuKey;
    public static KeyBinding lockSlotKey;
    public static KeyBinding configKey;
    public static KeyBinding toggleAutoAttackKey;

    // Método para registrar a tecla nas configurações do jogo
    public static void register() {
        // Parâmetros: Nome da ação, Tecla padrão (P), Categoria nas opções do Minecraft
        openMenuKey = new KeyBinding("Abrir Menu", Keyboard.KEY_P, "SkyLake");
        ClientRegistry.registerKeyBinding(openMenuKey);

        lockSlotKey = new KeyBinding("Bloquear Slot", Keyboard.KEY_L, "SkyLake");
        ClientRegistry.registerKeyBinding(lockSlotKey);

        configKey = new KeyBinding("Configurações", Keyboard.KEY_G, "SkyLake");
        ClientRegistry.registerKeyBinding(configKey);

        toggleAutoAttackKey = new KeyBinding("Alterar Auto Ataque", Keyboard.KEY_R, "SkyLake");
        ClientRegistry.registerKeyBinding(toggleAutoAttackKey);
    }

    // Fica ouvindo os inputs do teclado
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        // Verifica se a nossa tecla mapeada foi pressionada
        if (openMenuKey.isPressed()) {
            // Só abre o menu se o jogador estiver de fato jogando (sem chat ou pause aberto)
            if (mc.inGameHasFocus && mc.currentScreen == null) {
                mc.displayGuiScreen(new SkyTabMenuGui());
            }
        }

        if (configKey.isPressed()) {
            // Só abre a config se o jogador estiver de fato jogando (sem chat ou pause aberto)
            if (mc.inGameHasFocus && mc.currentScreen == null && SkyLake.optionsService != null) {
                mc.displayGuiScreen(new ConfigUI(SkyLake.optionsService));
            }
        }

        if (toggleAutoAttackKey.isPressed()) {
            // Só alterna se o jogador estiver jogando
            if (mc.inGameHasFocus && mc.currentScreen == null && SkyLake.optionsService != null) {
                boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(com.galaxyhells.skylake.utils.OptionType.AUTO_ATTACK));
                SkyLake.optionsService.set(com.galaxyhells.skylake.utils.OptionType.AUTO_ATTACK, !currentValue);
                SkyLake.optionsService.save();
                mc.thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText("§[SkyLake] Auto Attack: " + (!currentValue ? "§aON" : "§cOFF")));
            }
        }
    }
}
