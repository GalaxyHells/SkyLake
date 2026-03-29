package com.galaxyhells.skylake.features.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Calendar;

public class MutantTimer {

    // Controle para não enviar a mensagem 60 vezes por segundo
    private boolean alerted = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!com.galaxyhells.skylake.config.ConfigHandler.mutantTimer) return;
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        // Pegamos o tempo real (IRL) do computador
        Calendar calendar = Calendar.getInstance();
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        // A mágica matemática dos 12 minutos
        int minutesPassed = minutes % 12;
        int minutesRemaining = 11 - minutesPassed;
        int secondsRemaining = 59 - seconds;

        // Faltam exatamente 1 minuto e 0 segundos
        if (minutesRemaining == 1 && secondsRemaining == 0) {
            if (!alerted) {
                // Notifica no chat
                mc.thePlayer.addChatMessage(new ChatComponentText("§5§l[SkyLake] §dPrepare-se! O Enderman Mutante nasce em 1 minuto!"));

                // Toca o som (note.pling é excelente para alertas)
                mc.thePlayer.playSound("note.pling", 1.0F, 2.0F);

                alerted = true; // Trava para não tocar de novo
            }
        } else if (minutesRemaining != 1) {
            // Destrava o alerta para o próximo ciclo
            alerted = false;
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (!com.galaxyhells.skylake.config.ConfigHandler.mutantTimer) return;
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getMinecraft();

        // Recalculamos o tempo para a tela
        Calendar calendar = Calendar.getInstance();
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        int minutesRemaining = 11 - (minutes % 12);
        int secondsRemaining = 59 - seconds;

        // Formata o texto para sempre ter 2 casas (ex: 04:09)
        String timeText = String.format("%02d:%02d", minutesRemaining, secondsRemaining);

        // Sistema de Cores Dinâmico (Verde -> Amarelo -> Vermelho)
        int textColor = 0xFF55FF55; // Verde padrão
        if (minutesRemaining < 3) textColor = 0xFFFFFF55; // Amarelo (Faltam < 3 min)
        if (minutesRemaining < 1) textColor = 0xFFFF5555; // Vermelho (Menos de 1 min)

        ScaledResolution sr = new ScaledResolution(mc);

        // Configuração visual do retângulo
        int width = 60;
        int height = 20;
        // Coloquei no canto superior esquerdo (pode ajustar depois)
        int x = 10;
        int y = 10;

        // Fundo escuro
        Gui.drawRect(x, y, x + width, y + height, 0x90000000);
        // Borda roxa esquerda para dar o estilo "The End"
        Gui.drawRect(x, y, x + 2, y + height, 0xFFAA00AA);

        // --- ÍCONE ---
        // Nota: O Minecraft 1.8.9 não tem um bloco "Cabeça de Enderman" nativo fácil de pegar.
        // Renderizar uma cabeça customizada via NBT Base64 é muito pesado e complexo para um HUD simples.
        // A melhor alternativa nativa e limpa é a Pérola do Fim ou o Ovo de Spawn.
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 5, y + 2, 0);
        mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.ender_pearl), 0, 0);
        GlStateManager.popMatrix();

        // Desenha o relógio
        mc.fontRendererObj.drawStringWithShadow(timeText, x + 24, y + 6, textColor);
    }
}