package com.galaxyhells.skylake.listener;

import com.galaxyhells.skylake.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatListener {

    private String displayMessage = "";
    private long displayTime = 0;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!ConfigHandler.bossAlert) return;

        String message = event.message.getUnformattedText();

        if (message.contains("Enderman Mutante foi visto nas proximidades") || message.contains("Headless Horseman")) {
            this.displayMessage = "§c§lBOSS DETECTADO!";
            this.displayTime = System.currentTimeMillis(); // Marca o tempo inicial
            Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1.0F, 1.0F);
        }
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