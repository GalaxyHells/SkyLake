package com.galaxyhells.skylake.listener;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.features.render.MutantHighlight;
import com.galaxyhells.skylake.utils.OptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class ChatListener {

    private String displayMessage = "";
    private long displayTime = 0;
    
    private int soundCount = 0;
    private long lastSoundTime = 0;
    private static final long SOUND_INTERVAL = 200; // 100ms between sounds
    
    
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        String cleanMessage = stripColorCodes(message);

        
        // Boss alert
        if (Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.BOSS_ALERT))) {
            if (message.contains("Enderman Mutante foi visto nas proximidades") || message.contains("Headless Horseman")) {
                this.displayMessage = "§c§lBOSS DETECTADO!";
                this.displayTime = System.currentTimeMillis(); // Marca o tempo inicial
                Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1.0F, 1.0F);
                
                // Força verificação imediata do mutante
                MutantHighlight.forceCheck();
            }
        }
        
        // Auto fishing - detect sea creature
        if (Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_FISHING))) {
            if (cleanMessage.contains("Você encontrou uma Criatura Marinha enquanto pescava!")) {
                soundCount = 3;
                lastSoundTime = System.currentTimeMillis();
            }
        }
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        
        // Handle triple sound playback
        if (soundCount > 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSoundTime >= SOUND_INTERVAL) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.thePlayer != null) {
                    mc.thePlayer.playSound("note.pling", 1.0F, 1.0F);
                    soundCount--;
                    lastSoundTime = currentTime;
                }
            }
        }
    }
    
    private String stripColorCodes(String input) {
        if (input == null) return "";
        return input.replaceAll("§[0-9a-fk-or]", "");
    }

    
    
    
    
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        // Só desenhamos se for a fase de renderização de TEXTO e se houver mensagem
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || displayTime == 0) return;

        // Verifica se já passaram 3 segundos (3000ms)
        if (System.currentTimeMillis() - displayTime > 3000) {
            displayTime = 0;
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        int x = sr.getScaledWidth() / 2;
        int y = sr.getScaledHeight() / 2 - 20; // Um pouco acima da mira

        // Desenha o texto centralizado
        mc.fontRendererObj.drawStringWithShadow(displayMessage,
                x - (mc.fontRendererObj.getStringWidth(displayMessage) / 2),
                y, 0xFFFFFF);
    }
}
